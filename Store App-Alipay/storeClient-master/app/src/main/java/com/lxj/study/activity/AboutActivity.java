package com.lxj.study.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lxj.study.R;
import com.lxj.study.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.it_return)
    TextView itReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }


    @OnClick({R.id.it_return})
    public void onViewClicked() {
        finish();
    }

}
