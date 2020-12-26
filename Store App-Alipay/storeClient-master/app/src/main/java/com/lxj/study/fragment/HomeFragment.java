package com.lxj.study.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.study.R;
import com.lxj.study.activity.SearchActivity;
import com.lxj.study.adapter.SoftRightAdapter;
import com.lxj.study.base.AppConst;
import com.lxj.study.base.BaseFragment;
import com.lxj.study.helper.JsonCallback;
import com.lxj.study.helper.ResponseData;
import com.lxj.study.model.Vo.ProductModel;
import com.lxj.study.util.ImageLoaderManager;
import com.lxj.study.util.T;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

import static android.view.View.inflate;

/**
 * user：LXJ
 * desc：首页模块
 */

public class HomeFragment extends BaseFragment
                implements BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.rl_to_search)
    RelativeLayout rlToSearch;
    @Bind(R.id.banner)
    BGABanner banner;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private int pageSize = 10;
    private int pageNum =1;
    private SoftRightAdapter goodsAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {

        rvContent.setFocusable(false);
        rvContent.setHasFixedSize(true);
        rvContent.setNestedScrollingEnabled(false);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        goodsAdapter = new SoftRightAdapter(null);
        rvContent.setAdapter(goodsAdapter);
        goodsAdapter.setOnLoadMoreListener(this, rvContent);

        initBannerData();
        getContentData();

    }


    private void initBannerData() {

        List<String> images = new ArrayList<>();
        images.add("http://f.01ny.cn/forum/201203/07/135909uaa0a8ihhwhieen8.jpg");
        images.add("http://img.zcool.cn/community/01c5d658acfe73a801219c77b80338.jpg@900w_1l_2o_100sh.jpg");
        images.add("http://pic.rmb.bdstatic.com/c31d21815440908dd7b19e728bb59b1a7578.jpeg");
        images.add("http://t8.baidu.com/it/u=970045623,1055548165&fm=193");
        banner.setData(R.layout.item_home_banner, images, null);
        banner.setAdapter(new BGABanner.Adapter<View, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, String string, int position) {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image);
                ImageLoaderManager.loadImage(getContext(), string, imageView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate<View, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, String model, int position) {

            }
        });

    }

    @OnClick(R.id.rl_to_search)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }


    private void getContentData() {
        pageNum = 1;
        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();
                        if (model.getData() !=null){
                            goodsAdapter.setNewData(model.getData().getList());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                    }
                });

    }


    @Override
    public void onLoadMoreRequested() {
        pageNum += 10;
        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();
                        if (model.getData() !=null && model.getData().getList() !=null
                                && model.getData().getList().size() >0){
                            goodsAdapter.setNewData(model.getData().getList());
                            goodsAdapter.loadMoreComplete();
                        }else{
                            goodsAdapter.loadMoreEnd();
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                        goodsAdapter.loadMoreFail();
                    }
                });
    }
}
