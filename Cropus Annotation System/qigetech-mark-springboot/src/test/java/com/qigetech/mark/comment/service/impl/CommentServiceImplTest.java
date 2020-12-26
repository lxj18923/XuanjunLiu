package com.qigetech.mark.comment.service.impl;

import com.google.gson.Gson;
import com.qigetech.mark.comment.controller.CommentController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liuxuanjun on 2019-07-08
 * Project : qigetech-mark
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceImplTest {

    @Autowired
    private CommentController commentController;

    @Test
    public void getPage() {
        System.out.println(new Gson().toJson(commentController.getPage(0,10)));
    }
}