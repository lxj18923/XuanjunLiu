package com.qigetech.mark.result.label.service.labelresult.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.OriginStatus;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.entity.dto.LabelResultDTO;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.mapper.user.UserMapper;
import com.qigetech.mark.user.service.permission.ISysRoleService;
import com.qigetech.mark.user.service.user.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
@Service
public class LabelResultServiceImpl extends ServiceImpl<LabelResultMapper, LabelResult> implements ILabelResultService {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private LabelResultMapper labelResultMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    /** author：LXJ date：2020.2.1 */
    @Override
    public List<LabelResultVO> getLabelResultVO(Long userId,String language){
        List<LabelResultVO> labelResultVOS = new ArrayList<>();
        String originId = (String) redisTemplate.opsForValue().get(userId+"_mission");
        if (originId != null) {
            labelResultVOS = getLabelResultByOriginId(Integer.valueOf(originId));
        }
        return labelResultVOS;
    }

    /** author：fty date：2020.2.1 */
    @Override
    public Boolean proofreading(int x,List<LabelResult> newResults) {//交叉验证
        //第一个人标注的结果从数据库中获取
        List<LabelResult> User_1List =  labelResultMapper.selectList(new QueryWrapper<LabelResult>().eq("origin_id", x));
        //第二个人标注的结果，从前端获取
        List<LabelResult> User_2List = newResults;

        //将各个属性转存到变量里便于比较值的大小。若在list中调用对象直接比较则是在比较地址！！！！！！！
        int User_1_location, User_2_location;
        String User_1_PartOfSpeech, User_2_PartOfSpeech, User_1_Word, User_2_Word;

        //分完类后再比较两个list的大小是否相同，若list大小不同则一定分词出错
        if(User_1List.size()==User_2List.size()){
            for (int i = 0; i <= User_1List.size()-1; i++) {
                User_1_PartOfSpeech=User_1List.get(i).getPartOfSpeech();
                User_2_PartOfSpeech=User_2List.get(i).getPartOfSpeech();
                User_1_Word=User_1List.get(i).getWord();
                User_2_Word=User_2List.get(i).getWord();
                User_1_location=User_1List.get(i).getLocation();
                User_2_location=User_2List.get(i).getLocation();
                //将两个list中的每一条记录同时比较分词，词性，与标注位置，。
                if (!(User_1_Word.equals(User_2_Word)) ) {
                    //有一条不正确即都不正确

                    return false;

                }else if(!(User_1_PartOfSpeech.equals(User_2_PartOfSpeech))){
                    return false;

                }else{

                    if(i==User_1List.size()-1){
                        return true;
                    }
                    continue;

                }
            }
        }else{
            return false;

        }
        return false;
    }

    //未使用
//    @Override
//    public ProofreadingVO getProofreading(){
//        ProofreadingVO proofreading = new ProofreadingVO();
//        Origin origin = originServiceImpl.getOne(new QueryWrapper<Origin>().eq("status",OriginStatus.VERIFICATION_FAILED.getData()).last("limit 1"));
//        if(origin==null){
//            return null;
//        }
//        proofreading.setOrigin(origin);
//        List<LabelResultVO> labelResultVOS = new ArrayList<>();
//        List<LabelResult> results = this.baseMapper.selectList(new QueryWrapper<LabelResult>().eq("origin_id",origin.getId()));
//        Map<Integer,List<LabelResult>> resultMap = results.stream().collect(Collectors.groupingBy(LabelResult::getUserId));
//        for(Integer userId : resultMap.keySet()){
//            User user = userServiceImpl.getById(userId);
//            List<LabelResult> userLabelResults = resultMap.get(userId);
//            userLabelResults.sort((LabelResult result1,LabelResult result2)->result1.getLocation().compareTo(result2.getLocation()));
//            final StringBuilder str = new StringBuilder();
//            userLabelResults.forEach(labelResult -> {
//                String encodeWord = labelResult.getWord();
//                str.append(encodeWord+"/"+labelResult.getPartOfSpeech()+" ");
//            });
//            LabelResultVO resultVO = new LabelResultVO();
//            resultVO.setOriginId(origin.getId());
//            resultVO.setUserId(userId);
//            resultVO.setUsername(user.getName());
//            resultVO.setMarkContent(str.toString());
//            resultVO.setMarkDate(userLabelResults.get(0).getMarkDate());
//            labelResultVOS.add(resultVO);
//        }
//        proofreading.setLabelResults(labelResultVOS);
//        return proofreading;
//    }

