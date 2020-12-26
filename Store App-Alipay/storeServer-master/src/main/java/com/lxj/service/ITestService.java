package com.lxj.service;

import com.github.pagehelper.PageInfo;
import com.lxj.common.ServerResponse;
import com.lxj.pojo.User;

import java.util.List;

/**
 * 测试例子专用
 */
public interface ITestService {

    /**
     * 测试pager分页功能
     * @param pageNum 第几页  从1开始
     * @param pageSize 一页返回多少条数据
     * @return
     */
    ServerResponse<PageInfo> testPageHelp(int pageNum, int pageSize);


    ServerResponse<List<User>> testRedisCache();
}
