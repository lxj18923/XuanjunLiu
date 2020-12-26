package com.lxj.study.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.study.R;
import com.lxj.study.model.pojo.ShoppingCardBean;
import com.lxj.study.util.ImageLoaderManager;
import com.lxj.study.util.T;
import com.lxj.study.util.UIUtil;
import com.lxj.study.widget.IconTextView;
import com.lxj.study.widget.NumberButton;

import java.util.List;

/**
 * 购物车列表
 */
public class CarAdapter extends BaseQuickAdapter<ShoppingCardBean, BaseViewHolder> {

    private NumberChangeInterface numberInterface;

    public CarAdapter(@Nullable List<ShoppingCardBean> data) {
        super(R.layout.item_car, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShoppingCardBean item) {

        helper.setText(R.id.tv_title,item.getProductSubtitle());
        helper.setText(R.id.tv_price,"￥"+item.getProductPrice());
        IconTextView  itSelect = helper.getView(R.id.it_select);
        NumberButton  btnNumber = helper.getView(R.id.btn_number);
        ImageView imageView = helper.getView(R.id.iv_image);
        ImageLoaderManager.loadImage(mContext,item.getProductMainImage(),imageView);

        if (item.getProductChecked() == 1){
            itSelect.setText(UIUtil.getString(R.string.ic_select));
            itSelect.setTextColor(UIUtil.getColor(R.color.main));
        }else{
            itSelect.setText(UIUtil.getString(R.string.ic_un_select));
            itSelect.setTextColor(UIUtil.getColor(R.color.text1));
        }

        btnNumber.setBuyMax(item.getProductStock())
                .setInventory(item.getProductStock())
                .setCurrentNumber(item.getQuantity())
                .setOnWarnListener(new NumberButton.OnWarnListener() {
                    @Override
                    public void onWarningForInventory(int inventory) {
                        T.showShort("当前库存:" + inventory);
                    }

                    @Override
                    public void onWarningForBuyMax(int buyMax) {
                        T.showShort("超过最大购买数:" + buyMax);
                    }

                    @Override
                    public void onTextChanger(int number) {
                        numberInterface.onNumberChange(item,number);
                    }

                });

    }


    public interface NumberChangeInterface{
        void onNumberChange(ShoppingCardBean item,int number);
    }

    public void setCallBack(NumberChangeInterface numberInterface) {
        this.numberInterface = numberInterface;
    }

}
