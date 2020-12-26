package com.lxj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxj.common.ServerResponse;
import com.lxj.dao.ShippingMapper;
import com.lxj.pojo.Shipping;
import com.lxj.service.IShippingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by LXJ
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {


    @Resource
    private ShippingMapper shippingMapper;

    //添加收获地址
    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        //无地址则设置为默认
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        if (shippings ==null || shippings.size()<=0){
            shipping.setIsDefault(true);
        }
        shipping.setCreateTime(new Date());
        shipping.setUpdateTime(new Date());
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("新建地址成功",shipping.getId());
        }

        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    //删除地址
    public ServerResponse<String> del(Integer userId, Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }


    //更新地址
    public ServerResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        shipping.setUpdateTime(new Date());
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    //支付的时候选择地址
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功",shipping);
    }

    //支付信息分页
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }







}