    @Autowired
    private ISysRoleService sysRoleServiceImpl;

    @Override
    public Boolean save(LabelResultDTO labelResultDTO, Long userId){//保存
        Origin origin = originServiceImpl.getById(labelResultDTO.getOriginId());
        Integer status = origin.getStatus();
        List<LabelResult> newResults = toLabelResults(labelResultDTO,userId);

        if(status==OriginStatus.MARK_TWICE.getData()){
            origin.setStatus(OriginStatus.MARK_THIRD.getData());
            /**
             * 被注释
             */
            //esServiceImpl.updateStatusByEs(origin.getId(),OriginStatus.MARK_THIRD.getData());
        }

        if(status==OriginStatus.MARK_ONCE.getData()){
            if(proofreading(origin.getId(), newResults)){
                origin.setStatus(OriginStatus.PROOFREAD_SUCCESS.getData());
                //esServiceImpl.updateStatusByEs(origin.getId(),OriginStatus.PROOFREAD_SUCCESS.getData());
            }else {
                origin.setStatus(OriginStatus.MARK_TWICE.getData());
                //esServiceImpl.updateStatusByEs(origin.getId(),OriginStatus.MARK_TWICE.getData());
            }
        }

        if(status==OriginStatus.UNLABELED.getData()){
            origin.setStatus(OriginStatus.MARK_ONCE.getData());
            //esServiceImpl.updateStatusByEs(origin.getId(),OriginStatus.MARK_ONCE.getData());
        }

        if(originServiceImpl.updateById(origin)){
            System.out.println("\n" + new Date() + ": origin_id = "+origin.getId()+" ,status update success! From "+status+" changed to "+origin.getStatus()+" .");
            try {
                this.saveBatch(newResults);
            } catch (Exception e) {
                System.out.println("\n" + new Date() + ": origin_id = "+origin.getId()+" ,save failed...");
            }
        } else {
            System.out.println("\n" + new Date() + ": origin_id = "+origin.getId()+" ,status update failed... Status :"+status+",label_result didn't save.");
        }


        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
        String originId = (String) redisTemplate.opsForList().leftPop(user.getLanguage());
        redisTemplate.opsForValue().set(userId+"_mission", originId);

        System.out.println(new Date()+": getting new sentence for " + userId + " , origin_id = "+ originId + " .");

        return true;
    }

    @Override
    public Map<String,Object> getOriginListByRole(long current, long size, User user){//分页返回
        List<Role> roles = sysRoleServiceImpl.selectRoleByUserId(user.getId());
        boolean listStatus = false;
        for(Role role : roles){//判断角色
            if(StringUtils.equals("标注人员",role.getName())){
                listStatus = true;
                break;
            }else if(StringUtils.equals("专家",role.getName())){
                listStatus = true;
                break;
            }
        }
        Page<LabelResult> page = new Page<>(current,size);//定义了一个单页20的page

        if(listStatus){//普通标注人员
            QueryWrapper<LabelResult> queryWrapper = new QueryWrapper<LabelResult>().orderByDesc("mark_date").groupBy("origin_id");
            queryWrapper.eq("user_id",user.getId());
            IPage<LabelResult> labelResultIPage = this.page(page,queryWrapper);
            List<LabelResult> labelResults = labelResultIPage.getRecords();
            List<Integer> originIds = labelResults.stream().map(LabelResult::getOriginId).collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map.put("originIds",originIds);
            map.put("total",page.getTotal());
            return map;
        }else{//专家
            //选择状态为3的句子
            Page<Origin> originPage = new Page<>(current,size);
            QueryWrapper<Origin> queryWrapper = new QueryWrapper<Origin>().eq("status",3);
            IPage<Origin> originIPage = originServiceImpl.page(originPage,queryWrapper);
            List<Origin> origins = originIPage.getRecords();
            List<Integer> originIds = origins.stream().map(Origin::getId).collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map.put("originIds", originIds);
            map.put("total", originPage.getTotal());
            return map;
        }
    }


