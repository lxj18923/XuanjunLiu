package com.qigetech.mark;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.qigetech.mark.article.entity.Article;
import com.qigetech.mark.article.service.IArticleService;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.OriginStatus;
import com.qigetech.mark.origin.entity.OriginUser;
import com.qigetech.mark.origin.mapper.OriginMapper;
import com.qigetech.mark.origin.mapper.OriginUserMapper;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.utils.PasswordUtils;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private ILabelResultService labelResultService;
    @Resource
    private LabelResultMapper labelResultMapper;
    @Autowired
    private ResultBundleBuilder resultBundleBuilder;
    @Autowired
    private IArticleService articleServiceImpl;
    @Test
    public void contextLoads() {
        for(int i=1;i<5;i++){
            Origin origin = new Origin();
            origin.setSentence("这是一个测试"+i);
            origin.setSystemLabel("这是/n 一个/n 测试/n "+i+"/num");
            origin.setLanguage("中文简体");
            origin.setSource("测试");
            originServiceImpl.save(origin);
        }
    }

    @Test
    public void test() throws IOException {
        Collection<File> files =  FileUtils.listFiles(new File("C:\\Users\\LXJ\\Desktop\\语料标注\\SpiderTXT\\香港律政司网站(简体)"),null,false);
        for(File file : files){
            try{
                String text = FileUtils.readFileToString(file);
                String time = file.getName().split("。")[0]
                        .replaceAll("星期一","")
                        .replaceAll("星期二","")
                        .replaceAll("星期三","")
                        .replaceAll("星期四","")
                        .replaceAll("星期五","")
                        .replaceAll("星期六","")
                        .replaceAll("星期日","")
                        .replaceAll("年","-").replaceAll("月","-");
                //String title = file.getName().split("。")[1].replaceAll(".txt","");
                text = text.replaceAll("＊","")
                        .replaceAll("　","")
                        .replaceAll(" ","")
                        .replaceAll("\t","")
                        .replaceAll("\n","");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if(isChinnese(text)){
                    Article article = new Article();
                    article.setSource("明报");
                    article.setContent(text);
//                    article.setTitle(title);
                    //article.setPostDate(format.parse(time));
                    articleServiceImpl.save(article);
                    String regEx="[。？！?!]";
                    Pattern p =Pattern.compile(regEx);
                    /*按照句子结束符分割句子*/
                    String[] segments = p.split(text);
                    for(int location = 0 ; location<segments.length;location++){
                        String segment = segments[location];
                        Origin origin = new Origin();
                        origin.setSentence(segment);
                        origin.setSystemLabel(NLPTokenizer.analyze(segment).toString());
                        origin.setSource("明报");
                        origin.setLanguage("繁体");
                        origin.setStatus(OriginStatus.UNLABELED.getData());
                        origin.setArticleId(article.getId());
                        origin.setArticleLocation(location);
                        System.out.println(origin);
                        originServiceImpl.save(origin);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                continue;
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


    @Test
    public void group(){
        List<LabelResult> totalResults = labelResultMapper.countTotal(Long.valueOf(1));
        List<LabelResult> weekResults = labelResultMapper.countWeek(Long.valueOf(1));
        List<LabelResult> dailyResults = labelResultMapper.countDaily(Long.valueOf(1));
        System.out.println(totalResults.size());
        System.out.println(weekResults.size());
        System.out.println(dailyResults.size());
    }

    @Resource
    private OriginUserMapper originUserMapper;

    @Resource
    private OriginMapper originMapper;

    @Resource
    private IOriginService originService;

    @Test
    public void testGetOrigin(){
        Long userId = Long.valueOf(2733);
        String language = "简体";
        OriginUser originUser = originUserMapper.selectOne(new QueryWrapper<OriginUser>().eq("user_id",userId)
            .eq("status", 0));
        Origin origin;
        if(originUser==null||originUser.getOriginId()==null){
            Origin origin1 = testGetOriginByCheck(userId,language);
            OriginUser originUser1 = new OriginUser();
            originUser1.setUserId(userId.intValue());
            originUser1.setOriginId(origin1.getId());
            originUser1.setSub_date(new Date());
            originUser1.setSub_status(0);
            originUserMapper.insert(originUser1);
            origin = originService.getById(origin1.getId());
        }else{
            origin = originService.getById(originUser.getOriginId());
        }
        System.out.println(origin);
    }

    public Origin testGetOriginByCheck(Long userId, String language){
        Origin origin = originMapper.getOriginAfterCheck(userId, language, 2);
        if (origin == null){
            origin = originMapper.getOriginAfterCheck(userId, language, 1);
            if (origin == null){
                origin = originMapper.getOriginAfterCheck(userId, language, 0);
                return origin;
            }
            return origin;
        }
        return origin;
    }

    @Test
    public void testGetOriginAfterCheck(){
        Origin origin = originService.getOriginByCheck(Long.valueOf(1), "简体");
        System.out.println(origin.getId());
    }

    @Test
    public void testGetOriginWithoutCheck(){
//        Origin origin = originService.getOriginByNoCheck("简体");
//        System.out.println(origin);
        System.out.print( PasswordUtils.getSlatPwMd5("longcongjun"));
    }

    @Test
    public void testNewGetOrigin(){
        int userId = 1;
        String language = "简体";
        long start = System.currentTimeMillis();
        Origin origin = originService.getOriginByCheck((long) userId, language);
        System.out.println(origin);
        long end = System.currentTimeMillis();
        System.out.println("--------------- " + (end - start) + "ms ---------------");
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        ValueOperations<String, String> valueStr = redisTemplate.opsForValue();
        //存储一条数据
        valueStr.append("Connection","...");
        valueStr.set("Connection","OK");
        //获取一条数据并输出
        String status = valueStr.get("Connection");
        System.out.println(status);
    }
    @Test
    public void testRedis2(){
        SetOperations<String, String> set = redisTemplate.opsForSet();
        set.add(123+"skip", "12580");
        boolean b = set.isMember("1"+"skip", "12580");
        System.out.println(b);
    }

    @Test
    public void initOriginRedis() {
        String[] languages = {"简体", "繁体"};
        int full = 5000;
        for (String string : languages){
            int size = Math.toIntExact(redisTemplate.opsForList().size(string));
            List<Integer> list = originMapper.getOriginIdList(full-size, string);
            if (full != size ){
                redisTemplate.opsForList().rightPushAll(string, list);
            }
        }

        System.out.println(new Date()+": initOriginRedis is OK!");
    }

    @Test
    public void initOriginRedis2() {
        String[] languages = {"简体", "繁体"};
        int full = 5000;
        int i = 0;
        for (String lan : languages){
            int size = Math.toIntExact(redisTemplate.opsForList().size(lan));
            List<Integer> list = originMapper.getOriginIdList(full-size, lan);
            List<String> list2 = new ArrayList<>();
            for (Integer integer : list) {
                list2.add(String.valueOf(integer));
            }
            for (Integer originId : list) {
                List<LabelResult> resultList = labelResultMapper.selectList(
                        new QueryWrapper<LabelResult>().eq("origin_id", originId)
                );
                Map<Integer,List<LabelResult>> resultMap = resultList.stream().collect(Collectors.groupingBy(LabelResult::getUserId));
                for(Integer key : resultMap.keySet()){
                    List<LabelResult> labelResults = resultMap.get(key);
                    final StringBuilder str = new StringBuilder();
                    labelResults.forEach(labelResult -> {
                        String encodeWord = labelResult.getWord();
                        str.append(encodeWord+"/"+labelResult.getPartOfSpeech()+" ");
                    });
                    System.out.println(++i);
                    redisTemplate.opsForSet().add("vo"+originId, str.toString());
                }
            }
            if (size != full){
                redisTemplate.opsForList().rightPushAll(lan, list2);
            }
        }
        System.out.println(new Date()+": initOriginRedis is OK!");
    }

    @Test
    public void initRedisOrigin(){
        System.out.println(new Date() + ": initOriginRedis start!");
        long start = System.currentTimeMillis();
        try {
            String[] languages = {"简体", "繁体"};
            int full = 5000;
            for (String lan : languages){
                redisTemplate.opsForList().trim(lan,0,0);
                redisTemplate.opsForList().leftPop(lan);
                List<Integer> list = originMapper.getOriginIdList(full, lan);
                for (Integer originId : list) {
                    List<LabelResult> resultList = labelResultMapper.selectList(
                            new QueryWrapper<LabelResult>().eq("origin_id", originId)
                    );
                    Map<Integer,List<LabelResult>> resultMap = resultList.stream().collect(Collectors.groupingBy(LabelResult::getUserId));
                    for(Integer key : resultMap.keySet()){
                        List<LabelResult> labelResults = resultMap.get(key);
                        final StringBuilder str = new StringBuilder();
                        labelResults.forEach(labelResult -> {
                            String encodeWord = labelResult.getWord();
                            str.append(encodeWord).append("/").append(labelResult.getPartOfSpeech()).append(" ");
                        });
                        redisTemplate.opsForSet().add("vo"+originId, str.toString());
                    }
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(new Date() + ": initOriginRedis is OK! (" + (end - start)/60000 + " min)");
        } catch (RuntimeException e){
            e.printStackTrace();
            long end = System.currentTimeMillis();
            System.out.println(new Date() + ": initOriginRedis is failed! (" + (end - start)/60000 + " min)");
        }
    }

    @Test
    public void newGetOrigin(){
        Long userId = Long.valueOf(3281);
        String language = "runoobkey";
//        List<String> list2 = new ArrayList<>();
//        list2.add("123");
//        list2.add("234");
//        list2.add("345");
//        redisTemplate.opsForList().rightPushAll(language, list2);
//        redisTemplate.opsForList().trim(language,0,0);
//        redisTemplate.opsForList().leftPop(language);
        redisTemplate.opsForSet().add("abaaba", "西巴");
        redisTemplate.opsForSet().add("abaaba", "阿巴");
        redisTemplate.opsForSet().add("abaaba", "辛巴");
        redisTemplate.opsForSet().add("abaaba", "阿巴");
        Set set = redisTemplate.opsForSet().members("abaaba");
        set.forEach(System.out::println);

//         获取origin测试
//        Origin origin = originService.getOrigin(userId, language);
//        System.out.println(origin);

        // 跳过测试
//        originService.skip(userId, language);
//        Origin origin = originService.getOrigin(userId, language);
//        System.out.println(origin);

        // 增加labelresultvo 测试
//        redisTemplate.opsForSet().add("vo"+123,"驾车/v 人士/n 驶/v 经/p 沙田/n 一带/n ，/w 请/v 留意/v 警员/n 的/u 指示/n 及/c 交通标志/nz");
//        Set set = redisTemplate.opsForSet().members("vo"+146158);
//        System.out.println(set);

//        List<LabelResultVO> labelResultVOList = labelResultService.getLabelResultVO(userId, language);
//        labelResultVOList.forEach(System.out::println);
//        List<LabelResultVO> labelResultVOList = labelResultService.getLabelResultByOriginId(161263);
//        labelResultVOList.forEach(System.out::println);
//        List<Integer> idList = redisTemplate.opsForList().range(language,0,-1);
//        System.out.println(idList);
//        List<LabelResultVO> labelResultVOList = labelResultService.getLabelResultByOriginId(154498);
//        labelResultVOList.forEach(System.out::println);
    }

}