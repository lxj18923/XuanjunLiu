package com.lxj.dao;

import com.lxj.pojo.Order;
import com.lxj.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends MyMapper<Order> {



    //
    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId, @Param("orderNo")Long orderNo);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserId(Integer userId);

    List<Order> selectByUserIdAndStatus(@Param("userId") Integer userId,@Param("status") Integer status);

    List<Order> selectAllOrder();

    int confirmReceivedGoods(@Param("userId") Integer userId,@Param("orderNo")Long orderNo);

    //定时关单
    List<Order> selectOrderStatusByCreateTime(@Param("status") Integer status,@Param("date") String date);

    int closeOrderByOrderId(Integer id);


}