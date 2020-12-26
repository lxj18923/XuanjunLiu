package com.qigetech.mark;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuxuanjun on 2019-08-14
 * Project : qigetech-mark
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OriginParserTest {

    @Resource
    private LabelResultMapper labelResultMapper;

    @Test
    public void test() throws IOException {
        List<String> texts = FileUtils.readLines(new File("C:\\Users\\jaypa\\Desktop\\验证成功数据：1365条.txt"),"UTF-8");
        String content = "";
        for(int i = 0;i<texts.size();i++){
            if(i%3 == 0){
                content += get(texts.get(i)) + "\r\n";
            }
        }
        FileUtils.writeStringToFile(new File("C:\\Users\\jaypa\\Desktop\\fenci.txt"),content,"UTF-8");
    }

    private String get(String id){
        System.out.println(id);
        List<LabelResult> resultList = labelResultMapper.selectList(
                new QueryWrapper<LabelResult>().eq("origin_id",id)
        );
        Map<Integer,List<LabelResult>> resultMap = resultList.stream().collect(Collectors.groupingBy(LabelResult::getUserId));
        final StringBuilder str = new StringBuilder();
        for(Integer key : resultMap.keySet()){
            List<LabelResult> labelResults = resultMap.get(key);

            labelResults.forEach(labelResult -> {
                String encodeWord = labelResult.getWord();
                str.append(encodeWord + " ");
            });

            break;
        }
        return str.toString();
    }

}
