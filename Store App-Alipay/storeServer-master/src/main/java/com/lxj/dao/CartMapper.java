package com.lxj.dao;

import com.lxj.pojo.Cart;
import com.lxj.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper extends MyMapper<Cart> {

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId")Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productIdList")List<String> productIdList);


    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId")Integer productId,@Param("checked") Integer checked);

    int selectCartProductCount(@Param("userId") Integer userId);


    List<Cart> selectCheckedCartByUserId(Integer userId);



}