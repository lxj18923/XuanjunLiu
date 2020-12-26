package com.qigetech.mark;

import com.qigetech.mark.triad.entity.Triad;
import com.qigetech.mark.triad.service.ITriadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by liuxuanjun on 2019-08-25
 * Project : qigetech-mark
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TriadTest {

    @Autowired
    private ITriadService triadServiceImpl;

    @Test
    public void test() throws IOException {
        List<Triad> triads = triadServiceImpl.list();
        int length = 0;
        for(Triad triad :triads){
            String content = triad.getContent();
            length += content.split(";").length + content.split("；").length;

        }
        System.out.println(length);
    }

}
