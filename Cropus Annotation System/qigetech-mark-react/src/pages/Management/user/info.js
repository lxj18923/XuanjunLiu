import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { formatMessage, FormattedMessage } from 'umi/locale';
import {
  Row,
  Col,
  Card,
  Form,
  Input,
  Select,
  Icon,
  Button,
  Menu,
  Modal,
  message,
  Divider,
} from 'antd';
import StandardTable from '@/components/StandardTable';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import styles from './info.less';
import partOfSpeechJson from "../mark/partOfSpeech";
const FormItem = Form.Item;
const { Option } = Select;

/**
 * 1.可以将角色选择器拆分出做独立组件
 */
@connect(({ user, loading }) => ({
  user,
  loading: loading.models.user,
}))
@Form.create()
class CreateForm extends PureComponent {

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: "user/getRoles"
    });
  }

  renderContent = () => {
    const { form, user:{roles} } = this.props;
    let rolesTemp = {};
    if(roles!=null){
      rolesTemp = roles.result;
    }
    const roleSelect = [];
    for(let i in rolesTemp){
      roleSelect.push(<Option key={rolesTemp[i].id} value={rolesTemp[i].id}>{rolesTemp[i].name}</Option>);
    }
    return [
      <FormItem key='name' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.loginName'})}>
        {form.getFieldDecorator("name", {
          roles: [{ required: true, message: "请输入名称！", min: 11 }],
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='password' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.password'})}>
        {form.getFieldDecorator("password", {
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='stuName' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.name'})}>
        {form.getFieldDecorator("stuName", {
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='stuId' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.studentId'})}>
        {form.getFieldDecorator("stuId", {
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='major' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.major'})}>
        {form.getFieldDecorator("major", {
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
    ]
  };


  render() {
    const { modalVisible, handleModalVisible, form, handleAdd } = this.props;

    const okHandle = () => {
      form.validateFields((err, fieldsValue) => {
        if (err) return;
        form.resetFields();
        handleAdd(fieldsValue);
      });
    };

    return (
      <Modal
        width={640}
        bodyStyle={{ padding: '32px 40px 48px' }}
        destroyOnClose
        title={formatMessage({id: 'app.info.form.create.title'})}
        visible={modalVisible}
        onOk={okHandle}
        onCancel={() => handleModalVisible()}
      >
        {this.renderContent()}
      </Modal>
    );
  }
}

@connect(({ user, loading }) => ({
  user,
  loading: loading.models.user,
}))
@Form.create()
class UpdateForm extends PureComponent {

  componentDidMount() {
    const { dispatch, values } = this.props;
    dispatch({
      type: "user/getUserById",
      payload: values.id
    });
  }

  renderContent = () => {
    const { form, values} = this.props;

    return [
      <FormItem key='id'>
        {form.getFieldDecorator("id", {
          initialValue: values.id
        })(<Input type='hidden' placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='name' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.loginName'})}>
        {form.getFieldDecorator("name", {
          roles: [{ required: true, message: "请输入登录名称！", min: 11 }],
          initialValue: values.name
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='password' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.password'})}>
        {form.getFieldDecorator("password", {
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='stuName' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.name'})}>
        {form.getFieldDecorator("stuName", {
          initialValue: values.stuName
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='stuId' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.studentId'})}>
        {form.getFieldDecorator("stuId", {
          initialValue: values.stuId
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='major' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.major'})}>
        {form.getFieldDecorator("major", {
          initialValue: values.major
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='language' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.info.form.language'})}>
        {form.getFieldDecorator("language", {
          initialValue: values.language
        })(
          <Select style={{width:'100%'}}>
            <Select.Option value={"繁体"}>繁体</Select.Option>
            <Select.Option value={"简体"}>简体</Select.Option>
            <Select.Option value={"英文"}>英文</Select.Option>
          </Select>
        )}
      </FormItem>
    ]
  };

  render() {
    const { updateModalVisible, handleUpdateModalVisible, form, handleUpdate } = this.props;

    const okHandle = () => {
      form.validateFields((err, fieldsValue) => {
        if (err) return;
        form.resetFields();
        handleUpdate(fieldsValue);
      });
    };

    return (
      <Modal
        width={640}
        bodyStyle={{ padding: '32px 40px 48px' }}
        destroyOnClose
        title={formatMessage({id: 'app.info.form.operation.deploy.title'})}
        visible={updateModalVisible}
        onCancel={() => handleUpdateModalVisible()}
        onOk={okHandle}
      >
        {this.renderContent()}
      </Modal>
    );
  }
}

/* eslint react/no-multi-comp:0 */
@connect(({ user, loading }) => ({
  user,
  loading: loading.models.user,
}))
@Form.create()
class InfoManagement extends PureComponent {
  state = {
    modalVisible: false,
    updateModalVisible: false,
    expandForm: false,
    selectedRows: [],
    formValues: {},
    stepFormValues: {},
  };

  columns = [
    {
      title:formatMessage({id: 'app.info.form.userId'}),
      dataIndex: 'id',
    },
    {
      title:formatMessage({id: 'app.info.form.loginName'}),
      dataIndex: 'name',
    },
    {
      title: formatMessage({id: 'app.info.form.language'}),
      dataIndex: 'language',
    },
    {
      title: formatMessage({id: 'app.info.form.name'}),
      dataIndex: 'stuName',
    },
    {
      title: formatMessage({id: 'app.info.form.studentId'}),
      dataIndex: 'stuId',
    },
    {
      title: formatMessage({id: 'app.info.form.major'}),
      dataIndex: 'major',
    },
    {
      title: formatMessage({id: 'app.form.operation'}),
      render: (text, record) => (
        <Fragment>
          <a onClick={() => this.handleUpdateModalVisible(true, record)}>{formatMessage({id: 'app.form.operation.deploy'})}</a>
          <Divider type="vertical" />
          <a onClick={() => this.handleDelete(record)}>{formatMessage({id: 'app.form.delete'})}</a>
        </Fragment>
      ),
    },
  ];

  componentDidMount() {
    this.handleFetch();
  }

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const { dispatch } = this.props;
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
    };
    dispatch({
      type: 'user/fetch',
      payload: params,
    });
  };

  handleFormReset = () => {
    const { form, dispatch } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    dispatch({
      type: 'user/fetch',
      payload: {},
    });
  };

  toggleForm = () => {
    const { expandForm } = this.state;
    this.setState({
      expandForm: !expandForm,
    });
  };

  handleMenuClick = e => {
    const { dispatch } = this.props;
    const { selectedRows } = this.state;

    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        dispatch({
          type: 'user/remove',
          payload: {
            key: selectedRows.map(row => row.key),
          },
          callback: () => {
            this.setState({
              selectedRows: [],
            });
          },
        });
        break;
      default:
        break;
    }
  };

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  handleSearch = e => {
    e.preventDefault();

    const { dispatch, form } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const values = {
        ...fieldsValue,
        updatedAt: fieldsValue.updatedAt && fieldsValue.updatedAt.valueOf(),
      };

      this.setState({
        formValues: values,
      });

      dispatch({
        type: 'user/fetch',
        payload: values,
      });
    });
  };

  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };

  handleUpdateModalVisible = (flag, record) => {
    this.setState({
      updateModalVisible: !!flag,
      stepFormValues: record || {},
    });
  };

  handleFetch = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'user/fetch',
      payload:{}
    });
  };

  handleReloadData = ()=>{
    if (this.timer) {
      clearTimeout(this.timer);
    }
    this.timer = setTimeout(() => {
      this.handleFetch()
    }, 1000);
  };

  handleAdd = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'user/add',
      payload: fields,
    });

    message.success(formatMessage({id: 'app.form.create.successMessage'}));
    this.handleModalVisible();
    this.handleReloadData();
  };

  handleUpdate = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'user/update',
      payload: fields,
    });
    message.success(formatMessage({id: 'app.form.operation.deploy.success'}));
    this.handleUpdateModalVisible();
    this.handleReloadData();
  };

  handleDelete = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'user/delete',
      payload: fields.id,
    });
    message.success(formatMessage({id: 'app.form.operation.delete.success'}));
    this.handleReloadData();
  };


  renderSimpleForm() {
    const {
      form: { getFieldDecorator },
    } = this.props;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <FormItem label="用户名称">
              {getFieldDecorator('name')(<Input placeholder="请输入" />)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="用户类型">
              {getFieldDecorator('type')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">关闭</Option>
                  <Option value="1">运行中</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
                重置
              </Button>
              <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
                展开 <Icon type="down" />
              </a>
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  render() {
    const {
      user: { users },
      loading,
    } = this.props;
    const userList = [];
    if(users.result!=null){
      userList["list"] = users.result.records;
      userList["pagination"] = {
        current: users.result.current,
        pageSize: users.result.size,
        total: users.result.total,
      }
    }
    const { selectedRows, modalVisible, updateModalVisible, stepFormValues } = this.state;
    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove">删除</Menu.Item>
        <Menu.Item key="approval">批量审批</Menu.Item>
      </Menu>
    );

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };
    const updateMethods = {
      handleUpdateModalVisible: this.handleUpdateModalVisible,
      handleUpdate: this.handleUpdate,
    };
    return (
      <PageHeaderWrapper title={formatMessage({id: 'app.info'})}>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                {formatMessage({id: 'app.form.create'})}
              </Button>
            </div>
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={userList}
              columns={this.columns}
              rowKey='id'
              onSelectRow={this.handleSelectRows}
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>
        <CreateForm {...parentMethods} modalVisible={modalVisible} />
        {stepFormValues && Object.keys(stepFormValues).length ? (
          <UpdateForm
            {...updateMethods}
            updateModalVisible={updateModalVisible}
            values={stepFormValues}
          />
        ) : null}
      </PageHeaderWrapper>
    );
  }
}

export default InfoManagement;
