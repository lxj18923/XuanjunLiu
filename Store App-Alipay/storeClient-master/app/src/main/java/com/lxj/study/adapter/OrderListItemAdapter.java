package com.lxj.study.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.study.R;
import com.lxj.study.model.pojo.OrderItemVoListBean;
import com.lxj.study.util.ImageLoaderManager;

import java.util.List;

import butterknife.Bind;

public class OrderListItemAdapter extends BaseQuickAdapter<OrderItemVoListBean, BaseViewHolder> {


    @Bind(R.id.tv_num)
    TextView tvNum;

    public OrderListItemAdapter(@Nullable List<OrderItemVoListBean> data) {
        super(R.layout.item_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderItemVoListBean item) {

        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageLoaderManager.loadImage(mContext,item.getProductImage(),ivImage);

        helper.setText(R.id.tv_title,item.getProductName());
        helper.setText(R.id.tv_price,"￥"+item.getCurrentUnitPrice());
        helper.setText(R.id.tv_num,"x"+item.getQuantity());

    }


}
