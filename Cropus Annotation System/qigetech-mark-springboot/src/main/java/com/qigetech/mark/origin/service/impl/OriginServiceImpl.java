package com.qigetech.mark.origin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.OriginStatus;
import com.qigetech.mark.origin.entity.OriginUser;
import com.qigetech.mark.origin.entity.vo.OriginLabelVO;
import com.qigetech.mark.origin.mapper.OriginMapper;
import com.qigetech.mark.origin.mapper.OriginUserMapper;
import com.qigetech.mark.origin.service.IEsOriginService;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.entity.user.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-04-23
 */
@Service
public class OriginServiceImpl extends ServiceImpl<OriginMapper, Origin> implements IOriginService {

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Resource
    private OriginUserMapper originUserMapper;

    @Resource
    private OriginMapper originMapper;

    @Autowired
    private IEsOriginService esServiceImpl;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private LabelResultMapper labelResultMapper;

    @Scheduled(cron = "0 0 4 ? * *")
    public void initOriginRedis() {
        System.out.println(new Date() + ": initOriginRedis start!");
        long start = System.currentTimeMillis();
        try {
            String[] languages = {"简体", "繁体"};
            int full = 5000;
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
                        redisTemplate.opsForSet().add("vo"+originId, str.toString());
                    }
                }
                if (size != full){
                    redisTemplate.opsForList().rightPushAll(lan, list2);
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

    /**update：ZZY date：2020.11.14  */
    @Override
    public Origin getOrigin(Long userId, String language){
        try {
            String originId = redisTemplate.opsForValue().get(userId + "_mission");
            if (originId == null){
                originId = redisTemplate.opsForList().leftPop(language);
                redisTemplate.opsForValue().set(userId + "_mission", originId);
            }
            return this.getById(originId);
        } catch (RuntimeException e){
            e.printStackTrace();
            return getOriginByNoCheck(language);
        }
    }

    /**author：LXJ date：2020.2.8  */
    /**update：ZZY date：2020.11.14  */
    //由于选取数据算法的改变，这里的跳过操作，需要包含一次getOriginBycheck的操作
    @Override
    public boolean skip(Long userId,String language){
        try {
            redisTemplate.opsForList().rightPush(language, redisTemplate.opsForValue().get(userId + "_mission"));
            redisTemplate.opsForValue().set(userId+"_mission", redisTemplate.opsForList().leftPop(language));
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**author:LXJ date:2020/2/2 */
    @Override
    public Origin getOriginByCheck(Long userId,String language){
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

    @Override
    public Origin getOriginByNoCheck(String language){
        Origin origin = originMapper.getOriginWithoutCheck(language, 2);
        if (origin == null){
            origin = originMapper.getOriginWithoutCheck(language, 1);
            if (origin == null){
                origin = originMapper.getOriginWithoutCheck(language, 0);
                System.out.println("---！！！语料库库存为空！！！---");
                return origin;
            }
            return origin;
        }
        return origin;
    }

    @Override
    public Page<OriginLabelVO> getOriginLabelPage(long current, long size, User user){
        Page<OriginLabelVO> originLabelVOPage = new Page<>(current,size);
        List<OriginLabelVO> originLabelVOS = new ArrayList<>();
        List<LabelResultVO> labelResultVOS = new ArrayList<>();
        Map<String,Object> map = labelResultServiceImpl.getOriginListByRole(current,size,user);
        List<Integer> originIds = (List<Integer>) map.get("originIds");
        long total = (long) map.get("total");
        //List<Origin> origins = this.baseMapper.selectBatchIds(originIds); //旧的id批量查询（无序）
        List<Origin> origins = this.baseMapper.findOriginByIds(originIds);  //新的id批量查询（有序）
        //根据当前页获取分词内容
        for(Origin origin:origins){
            labelResultVOS = labelResultServiceImpl.getSearchPageLabelResultByOriginId(origin.getId());
            OriginLabelVO originLabelVO = new OriginLabelVO();
            BeanUtils.copyProperties(origin,originLabelVO);
            originLabelVO.setLabelResults(labelResultVOS);
            originLabelVOS.add(originLabelVO);
        }
        originLabelVOPage.setTotal(total);
        originLabelVOPage.setRecords(originLabelVOS);
        return originLabelVOPage;
    }

    @Override
    public boolean deleteOrigin(Long userId, String language) {
        //将句子状态改为已删除
        Origin origin = this.getOrigin(userId, language);
        if (origin.getStatus() == OriginStatus.UNLABELED.getData()) {
            origin.setStatus(OriginStatus.WRONG_DELETE.getData());//改变句子状态状态
            //esServiceImpl.updateStatusByEs(origin.getId(), OriginStatus.WRONG_DELETE.getData());//更新es中origin
            this.updateById(origin);//更新数据库中origin
            originUserMapper.delete(
                    new QueryWrapper<OriginUser>().eq("user_id", userId).eq("origin_id", origin.getId()));

            try {
                redisTemplate.opsForList().rightPush(language, redisTemplate.opsForValue().get(userId + "_mission"));
                redisTemplate.opsForValue().set(userId + "_mission", redisTemplate.opsForList().leftPop(language));
            } catch (RuntimeException e){
                e.printStackTrace();
                System.out.println(new Date() + ": Delete failed where origin_id = " + origin.getArticleId() + " .");
                return  false;
            }

            return true;
        }
        return false;
    }

    @Override
    public Page<OriginLabelVO> getOriginSearchPage(long current, long size, Integer status, String source, String sentence) {
        //获取检索页面
        Page<OriginLabelVO> originLabelVOPage = new Page<>(current, size);
        List<OriginLabelVO> originLabelVOS = new ArrayList<>();
        Map<String, Object> map = esServiceImpl.findByStatusOrSource(current, size,status, source, sentence);
        List<Integer> originIds = (List<Integer>) map.get("originIds");
        System.out.println(map);
        Integer total = (Integer) map.get("total");

        try{//捕获数据库查询为空的异常
            List<Origin> origins = this.baseMapper.selectBatchIds(originIds);
            //根据当前页获取分词内容
            for (Origin origin : origins) {
                List<LabelResultVO> labelResultVOS = labelResultServiceImpl.getSearchPageLabelResultByOriginId(origin.getId());
                OriginLabelVO originLabelVO = new OriginLabelVO();
                BeanUtils.copyProperties(origin, originLabelVO);
                originLabelVO.setLabelResults(labelResultVOS);
                originLabelVOS.add(originLabelVO);
            }
        }catch(Exception e){
            System.out.println("相应语料不存在");
        }

        originLabelVOPage.setTotal(total);
        originLabelVOPage.setRecords(originLabelVOS);

        return originLabelVOPage;
    }

}
