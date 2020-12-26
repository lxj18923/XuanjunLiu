package com.qigetech.mark.origin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.mapper.OriginRepository;
import com.qigetech.mark.origin.mapper.OriginMapper;
import com.qigetech.mark.origin.service.IEsOriginService;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class EsOriginServiceImpl extends ServiceImpl<OriginMapper, Origin> implements IEsOriginService {
    @Autowired
    private TransportClient client;
    @Autowired
    private OriginRepository originRepository;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean updateStatusByEs(int originId, int status) {//在es更新origin状态
        try{
            //        OriginBackup originBackup=originBackupRepository.queryOriginById(String.valueOf(originId));
            Origin origin = originRepository.queryOriginById(originId);
//        System.out.println("originBackup.getStatus()="+originBackup.getStatus());
//        originBackup.setStatus(String.valueOf(status));
            origin.setStatus(status);
            originRepository.save(origin);
//        System.out.println("originBackup.getStatus()="+originBackup.getStatus());
        } catch (Exception e){
            System.out.println(e);
        }
        return true;
    }

    @Override
    public Map<String, Object> findByStatusOrSource(long current, long size, Integer status, String source, String sentence) {
        //构建查询器
        QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("sentence", sentence).minimumShouldMatch("80%");
        QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("source", source);
        QueryBuilder queryBuilder3 = QueryBuilders.matchPhraseQuery("status", status);
        //QueryBuilder queryBuilder4 = QueryBuilders.matchAllQuery();
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();

        //判断
        if (!sentence.equals("")) {
            bqb.must(queryBuilder1);
        }
        if (!source.equals("")) {
            bqb.must(queryBuilder2);
        }
        bqb.must(queryBuilder3);
//        FieldSortBuilder fsb = SortBuilders.fieldSort("updateDate").order(SortOrder.DESC); 按日期排序

        //分页
        Pageable pageable = PageRequest.of((int) current - 1, (int) size);
        System.out.println("当前页="+current);


        //查询
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(bqb)
//                .withSort(fsb)
                .build();
        query.setPageable(pageable);


        // 深度查询分页
//        Page<OriginBackup> result =
//                this.elasticsearchTemplate.startScroll(2000000, query, OriginBackup.class);
//        for (int i = 0; i < current - 1; i++) {
//            elasticsearchTemplate.continueScroll(
//                    ((ScrolledPage) result).getScrollId(), 2000000, OriginBackup.class);
//        }
        //获取originid
        Page<Origin> searchResponse = originRepository.search(query);
        List<Origin> origins = searchResponse.getContent();
        List<Integer> originIds = new ArrayList<>();
        for (Origin origin : origins) {
            originIds.add(origin.getId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("originIds", originIds);
        map.put("total", new Long(searchResponse.getTotalElements()).intValue());
        return map;
    }
}

