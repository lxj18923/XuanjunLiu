package com.qigetech.mark.result.label.service.count.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.result.label.entity.count.CountAll;
import com.qigetech.mark.result.label.entity.count.CountStu;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.result.label.mapper.count.CountAllMapper;
import com.qigetech.mark.result.label.mapper.count.CountStuMapper;
import com.qigetech.mark.result.label.service.count.ICountStuService;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.mapper.user.UserMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountStuServiceImpl extends ServiceImpl<CountStuMapper, CountStu> implements ICountStuService {
    @Autowired
    private TransportClient client;

    @Resource
    private CountStuMapper countStuMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CountAllMapper countAllMapper;

    @Resource
    private LabelResultMapper labelResultMapper;

    @Scheduled(cron = "0 00 04 ? * *")
    @Override
    public void countBySort() {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().ge("id" ,2730));
        countStuMapper.reCreateCountStu();//重置sql中count_stu的数据
        List<CountStu> countStuList = new ArrayList<>();
        for (User user : users) {
            countStuList.add(countByUserId(user));
        }
        List<CountStu> result = countStuList.stream().sorted(Comparator.comparingInt(CountStu::getCountTotal)).collect(Collectors.toList());
        Collections.reverse(result);//有这一步就是按countTotal从大到小排，倒排的话删掉就好
        this.saveBatch(result);
        //result.forEach(System.out::println);
    }

    @Override
    public CountStu countByUserId(User user) {
        CountStu countStu = new CountStu();
        SearchResponse response = client.prepareSearch("label_result").setTypes("doc")
                .setQuery(QueryBuilders.termQuery("userId",user.getId()))
                .addAggregation(AggregationBuilders.dateHistogram("markDate").field("markDate")
                        .order(BucketOrder.key(false))
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .subAggregation(AggregationBuilders
                                .terms("groupId")
                                .field("originId")
                                .size(20000)))
                .addAggregation(AggregationBuilders
                        .terms("groupId2")
                        .field("originId")
                        .size(20000))
                .get();
        Histogram markDate = response.getAggregations().get("markDate");
        Terms group = response.getAggregations().get("groupId2");
        int day = 0;
        int total = 0;
        for (Histogram.Bucket bucketObj: markDate.getBuckets()) {
            LongTerms sum = bucketObj.getAggregations().get("groupId");
            total += sum.getBuckets().size();
            if (day == 0){
                countStu.setCountDaily(total);
                countStu.setLastMarkdate(bucketObj.getKey().toString().substring(0,10));
            }
            if (day <= 6){
                countStu.setCountWeek(total);
            }
            day++;
        }
        countStu.setUsername(user.getName());
        countStu.setUserId((int)user.getId());
        countStu.setCountTotal(group.getBuckets().size());
        if (countStu.getLastMarkdate() == null){
            countStu.setLastMarkdate("无记录");
        }
        if (countStu.getCountDaily() == null){
            countStu.setCountDaily(0);
        }
        if (countStu.getCountWeek() == null){
            countStu.setCountWeek(0);
        }
        return countStu;
    }

    /** author：LXJ date：2020.4.6 */
    @Scheduled(cron = "0 00 04 ? * *")
    @Override
    public void  dailyCount() {
        CountAll countAll = new CountAll();
        Date date = new Date();
        List<Integer> list = labelResultMapper.countAllDaily();
        countAll.setAmount(list.size());
        countAll.setCountDate(new Date(date.getTime()-5*60*60*1000));
        countAllMapper.insert(countAll);
    }

    /** author：ZZY date：2020.4.2
    @Scheduled(cron = "0 00 04 ? * *")
    @Override
    public void  dailyCount() {
        CountAll countAll = new CountAll();
        SearchResponse response = client.prepareSearch("label_result").setTypes("doc")
                .addAggregation(AggregationBuilders.dateHistogram("markDate").field("markDate")
                        .order(BucketOrder.key(false))
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .subAggregation(AggregationBuilders
                                .terms("groupId")
                                .field("originId")
                                .size(10000)
                        )
                )
                .get();
        Histogram markDate = response.getAggregations().get("markDate");
        int day = 1;
        for (Histogram.Bucket bucketObj: markDate.getBuckets()) {
            LongTerms sum = bucketObj.getAggregations().get("groupId");
            if (day==2){
                countAll.setAmount(sum.getBuckets().size());
                Date date = new Date();
                countAll.setCountDate(new Date(date.getTime()-24*60*60*1000));
                countAllMapper.insert(countAll);
                System.out.println("markCount insert success! -----" + countAll.getCountDate());
                break;
            }else {
                day++;
            }
        }
    }*/

    /** 未排序的拿个人标注情况分页的方法，暂时弃用
    public Page<StuCountVO> getLabelCountVO(long current, long size){
        Page<User> userPage = new Page<>(current,size);
        Page<StuCountVO> stuCountVOPage = new Page<>(current,size);
        List<StuCountVO> stuCountVOS = new ArrayList<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().ge("id",2730);
        IPage<User> userIPage = userServiceImpl.page(userPage,queryWrapper);

        List<User> users = userIPage.getRecords();

        for (User user : users) {
            stuCountVOS.add(countByUserId2(user));
        }
        stuCountVOPage.setRecords(stuCountVOS);
        return stuCountVOPage;
    }

    public StuCountVO countByUserId2(User user) {
        StuCountVO stuCountVO = new StuCountVO();

        SearchResponse response = client.prepareSearch("label_result").setTypes("doc")
                .setQuery(QueryBuilders.termQuery("userId", user.getId()))
                .addAggregation(AggregationBuilders.dateHistogram("markDate").field("markDate")
                        .order(BucketOrder.key(false))
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .subAggregation(AggregationBuilders
                                .terms("groupId")
                                .field("originId")
                                .size(20000)))
                .addAggregation(AggregationBuilders
                        .terms("groupId2")
                        .field("originId")
                        .size(20000))
                .get();
        Histogram markDate = response.getAggregations().get("markDate");
        Terms group = response.getAggregations().get("groupId2");
        int day = 0;
        int total = 0;
        for (Histogram.Bucket bucketObj: markDate.getBuckets()) {
            LongTerms sum = bucketObj.getAggregations().get("groupId");
            total += sum.getBuckets().size();
            if (day == 0){
                stuCountVO.setLastMarkDate(bucketObj.getKey().toString().substring(0,10));
                stuCountVO.setCountDaily(total);
//                System.out.println(userId +" ----- "+ bucketObj.getKey().toString().substring(0,10)+ "-----" + total);
            }
            if (day <= 6){
                stuCountVO.setCountWeek(total);
//                System.out.println("week ----- " + total);
            }
            day++;
        }
        stuCountVO.setUserId((int)user.getId());
        stuCountVO.setUsername(user.getName());
        stuCountVO.setCountTotal(group.getBuckets().size());
        return stuCountVO;
    }*/

}