    @Override
    public Boolean update(LabelResultDTO labelResultDTO, Long userId){
        List<Role> roles = sysRoleServiceImpl.selectRoleByUserId(userId);
        for(Role role : roles){
            if(StringUtils.equals("专家",role.getName())){//先删后加
                Origin origin = originServiceImpl.getById(labelResultDTO.getOriginId());
                this.remove(new QueryWrapper<LabelResult>().eq("origin_id",labelResultDTO.getOriginId()));//删掉原本的句子
                List<LabelResult> newResults = toLabelResults(labelResultDTO,userId);
                origin.setStatus(OriginStatus.MARK_fourth.getData());//句子状态改为4
                /**
                 * 被注释
                 */
                //esServiceImpl.updateStatusByEs(origin.getId(),OriginStatus.MARK_fourth.getData());
                originServiceImpl.updateById(origin);
                return this.saveBatch(newResults);
            }
        }
        //先删后加
        this.remove(new QueryWrapper<LabelResult>().eq("origin_id",labelResultDTO.getOriginId()).eq("user_id",userId));
        List<LabelResult> newResults = toLabelResults(labelResultDTO,userId);
        return this.saveBatch(newResults);
    }


    @Override
    public List<LabelResultVO> getLabelResultByOriginId (Integer originId){
        List<LabelResultVO> labelResultVOS = new ArrayList<>();
        List<LabelResult> resultList = this.baseMapper.selectList(
                new QueryWrapper<LabelResult>().eq("origin_id",originId)
        );
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        Set set = redisTemplate.opsForSet().members("vo"+originId);
        if (!set.isEmpty()) {
            Map<Integer, List<LabelResult>> resultMap = resultList.stream().collect(Collectors.groupingBy(LabelResult::getUserId));
            for (Integer key : resultMap.keySet()) {
                List<LabelResult> labelResults = resultMap.get(key);
                User user = userServiceImpl.getById(key);
                LabelResultVO labelResultVO = new LabelResultVO();
                labelResultVO.setUserId(key);
                labelResultVO.setUsername(user.getName());
                labelResultVO.setMarkDate(labelResults.get(0).getMarkDate());
                labelResultVO.setOriginId(originId);
                labelResultVO.setMarkContent(String.valueOf(set.iterator().next()));
                labelResultVOS.add(labelResultVO);
            }
        }
        return labelResultVOS;
    }

    @Override
    public List<LabelResultVO> getSearchPageLabelResultByOriginId (Integer originId){
        List<LabelResultVO> labelResultVOS = new ArrayList<>();
        List<LabelResult> resultList = this.baseMapper.selectList(
                new QueryWrapper<LabelResult>().eq("origin_id",originId)
        );
        Map<Integer,List<LabelResult>> resultMap = resultList.stream().collect(Collectors.groupingBy(LabelResult::getUserId));
        for(Integer key : resultMap.keySet()){
            List<LabelResult> labelResults = resultMap.get(key);
            User user = userServiceImpl.getById(key);
            LabelResultVO labelResultVO = new LabelResultVO();
            labelResultVO.setUserId(key);
            labelResultVO.setUsername(user.getName());
            labelResultVO.setMarkDate(labelResults.get(0).getMarkDate());
            labelResultVO.setOriginId(originId);
            labelResults.sort((LabelResult result1,LabelResult result2)->result1.getLocation().compareTo(result2.getLocation()));
            final StringBuilder str = new StringBuilder();
            labelResults.forEach(labelResult -> {
                String encodeWord = labelResult.getWord();
                str.append(encodeWord+"/"+labelResult.getPartOfSpeech()+" ");
            });
            labelResultVO.setMarkContent(str.toString());
            labelResultVOS.add(labelResultVO);
        }
//        if (labelResultVOS.size()>1){
//            Map<Integer, List<String>> map = findDiffStr(labelResultVOS.get(0).getMarkContent()
//                    , labelResultVOS.get(1).getMarkContent());
//            labelResultVOS.get(0).setDiffString(map.get(0));
//            labelResultVOS.get(1).setDiffString(map.get(1));
//        }
        return labelResultVOS;
    }



//    @Override
//    public List<LabelResultVO> getLabelResultByOriginId (Integer originId){
//        List<LabelResultVO> labelResultVOS = new ArrayList<>();
//        Set set = redisTemplate.opsForSet().members("vo"+originId);
//        while(!set.isEmpty()){
//            LabelResultVO labelResultVO = new LabelResultVO();
//            labelResultVO.setMarkContent(String.valueOf(set.iterator().next()));
//            labelResultVOS.add(labelResultVO);
//        }
//        return labelResultVOS;
//    }


