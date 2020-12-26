package com.qigetech.mark;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.mapper.OriginMapper;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Component
public class TestCV {

    @Autowired
    private LabelResultMapper labelResultMapper;

    @Resource
    private OriginMapper originMapper;

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
                list.add(origin.getId());
                System.out.println(origin.getId());
            }
        }
        System.out.println(list.size());
    }
}







