package com.lxj.study.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lxj.study.R;
import com.lxj.study.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发现更多界面
 */
public class MoreActivity extends BaseActivity {

    @Bind(R.id.it_return)
    TextView itReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

    }


    @OnClick({R.id.it_return})
    public void onViewClicked() {
        finish();
    }

}
