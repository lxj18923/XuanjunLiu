import React, { PureComponent, Fragment } from "react";
import { connect } from "dva";
import { formatMessage, FormattedMessage } from 'umi/locale';

import {
  Card,
  Form,
  Input,
  Button,
  Modal,
  message,
  Table, TreeSelect, Menu, Dropdown, Icon, Tree, Select, Steps, Divider
} from "antd";
import PageHeaderWrapper from "@/components/PageHeaderWrapper";

import styles from "./role.less";

const FormItem = Form.Item;
const Option = Select.Option;
const CreateForm = Form.create()(props => {
  const { modalVisible, form, handleAdd, handleModalVisible, permission } = props;
  let permissionTemp = permission;
  permissionTemp.push(
    {
      "id": 0,
      "value": 0,
      "title": "顶层资源"
    }
  );
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      console.log(fieldsValue);
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      destroyOnClose
      title={formatMessage({id: 'app.form.create.title'})}
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.superior'})}>
        {form.getFieldDecorator("parentId", {
          initialValue: "0"
        })(
          <TreeSelect
            style={{ width: '100%' }}
            dropdownStyle={{ maxHeight: 400, overflow: "auto" }}
            treeData={permissionTemp}
            placeholder="请选择上级资源"
          />
        )
        }
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.resourceName'})}>
        {form.getFieldDecorator("resName", {
          roles: [{ required: true, message: "请输入至少四个字符的资源名称！", min: 4 }]
        })(<Input placeholder="请输入"/>)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.type'})}>
        {form.getFieldDecorator("resType", {
          initialValue: "url"
        })(
          <Select style={{ width: '100%' }}>
            <Option value="url">url</Option>
            <Option value="data">data</Option>
          </Select>
        )
        }
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.authorityValue'})}>
        {form.getFieldDecorator("permission", {
          roles: [{ required: true, message: "请输入至少四个字符的权限值！", min: 4 }]
        })(<Input placeholder="请输入"/>)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.url'})}>
        {form.getFieldDecorator("url", {
          roles: [{ required: true, message: "请输入至少四个字符的接口！", min: 4 }]
        })(<Input placeholder="请输入"/>)}
      </FormItem>
      <FormItem labelCol={{ span: 6 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.requestMethod'})}>
        {form.getFieldDecorator("requestMethod", {
          initialValue: "GET"
        })(
          <Select style={{ width: '100%' }}>
            <Option value="GET">GET</Option>
            <Option value="POST">POST</Option>
            <Option value="PUT">PUT</Option>
            <Option value="DELETE">DELETE</Option>
          </Select>
        )
        }
      </FormItem>
    </Modal>
  );
});

/**
 * 更新表单
 */
@Form.create()
class UpdateForm extends PureComponent {
  constructor(props) {
    super(props);
  }

  renderContent = () => {
    const { form, values, permission } = this.props;
    return [
      <FormItem key='id'>
        {form.getFieldDecorator("id", {
          initialValue: values.id
        })(
          <Input type="hidden"/>
        )
        }
      </FormItem>,
      <FormItem key='parentId' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.superior'})}>
        {form.getFieldDecorator("parentId", {
          initialValue: values.parentId
        })(
          <TreeSelect
            style={{ width: '100%' }}
            dropdownStyle={{ maxHeight: 400, overflow: "auto" }}
            treeData={permission}
            placeholder="请选择上级资源"
          />
        )
        }
      </FormItem>,
      <FormItem key='resName' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.resourceName'})}>
        {form.getFieldDecorator("resName", {
          roles: [{ required: true, message: "请输入至少四个字符的资源名称！", min: 4 }],
          initialValue: values.resName
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='resType' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.type'})}>
        {form.getFieldDecorator("resType", {
          initialValue: values.resType
        })(
          <Select style={{ width: '100%' }}>
            <Option value="url">url</Option>
            <Option value="data">data</Option>
          </Select>
        )
        }
      </FormItem>,
      <FormItem key='permission' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.authorityValue'})}>
        {form.getFieldDecorator("permission", {
          roles: [{ required: true, message: "请输入至少四个字符的权限值！", min: 4 }],
          initialValue: values.permission
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem key='url' labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.url'})}>
        {form.getFieldDecorator("url", {
          roles: [{ required: true, message: "请输入至少四个字符的接口！", min: 4 }],
          initialValue: values.url
        })(<Input placeholder="请输入"/>)}
      </FormItem>,
      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label={formatMessage({id: 'app.permission.form.requestMethod'})}>
        {form.getFieldDecorator("requestMethod", {
          initialValue: values.requestMethod
        })(
          <Select style={{ width: '100%' }}>
            <Option value="GET">GET</Option>
            <Option value="POST">POST</Option>
            <Option value="PUT">PUT</Option>
            <Option value="DELETE">DELETE</Option>
          </Select>
        )
        }
      </FormItem>
    ];
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
        bodyStyle={{ padding: "32px 40px 48px" }}
        destroyOnClose
        title={formatMessage({id:'app.permission.form.operation.deploy.title'})}
        visible={updateModalVisible}
        onOk={okHandle}
        onCancel={() => handleUpdateModalVisible()}
      >
        {this.renderContent()}
      </Modal>
    );
  }
}

/* eslint react/no-multi-comp:0 */
@connect(({ permission, loading }) => ({
  permission,
  loading: loading.models.permission
}))
@Form.create()
class PermissionManagement extends PureComponent {
  state = {
    modalVisible: false,
    updateModalVisible: false,
    expandForm: false,
    selectedRows: [],
    formValues: {},
    stepFormValues: {}
  };

  componentDidMount() {
    this.handleFetch();
  }

  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag
    });
  };

  handleUpdateModalVisible = (flag, record) => {
    this.setState({
      updateModalVisible: !!flag,
      stepFormValues: record || {}
    });
  };

  handleFetch = () => {
    const { dispatch } = this.props;
    dispatch({
      type: "permission/getPermissionTree"
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
      type: "permission/addPermission",
      payload: {
        ...fields
      }
    });

    message.success(formatMessage({id: 'app.form.create.successMessage'}));
    this.handleModalVisible();
    this.handleReloadData();
  };

  handleUpdate = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: "permission/updatePermission",
      payload: {
        ...fields
      }
    });
    message.success(formatMessage({id: 'app.form.operation.deploy.success'}));
    this.handleUpdateModalVisible();
    this.handleReloadData();
  };

  handleDelete = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: "permission/deletePermission",
      payload: fields.id
    });
    message.success(formatMessage({id: 'app.form.operation.delete.success'}));
    this.handleReloadData();
  };

  render() {

    const { modalVisible, updateModalVisible, stepFormValues } = this.state;

    const { permission } = this.props;
    const permissions = permission.permission.result;
    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible
    };
    const updateMethods = {
      handleUpdateModalVisible: this.handleUpdateModalVisible,
      handleUpdate: this.handleUpdate
    };

    const columns = [{
      title: formatMessage({id: 'app.permission.form.resourceName'}),
      dataIndex: "resName",
      key: "name"
    }, {
      title: formatMessage({id: 'app.permission.form.type'}),
      dataIndex: "resType",
      key: "resType",
      width: "12%"
    }, {
      title:formatMessage({id: 'app.permission.form.authorityValue'}),
      dataIndex: "permission",
      width: "15%",
      key: "permission"
    }, {
      title: formatMessage({id: 'app.permission.form.url'}),
      dataIndex: "url",
      width: "15%",
      key: "url"
    }, {
      title: formatMessage({id: 'app.permission.form.requestMethod'}),
      dataIndex: "requestMethod",
      width: "12%",
      key: "requestMethod"
    }, {
      title: formatMessage({id: 'app.form.operation'}),
      key: "operation",
      render: (record) => (
        <span className="table-operation">
          <a onClick={() => this.handleUpdateModalVisible(true, record)}>{formatMessage({id: 'app.form.operation.deploy'})}</a>
          <Divider type="vertical"/>
          <a onClick={() => this.handleDelete(record)}>{formatMessage({id: 'app.form.delete'})}</a>
        </span>
      )
    }];

    const data = [];
    for (let i in permissions) {
      let types = permissions[i];
      for (let j in types) {
        data.push(types[j]);
      }
    }

    return (
      <PageHeaderWrapper title={formatMessage({id: 'app.permission'})}>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                {formatMessage({id: 'app.form.create'})}
              </Button>
            </div>
            <Table columns={columns} dataSource={data} rowKey='id'/>,
          </div>
        </Card>
        <CreateForm {...parentMethods} modalVisible={modalVisible} permission={data}/>
        {stepFormValues && Object.keys(stepFormValues).length ? (
          <UpdateForm
            {...updateMethods}
            updateModalVisible={updateModalVisible}
            values={stepFormValues}
            permission={data}
          />
        ) : null}
      </PageHeaderWrapper>
    );
  }
}

export default PermissionManagement;
