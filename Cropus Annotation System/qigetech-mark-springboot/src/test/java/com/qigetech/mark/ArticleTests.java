package com.qigetech.mark;

import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.qigetech.mark.article.entity.Article;
import com.qigetech.mark.article.service.IArticleService;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.OriginStatus;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleTests {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private ILabelResultService labelResultServiceImpl;
    @Autowired
    private ResultBundleBuilder resultBundleBuilder;
    @Autowired
    private IArticleService articleServiceImpl;


    @Test
    public void test()  {
        List<Article> articles = articleServiceImpl.list(null);
        System.out.println(articles.size());
        for(Article article:articles){
            String regEx="[。？！]";
            Pattern p =Pattern.compile(regEx);
            /*按照句子结束符分割句子*/
            String[] segments = p.split(article.getContent());
            System.out.println(segments.length);
            for( int i = 0;i<segments.length;i++){
                String segment = segments[i];
                if(StringUtils.isEmpty(segment)){
                    continue;
                }
                Origin origin = new Origin();
                origin.setSentence(segment);
                origin.setSystemLabel(NLPTokenizer.analyze(segment).toString());
                origin.setSource(article.getSource());
                origin.setLanguage("繁体");

                origin.setStatus(OriginStatus.UNLABELED.getData());
                origin.setArticleId(article.getId());
                origin.setArticleLocation(i);
                try {
                    originServiceImpl.save(origin);
                    System.out.println(origin.getId()+"\t"+origin.getLanguage());
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    private static boolean isChinnese(String str) {
        String reg = "[\\u4E00-\\u9FA5]+";
        Matcher m = Pattern.compile(reg).matcher(str);
        if (m.find()) {
            return true;
        }else{
            return false;
        }
    }

}
