package com.lxj.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.lxj.common.Const;
import com.lxj.common.ResponseCode;
import com.lxj.common.ServerResponse;
import com.lxj.dao.CartMapper;
import com.lxj.dao.ProductMapper;
import com.lxj.pojo.Cart;
import com.lxj.pojo.Product;
import com.lxj.service.ICartService;
import com.lxj.util.BigDecimalUtil;
import com.lxj.util.PropertiesUtil;
import com.lxj.vo.CartProductVo;
import com.lxj.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by LXJ
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductMapper productMapper;

    //往购物车重添加商品
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }


        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartItem.setCreateTime(new Date());
            cartItem.setUpdateTime(new Date());
            cartMapper.insert(cartItem);
        }else{
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    //更新购物车中的商品
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }else{
            //购物车没有该商品
        }
        cart.setUpdateTime(new Date());
        cartMapper.updateByPrimaryKey(cart);
        return this.list(userId);
    }

    //删除购物车中的商品
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        System.out.print("============="+CollectionUtils.isEmpty(productList));
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        return this.list(userId);
    }

    //获得购物车列表
    public ServerResponse<CartVo> list (Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    //选中or不选中商品
    public ServerResponse<CartVo> selectOrUnSelect (Integer userId, Integer productId, Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    //对购物车中的商品进行计数
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    //查购物车中所有的商品
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        //查出来的是所有在购物车中的商品，所以即使一个购物车中有多个商品，这多个商品都会被查出来放在cartList中，参考add()方法逻辑
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }

                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
    }

    //拿到购物车中商品的状态
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

    }


























}
