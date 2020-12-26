package com.qigetech.mark.testLXJ;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.OriginStatus;
import com.qigetech.mark.origin.mapper.OriginMapper;
import com.qigetech.mark.origin.service.IEsOriginService;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.mapper.user.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCV2 {

    @Autowired
    private LabelResultMapper labelResultMapper;

    @Resource
    private OriginMapper originMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private IEsOriginService esServiceImpl;

    @Autowired
    private IOriginService originServiceImpl;


    public Boolean proofread(int x)  {

        //按origin_id将同一个句子的标注结果选出来
        List<LabelResult> OriginsList = labelResultMapper .selectList(new QueryWrapper<LabelResult>().eq("origin_id",x));
        //第一个人标注的结果
        List<LabelResult> User_1List=new ArrayList<>();
        //第二个人标注的结果
        List<LabelResult> User_2List=new ArrayList<>();
        //将各个属性转存到变量里便于比较值的大小。若在list中调用对象直接比较则是在比较地址！！！！！！！1
        int User_1_id,User_2_id,User_1_location,User_2_location;
        String User_1_PartOfSpeech,User_2_PartOfSpeech,User_1_Word,User_2_Word;
        int c=0;
        for (int i = 0; i <= OriginsList.size()-1; i++) {
            //将句子分别按标注者1 与标注者2 分类
            User_1_id=OriginsList.get(0).getUserId();
            User_2_id=OriginsList.get(i ).getUserId();
            if (User_1_id == User_2_id) {
                User_1List.add(OriginsList.get(i ));
            }else{
                User_2List.add(OriginsList.get(i ));
            }
        }
//        System.out.println("User_1List"+User_1List);
//        System.out.println("User_2List"+User_2List);

        //分完类后再比较两个list的大小是否相同，若list大小不同则一定分词出错
        if(User_1List.size()==User_2List.size()){
            for (int i = 0; i <= User_1List.size()-1; i++) {
                User_1_PartOfSpeech=User_1List.get(i).getPartOfSpeech();
                User_2_PartOfSpeech=User_2List.get(i).getPartOfSpeech();
                User_1_Word=User_1List.get(i).getWord();
                User_2_Word=User_2List.get(i).getWord();
                User_1_location=User_1List.get(i).getLocation();
                User_2_location=User_2List.get(i).getLocation();
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

    @Test
    public void test(){
        List<Integer> list = new ArrayList<>();
        List<Origin> origins = originMapper.selectList(new QueryWrapper<Origin>().eq("status",10));
        System.out.println(origins.size());
        for (Origin origin : origins) {
            if(proofread(origin.getId())){
                origin.setStatus(OriginStatus.PROOFREAD_SUCCESS.getData());
                //esServiceImpl.updateStatusByEs(origin.getId(),OriginStatus.PROOFREAD_SUCCESS.getData());
                originServiceImpl.updateById(origin);
                System.out.println(origin.getId());
            }
        }
        System.out.println(list.size());
    }

    @Test
    public void test2() {

        java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.000");

        List<User> users = userMapper.selectList(new QueryWrapper<User>());
        for (User user : users) {
            double S5=0;
            int Sz=0;
            List<LabelResult> LabelResults = labelResultMapper.selectList(new QueryWrapper<LabelResult>().eq("user_id", user.getId()).groupBy("origin_id"));

//            List<Origin> origins = originMapper.selectBatchIds(LabelResults);
//            System.out.println("origins="+origins);
            for (LabelResult LabelResult : LabelResults) {
                Origin origin=originMapper.selectById(LabelResult.getOriginId());
//                System.out.println(" origin="+ origin);
                if(origin!=null){
                    if(origin.getStatus()==5){
                        S5++;
                    }
                    Sz++;
                }
            }

            if(Sz!=0) {
                System.out.println("用户" + user.getId() + "，共标注了=" + LabelResults.size() + "条，正确条数=" + S5 + "条，正确率为=" + df.format(S5 / LabelResults.size()));
            }else{
                System.out.println("用户" + user.getId() + "，共标注了=" + LabelResults.size() + "条，正确率为=0" );
            }


        }
    }

    @Test
    public void test3() {

        java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.000");

        List<User> users = userMapper.selectList(new QueryWrapper<User>().ge("id",2730));
        for (User user : users) {

            int all = originMapper.countCVAll(user.getId(), "简体").size();
            double success = originMapper.countCVSuccess(user.getId(), "简体").size();

            if(all!=0) {
                System.out.println("用户" + user.getId() +" "+user.getName()+ "，共标注了=" + all + "条，正确条数=" + success + "条，正确率为=" + df.format(success / all));
            }else{
                System.out.println("用户" + user.getId() +" "+user.getName()+ "，共标注了=" + all + "条，正确率为=0" );
            }


        }
    }
}







