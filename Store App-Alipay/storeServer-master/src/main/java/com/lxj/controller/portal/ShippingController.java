package com.lxj.controller.portal;

import com.github.pagehelper.PageInfo;
import com.lxj.common.ResponseCode;
import com.lxj.common.ServerResponse;
import com.lxj.pojo.Shipping;
import com.lxj.pojo.User;
import com.lxj.service.IShippingService;
import com.lxj.util.JsonUtil;
import com.lxj.util.RedisShardedPoolUtil;
import com.lxj.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by LXJ
 */

@Controller
@RequestMapping("/shipping/")
public class ShippingController {


    @Autowired
    private IShippingService iShippingService;


    @RequestMapping("add.do")
   @ResponseBody
    public ServerResponse add(HttpServletRequest httpServletRequest, Shipping shipping){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(),shipping);
    }


    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpServletRequest httpServletRequest, Integer shippingId){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpServletRequest httpServletRequest, Shipping shipping){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(),shipping);
    }


    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpServletRequest httpServletRequest, Integer shippingId){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(),shippingId);
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         HttpServletRequest httpServletRequest){
        String loginToken = TokenUtil.readLoginTokenByHeader(httpServletRequest);
        User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }



}
