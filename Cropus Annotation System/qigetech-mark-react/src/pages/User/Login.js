import React, { Component } from 'react';
import { connect } from 'dva';
import { formatMessage, FormattedMessage } from 'umi/locale';
import Link from 'umi/link';
import { Checkbox, Alert, Icon } from 'antd';
import Login from '@/components/Login';
import styles from './Login.less';
import Input from "antd/es/input";

const { Tab, UserName, Password, Mobile, Captcha, Submit } = Login;

@connect(({ login, loading }) => ({
  login,
  submitting: loading.effects['login/login'],
}))
class LoginPage extends Component {
  state = {
    type: 'account',
    autoLogin: true,
  };

  onTabChange = type => {
    this.setState({ type });
  };

  handleSubmit = (err, values) => {
    loading:false;

    let formData = new FormData();
    formData.append("username",values.username);
    formData.append("password",values.password);
    if (!err) {
      const { dispatch } = this.props;
      dispatch({
        type: 'login/login',
        payload: formData,
      });
    }
  };

  changeAutoLogin = e => {
    this.setState({
      autoLogin: e.target.checked,
    });
  };

  renderMessage = content => (
    <Alert style={{ marginBottom: 24 }} message={content} type="error" showIcon />
  );

  render() {
    const { login, submitting } = this.props;
    const { type, autoLogin } = this.state;
    return (
      <div className={styles.main}>
        <Login
          defaultActiveKey={type}
          onTabChange={this.onTabChange}
          onSubmit={this.handleSubmit}
          ref={form => {
            this.loginForm = form;
          }}
        >
          <Tab key="account" tab={formatMessage({ id: 'app.login.tab-login-credentials' })}>
            {login.status === 'error' &&
              login.type === 'account' &&
              !submitting &&
              !state.loading.models.Verify &&
              this.renderMessage(formatMessage({ id: 'app.login.message-invalid-credentials' }))}
            <UserName name="username" placeholder="用户名" />
            <Password
              name="password"
              placeholder="密码"
            />
          </Tab>
          <Submit loading={submitting}>
            <FormattedMessage id="app.login.login" />
          </Submit>
        </Login>
      </div>
    );
  }
}

export default LoginPage;
