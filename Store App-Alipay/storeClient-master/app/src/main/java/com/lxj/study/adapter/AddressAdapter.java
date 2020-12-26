package com.lxj.study.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.study.R;
import com.lxj.study.model.pojo.ShippingBean;

import java.util.List;

/**
 * 收获地址列表
 */
public class AddressAdapter extends BaseQuickAdapter<ShippingBean,BaseViewHolder> {


    public AddressAdapter(@Nullable List<ShippingBean> data) {
        super(R.layout.item_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShippingBean item) {

        helper.setText(R.id.tv_shipping_name,"收货人:"+item.getReceiverName());
        helper.setText(R.id.tv_shipping_phone,"电话:"+item.getReceiverPhone());
        helper.setText(R.id.tv_shipping_address,"收货地址:"+item.getReceiverAddress());

    }


}
