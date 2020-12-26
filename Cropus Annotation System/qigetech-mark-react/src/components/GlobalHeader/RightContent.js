import React, { PureComponent } from 'react';
import { FormattedMessage, formatMessage } from 'umi/locale';
import { Spin, Tag, Menu, Icon, Dropdown, Avatar, Tooltip } from 'antd';
import moment from 'moment';
import groupBy from 'lodash/groupBy';
import NoticeIcon from '../NoticeIcon';
import HeaderSearch from '../HeaderSearch';
import SelectLang from '../SelectLang';
import styles from './index.less';
import {connect} from "dva";

@connect(({ label, loading }) => ({
  label,
  loading: loading.models.label,
}))

export default class GlobalHeaderRight extends PureComponent {

  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'label/getUserInfo',
    });
  }

  getNoticeData() {
    const { notices = [] } = this.props;
    if (notices.length === 0) {
      return {};
    }
    const newNotices = notices.map(notice => {
      const newNotice = { ...notice };
      if (newNotice.datetime) {
        newNotice.datetime = moment(notice.datetime).fromNow();
      }
      if (newNotice.id) {
        newNotice.key = newNotice.id;
      }
      if (newNotice.extra && newNotice.status) {
        const color = {
          todo: '',
          processing: 'blue',
          urgent: 'red',
          doing: 'gold',
        }[newNotice.status];
        newNotice.extra = (
          <Tag color={color} style={{ marginRight: 0 }}>
            {newNotice.extra}
          </Tag>
        );
      }
      return newNotice;
    });
    return groupBy(newNotices, 'type');
  }

  render() {
    const {
      currentUser,
      fetchingNotices,
      onNoticeVisibleChange,
      onMenuClick,
      onNoticeClear,
      theme,
      label:{userInfo}
    } = this.props;

    const menu = (
      <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick}>
        {/*<Menu.Item key="userCenter">*/}
          {/*<Icon type="user" />*/}
          {/*<FormattedMessage id="menu.account.center" defaultMessage="account center" />*/}
        {/*</Menu.Item>*/}
        {/*<Menu.Item key="userinfo">*/}
          {/*<Icon type="setting" />*/}
          {/*<FormattedMessage id="menu.a ccount.settings" defaultMessage="account settings" />*/}
        {/*</Menu.Item>*/}
        {/*<Menu.Item key="triggerError">*/}
          {/*<Icon type="close-circle" />*/}
          {/*<FormattedMessage id="menu.account.trigger" defaultMessage="Trigger Error" />*/}
        {/*</Menu.Item>*/}
        {/*<Menu.Divider />*/}
        <Menu.Item key="logout">
          <Icon type="logout" />
          <FormattedMessage id="menu.account.logout" defaultMessage="logout" />
        </Menu.Item>
      </Menu>
    );
    let className = styles.right;
    if (theme === 'dark') {
      className = `${styles.right}  ${styles.dark}`;
    }
    return (
      <div className={className}>
        {
          userInfo!=null?<span className={styles.name}>{userInfo.result}</span>:''
        }
        <Dropdown overlay={menu}>
            <span className={`${styles.action} ${styles.account}`}>
              {/*<Avatar*/}
              {/*  size="small"*/}
              {/*  className={styles.avatar}*/}
              {/*  src={currentUser.avatar}*/}
              {/*  alt="avatar"*/}
              {/*/>*/}

              <span  className={styles.name}><FormattedMessage id="header.logout"  /></span>
            </span>
        </Dropdown>
        <SelectLang className={styles.action} />
      </div>
    );
  }
}
