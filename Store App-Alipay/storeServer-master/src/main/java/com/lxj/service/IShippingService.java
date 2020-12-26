package com.lxj.service;

import com.github.pagehelper.PageInfo;
import com.lxj.common.ServerResponse;
import com.lxj.pojo.Shipping;

/**
 * Created by LXJ
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> del(Integer userId, Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
