package com.lxj.study.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lxj.study.R;
import com.lxj.study.adapter.FragPagerAdapter;
import com.lxj.study.fragment.LoginFragment;
import com.lxj.study.fragment.RegisterFragment;
import com.lxj.study.widget.IconTextView;
import com.lxj.study.base.BaseActivity;
import com.lxj.study.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * user：LXJ
 * desc：登录界面
 */

public class LoginRegisterActivity extends BaseActivity {

    @Bind(R.id.ic_return)
    IconTextView mIcReturn;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        initView();

    }

    private void initView() {
        mFragments.add(LoginFragment.newInstance());
        mFragments.add(RegisterFragment.newInstance());
        mViewPager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(), mFragments));
        mViewPager.setCurrentItem(0, false);
        setTabUI(mTvLogin);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    mViewPager.setCurrentItem(0);
                    setTabUI(mTvLogin);
                }else if (position ==1){
                    mViewPager.setCurrentItem(1);
                    setTabUI(mTvRegister);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({R.id.ic_return, R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_return:
                finish();
                break;
            case R.id.tv_login:
                mViewPager.setCurrentItem(0);
                setTabUI(mTvLogin);
                break;
            case R.id.tv_register:
                mViewPager.setCurrentItem(1);
                setTabUI(mTvRegister);
                break;
        }
    }

    private void setTabUI(TextView tvLogin) {
        mTvLogin.setTextColor(UIUtil.getColor(R.color.main));
        mTvRegister.setTextColor(UIUtil.getColor(R.color.main));
        mTvLogin.setBackgroundResource(R.drawable.round_rect_gray);
        mTvRegister.setBackgroundResource(R.drawable.round_rect_gray);

        tvLogin.setTextColor(UIUtil.getColor(R.color.white));
        tvLogin.setBackgroundResource(R.drawable.round_rect_main);

    }
}
