package com.qigetech.mark.triad.service.impl;

import com.qigetech.mark.triad.service.ITriadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuxuanjun on 2019-07-21
 * Project : qigetech-mark
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TriadServiceImplTest {

    @Autowired
    private ITriadService triadServiceImpl;

    @Test
    public void getOrigin() {
        System.out.println(triadServiceImpl.getOrigin());
    }
}