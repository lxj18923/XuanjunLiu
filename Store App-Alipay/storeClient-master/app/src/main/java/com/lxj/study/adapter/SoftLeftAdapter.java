package com.lxj.study.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.study.R;
import com.lxj.study.model.pojo.CategoryBean;
import com.lxj.study.util.UIUtil;

import java.util.List;

public class SoftLeftAdapter extends BaseQuickAdapter<CategoryBean,BaseViewHolder> {


    public SoftLeftAdapter(@Nullable List<CategoryBean> data) {
        super(R.layout.item_soft_left, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.getName());

        if (item.isSelect()){
            tvName.setTextColor(UIUtil.getColor(R.color.main));
            tvName.setBackgroundColor(UIUtil.getColor(R.color.tag_sel_color));
        }else{
            tvName.setTextColor(UIUtil.getColor(R.color.black));
            tvName.setBackgroundColor(UIUtil.getColor(R.color.white));
        }


    }




}
