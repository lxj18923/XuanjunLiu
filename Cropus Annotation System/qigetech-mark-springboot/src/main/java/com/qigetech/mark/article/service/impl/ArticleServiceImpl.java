package com.qigetech.mark.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.qigetech.mark.article.entity.Article;
import com.qigetech.mark.article.mapper.ArticleMapper;
import com.qigetech.mark.article.service.IArticleService;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.OriginStatus;
import com.qigetech.mark.origin.service.IOriginService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-4-29
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private TransportClient client;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map<String, Object> uploadArticle(InputStream inputStream) throws IOException {
        Map<String, Object> countMap = new HashMap<>();
        int totalArticle = 0;
        int totalOrigin = 0;
        countMap.put("totalArticle", totalArticle);
        countMap.put("totalOrigin", totalOrigin);
        int topArticleId = getTopArticleId();
        int topOriginId = getTopOriginId();
        // 获得工作簿对象
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(inputStream);
        } catch (BiffException e) {
            e.printStackTrace();
        }
        // 获得所有工作表
        Sheet[] sheets = workbook.getSheets();
        // 遍历工作表
        if (sheets != null)
        {
            for (Sheet sheet : sheets)
            {
                // 获得行数
                int rows = sheet.getRows();
                // 读取数据并存储
                for (int row = 1; row < rows; row++) {
                    // xls文件格式
                    // 第一列为文章标题
                    String title = sheet.getCell(0, row).getContents();
                    // 第二列为链接地址
                    String url = sheet.getCell(1, row).getContents();
                    // 第三列为正文内容
                    String text = sheet.getCell(2, row).getContents();
                    // 第四列为日期信息
                    String date = sheet.getCell(3, row).getContents();
                    // 第五列为文章来源
                    String source = sheet.getCell(4, row).getContents();
                    // 第六列为语言信息
                    String language = sheet.getCell(5, row).getContents();
                    // 文件格式化
                    text = text.replaceAll("＊", "")
                            .replaceAll("　", "")
                            .replaceAll(" ", "")
                            .replaceAll(" ", "")
                            .replaceAll("\t", "")
                            .replaceAll("\r", "")
                            .replaceAll("\n", "");
                    date = date.replaceAll("星期一", "")
                            .replaceAll("星期二", "")
                            .replaceAll("星期三", "")
                            .replaceAll("星期四", "")
                            .replaceAll("星期五", "")
                            .replaceAll("星期六", "")
                            .replaceAll("星期日", "")
                            .replaceAll("年", "-")
                            .replaceAll("月", "-")
                            .replaceAll("日", "")
                            .replaceAll("时", ":")
                            .replaceAll("分", ":00");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //正文、来源或语言信息其中之一为空则跳过
                    if (text.length() == 0 || source.length() == 0 || language.length() == 0){
                        continue;
                    }
                    // 存储article和origin
                    if (isChinese(text) && textIsOK(text)) {
                        Article article = new Article();
                        article.setSource(source);
                        article.setContent(text);
                        article.setTitle(title);
                        article.setUrl(url);
                        totalArticle++;
                        if (!date.isEmpty()) {
                            try {
                                article.setPostDate(format.parse(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        this.save(article);
                        article.setId(topArticleId);
                        topArticleId++;
                        addAndUpdateArticle(article);
                        String regEx = "[。？！?!]";
                        Pattern p = Pattern.compile(regEx);
                        // 按照句子结束符分割句子
                        String[] segments = p.split(text);
                        int location = 0;
                        for (; location < segments.length; location++) {
                            String segment = segments[location];
                            Origin origin = new Origin();
                            origin.setSentence(segment);
                            origin.setSystemLabel(NLPTokenizer.analyze(segment).toString());
                            origin.setSource(source);
                            origin.setLanguage(language);
                            origin.setStatus(OriginStatus.UNLABELED.getData());
                            origin.setArticleId(article.getId());
                            origin.setArticleLocation(location);
                            origin.setUpdateDate(new Date());
                            System.out.println(origin);
                            originServiceImpl.save(origin);
                            origin.setId(topOriginId);
                            topOriginId++;
                            addAndUpdateOrigin(origin);
                        }
                        totalOrigin += location;
                    }
                }
            }
        }
        countMap.replace("totalArticle", totalArticle);
        countMap.replace("totalOrigin", totalOrigin);
        return countMap;
    }

    /**
     * 中文检测
     * @param str
     * @return
     */
    @Override
    public boolean isChinese(String str) {
        String reg = "[\\u4E00-\\u9FA5]+";
        Matcher m = Pattern.compile(reg).matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测文件内每列标题是否符合规范
     * @param inputStream
     * @return
     */
    @Override
    public boolean fileIsOK(InputStream inputStream) {
        // 获得工作簿对象
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(inputStream);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        // 获得所有工作表
        assert workbook != null;
        Sheet[] sheets = workbook.getSheets();
        // 遍历工作表
        if (sheets != null) {
            for (Sheet sheet : sheets) {
                if (sheet.getCell(0, 0).getContents().equals("标题") && sheet.getCell(1, 0).getContents().equals("链接地址") &&
                        sheet.getCell(2, 0).getContents().equals("正文内容") && sheet.getCell(3, 0).getContents().equals("日期") &&
                        sheet.getCell(4, 0).getContents().equals("文章来源") &&sheet.getCell(5, 0).getContents().equals("语言")){
                    return true;
                } else {
                    break;
                }
            }
        }
        workbook.close();
        return false;
    }

    /**
     * Elasticsearch添加article
     * @param article
     */
    @Override
    public void addAndUpdateArticle(Article article) {
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId(String.valueOf(article.getId()));
        indexQuery.setObject(article);
        indexQuery.setIndexName("article_backup");
        indexQuery.setType ( "doc" );
        elasticsearchTemplate.index ( indexQuery );
        elasticsearchTemplate.refresh("article_backup");
    }

    /**
     * Elasticsearch添加origin
     * @param origin
     */
    @Override
    public void addAndUpdateOrigin(Origin origin) {
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId(String.valueOf(origin.getId()));
        indexQuery.setObject(origin);
        indexQuery.setIndexName("origin_backup");
        indexQuery.setType ( "doc" );
        elasticsearchTemplate.index ( indexQuery );
    }

    /**
     * article查重
     * @param text
     * @return
     */
    @Override
    public boolean textIsOK(String text){
        SearchRequestBuilder srb = client.prepareSearch("article_backup").setTypes("doc");
        QueryBuilder queryBuilder = QueryBuilders.termQuery("content.keyword", text);
        SearchResponse sr = srb.setQuery(QueryBuilders.boolQuery()
                .must(queryBuilder))
                .execute()
                .actionGet();
        SearchHits hits = sr.getHits();
        return hits.getHits().length == 0;
    }

    /**
     * 由于es没设置自增id，需要获取article_backup中最新的id
     * @return
     */
    private int getTopArticleId(){
        SearchRequestBuilder srb = client.prepareSearch("article_backup").setTypes("doc").addSort("id", SortOrder.DESC).setSize(1);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse sr = srb.setQuery(QueryBuilders.boolQuery()
                .must(queryBuilder))
                .execute()
                .actionGet();
        SearchHits hits = sr.getHits();
        int id = 0;
        for (SearchHit hit : hits) {
            if (StringUtils.isNotBlank(hit.getSourceAsString())) {
                String content = hit.getSourceAsString();
                JSONObject elasticJson = JSONObject.parseObject(content);
                Article article1 = JSON.toJavaObject(elasticJson,Article.class);
                id = article1.getId() + 1;
                break;
            }
        }
        return id;
    }

    /**
     * 由于es没设置自增id，需要获取origin_backup中最新的id
     * @return
     */
    private int getTopOriginId(){
        SearchRequestBuilder srb = client.prepareSearch("origin_backup").setTypes("doc").addSort("id", SortOrder.DESC).setSize(1);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse sr = srb.setQuery(QueryBuilders.boolQuery()
                .must(queryBuilder))
                .execute()
                .actionGet();
        SearchHits hits = sr.getHits();
        int id = 0;
        for (SearchHit hit : hits) {
            if (StringUtils.isNotBlank(hit.getSourceAsString())) {
                String content = hit.getSourceAsString();
                JSONObject elasticJson = JSONObject.parseObject(content);
                Origin origin1 = JSON.toJavaObject(elasticJson,Origin.class);
                id = origin1.getId() + 1;
                break;
            }
        }
        return id;
    }
}
