package com.qigetech.mark.testLXJ;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.count.CountAll;
import com.qigetech.mark.result.label.entity.count.CountStu;
import com.qigetech.mark.result.label.entity.t_User;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.result.label.mapper.count.CountAllMapper;
import com.qigetech.mark.result.label.mapper.count.CountStuMapper;
import com.qigetech.mark.result.label.service.count.ICountStuService;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.mapper.user.UserMapper;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Testgetorigin {

    @Autowired
    private IUserService userServiceImpl; //Autowired接口

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @Autowired
    private LabelResultMapper labelResultMapper;

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Autowired
    private IOriginService originServiceImpl;

    @Resource
    private CountAllMapper countAllMapper;

    @Resource
    private CountStuMapper countStuMapper;

    @Autowired
    private TransportClient client;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private ICountStuService countStuServiceImpl;

    @Test
    public void testGetOriginByRandom(){
        long startTime =  System.currentTimeMillis();

        String username = "wangyiting";
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();

        System.out.println(originServiceImpl.getOriginLabelPage(0,20,user));

        long endTime =  System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("该段代码执行耗时：" + totalTime + " ms");
    }

    @Test
    public void testInsertBatch() throws Exception {
        long start = System.currentTimeMillis();
        List<t_User> list = new ArrayList<>();
        t_User user;
        for (int i = 0; i < 10000; i++) {
            user = new t_User();
            //user.setId(i);
            user.setName("name" + i);
            user.setDelFlag("0");
            list.add(user);
        }
        labelResultMapper.insertBatch(list);
        long end = System.currentTimeMillis();
        System.out.println("---------------" + (start - end) + "---------------");
    }

    @Test
    public void testselect(){
        long startTime =  System.currentTimeMillis();

        System.out.println(originServiceImpl.getOriginByCheck((long) 2733,"简体"));


        long endTime =  System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("该段代码执行耗时：" + totalTime + " ms");
    }

    @Test
    public void testupdate(){
        long startTime =  System.currentTimeMillis();
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name","panzejia"));
        Map<String,Object> map = labelResultServiceImpl.getOriginListByRole(0,20,user);
        System.out.println(map);
        long endTime =  System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("该段代码执行耗时：" + totalTime + " ms");
    }

    @Test
    public void testgetlabelcount(){
        User user = userServiceImpl.getById(2733);
        System.out.println(originServiceImpl.getOriginLabelPage(1,20,user));
    }


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

    @Test
    public void countBySort() {
        long startTime =  System.currentTimeMillis();
        List<User> users = userMapper.selectList(new QueryWrapper<User>().ge("id" ,2730));
        countStuMapper.reCreateCountStu();//重置sql中count_stu的数据
        List<CountStu> countStuList = new ArrayList<>();
        for (User user : users) {
            countStuList.add(countByUserId(user));
        }
        List<CountStu> result = countStuList.stream().sorted(Comparator.comparingInt(CountStu::getCountTotal)).collect(Collectors.toList());
        Collections.reverse(result);//有这一步就是按countTotal从大到小排，倒排的话删掉就好
        countStuServiceImpl.saveBatch(result);
        result.forEach(System.out::println);
        long endTime =  System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("该段代码执行耗时：" + totalTime + " ms");
    }

    @Test
    public void  dailyCount() {
        CountAll countAll = new CountAll();
        Date date = new Date();
        List<Integer> list = labelResultMapper.countAllDaily();
        countAll.setAmount(list.size());
        countAll.setCountDate(new Date(date.getTime()-5*60*60*1000));
        System.out.println("markCount insert success! -----" + countAll.getCountDate());
    }

}
