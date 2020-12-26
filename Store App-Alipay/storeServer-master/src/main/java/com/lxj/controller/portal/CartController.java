package com.lxj.controller.portal;

import com.lxj.common.Const;
import com.lxj.common.ResponseCode;
import com.lxj.common.ServerResponse;
import com.lxj.pojo.User;
import com.lxj.service.ICartService;
import com.lxj.util.JsonUtil;
import com.lxj.util.RedisShardedPoolUtil;
import com.lxj.util.TokenUtil;
import com.lxj.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by LXJ
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;



    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add( HttpServletRequest httpServletRequest, Integer count, Integer productId){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest, String productIds){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }


    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null, Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null, Const.Cart.UN_CHECKED);
    }



    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest, Integer productId){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest, Integer productId){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.UN_CHECKED);
    }



    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