    private Map<Integer, List<String>> findDiffStr(String str1, String str2) {

        Map<Integer, List<String>> map = new HashMap<>();

        try{
            String originStr = str1;
            String[] spiltStr1 = str1.split(" "), spiltStr2 = str2.split(" ");
            int i = spiltStr1.length, j = spiltStr2.length;
            if ( i < j ) {
                int ans = i; i = j; j = ans;
                spiltStr1 = str2.split(" "); spiltStr2 = str1.split(" ");
            }

            for ( int m = 0 ; m < i; m++) {
                for ( int n = 0 ; n < j; n++) {
                    String diff1 = spiltStr1[m], diff2 = spiltStr2[n];
                    if (diff1.equals(diff2)) {
                        str1 = str1.replaceFirst(diff1+" ","");
                        str2 = str2.replaceFirst(diff1+" ","");
                        if (m == i-1){ str1 = str1.replaceFirst(diff1,""); }
                        if (n == j-1){ str2 = str2.replaceFirst(diff1,""); }
                        break;
                    }
                }
            }

            map.put(0, Arrays.asList(str1.split(" ")));

            for (int p = 0; p < map.get(0).size(); p++){
                if (!(originStr.contains(map.get(0).get(p)))){
                    map.put(1, Arrays.asList(str1.split(" ")));
                    map.put(0, Arrays.asList(str2.split(" ")));
                    return map;
                }
            }

            map.put(0, Arrays.asList(str1.split(" ")));
            map.put(1, Arrays.asList(str2.split(" ")));
        } catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    //未使用
//    @Override
//    public Boolean saveProofreading(LabelResultDTO labelResultDTO, Long userId){
//
//        Origin origin = originServiceImpl.getById(labelResultDTO.getOriginId());
//        //修改原句状态
//        origin.setStatus(OriginStatus.AFTER_PROOFREADING.getData());
//        List<LabelResult> newResults = toLabelResults(labelResultDTO,userId);
//        this.saveBatch(newResults);
//        labelResultSearchServiceImpl.addAndUpdateIndex(newResults);
//        return originServiceImpl.updateById(origin);
//        //存入到数据库中
//    }

    @Override
    public HashMap<String,Integer> count(long userId){
        HashMap<String,Integer> counts = new HashMap<>(3);
        List<LabelResult> totalResults = this.baseMapper.countTotal(userId);
        List<LabelResult> weekResults = this.baseMapper.countWeek(userId);
        List<LabelResult> dailyResults = this.baseMapper.countDaily(userId);
        counts.put("total",totalResults.size());
        counts.put("week",weekResults.size());
        counts.put("daily",dailyResults.size());
        return counts;
    }

    private ArrayList<LabelResult> toLabelResults(LabelResultDTO labelResultDTO,Long userId){
        ArrayList<LabelResult> newResults = new ArrayList<>();
        for(LabelResultDTO.Word word : labelResultDTO.getWords()){
            LabelResult labelResult = new LabelResult();
            labelResult.setWord(word.getWord());
            labelResult.setPartOfSpeech(word.getPartOfSpeech());
            labelResult.setLocation(word.getLocation());
            labelResult.setOriginId(labelResultDTO.getOriginId());
            labelResult.setUserId(userId.intValue());
            labelResult.setMarkDate(new Date());
            newResults.add(labelResult);
        }
        return newResults;
    }
}
