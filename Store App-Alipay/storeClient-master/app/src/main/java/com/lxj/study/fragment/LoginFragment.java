package com.lxj.study.fragment;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lxj.study.R;
import com.lxj.study.activity.MainActivity;
import com.lxj.study.common.broadcast.CommonEvent;
import com.lxj.study.base.App;
import com.lxj.study.base.AppConst;
import com.lxj.study.base.BaseFragment;
import com.lxj.study.helper.JsonCallback;
import com.lxj.study.helper.ResponseCode;
import com.lxj.study.helper.ResponseData;
import com.lxj.study.model.User;
import com.lxj.study.util.T;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.inflate;

/**
 * user：LXJ
 * desc：登录
 */

public class LoginFragment extends BaseFragment {

    @Bind(R.id.et_user_name)
    EditText mEtUserName;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.tv_login)
    TextView mTvLogin;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_login, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        final String userName = mEtUserName.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            T.showShort(getContext(), "请输入你的账号");
            return;
        } else if (TextUtils.isEmpty(password)) {
            T.showShort(getContext(), "请输入你的密码");
            return;
        } else {
            showWaitingDialog("正在登录...");
            OkGo.<ResponseData<User>>post(AppConst.User.login)
                    .params("username",userName)
                    .params("password",password)
                    .execute(new JsonCallback<ResponseData<User>>() {
                        @Override
                        public void onSuccess(Response<ResponseData<User>> response) {
                            ResponseData<User> data = response.body();
                            if (data.getStatus() == ResponseCode.SUCCESS.getCode()){
                                T.showShort(getContext(),"登录成功");
                                App.setUserInfo(data.getData());
                                App.setOkGoToken(data.getMsg());
                                EventBus.getDefault().post(new CommonEvent("登录状态改变"));
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            }else{
                                T.showShort(getContext(),"登录失败");
                            }

                        }

                        @Override
                        public void onError(Response<ResponseData<User>> response) {
                            T.showShort(response.getException().toString());
                        }

                        @Override
                        public void onFinish() {
                            hideWaitingDialog();
                        }
                    });


        }
    }
}
