import React, { PureComponent, Fragment } from "react";
import { formatMessage, FormattedMessage } from "umi/locale";
import { connect } from "dva";
import {
  Card,
  Form,
  Input,
  Select,
  Button,
  Tree,
  Modal,
  message,
  Divider,
  Steps,
  Table
} from "antd";
import StandardTable from "@/components/StandardTable";
import PageHeaderWrapper from "@/components/PageHeaderWrapper";

import styles from "./role.less";

const FormItem = Form.Item;
const { Step } = Steps;
const { TreeNode } = Tree;
const { Option } = Select;

/**
 * 新建表单
 * @type {React.ComponentClass<RcBaseFormProps & Omit<FormComponentProps, keyof FormComponentProps>>}
 */
const CreateForm = Form.create()(props => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      destroyOnClose
      title={formatMessage({ id: "app.form.create.title" })}
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label={formatMessage({ id: "app.role.form.name" })}
      >
        {form.getFieldDecorator("name", {
          roles: [
            {
              required: true,
              message: "请输入至少五个字符的角色名称！",
              min: 5
            }
          ]
        })(
          <Input placeholder={formatMessage({ id: "app.form.placeholder" })} />
        )}
      </FormItem>
    </Modal>
  );
});

/**
 * 更新表单
 */
@connect(({ role, loading }) => ({
  role,
  loading: loading.models.role
}))
@Form.create()
class UpdateForm extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      formVals: {
        name: props.values.name,
        id: props.values.id,
        permissions: []
      },
      currentStep: 0,
      checkedKeys: []
    };
    this.formLayout = {
      labelCol: { span: 7 },
      wrapperCol: { span: 13 }
    };
  }

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: "role/getById",
      payload: this.state.formVals.id
    });
    dispatch({
      type: "role/getPermissionTreeByType"
    });
  }

  handleNext = currentStep => {
    const { form, handleUpdate } = this.props;
    const { formVals: oldValue } = this.state;
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      const formVals = { ...oldValue, ...fieldsValue };
      this.setState(
        {
          formVals
        },
        () => {
          if (currentStep < 2) {
            this.forward();
          } else {
            handleUpdate(formVals);
          }
        }
      );
    });
  };

  backward = () => {
    const { currentStep } = this.state;
    this.setState({
      currentStep: currentStep - 1
    });
  };

  forward = () => {
    const { currentStep } = this.state;
    this.setState({
      currentStep: currentStep + 1
    });
  };

  renderTreeNodes = data =>
    data.map(item => {
      if (item.children !== null) {
        return (
          <TreeNode title={item.title} key={item.id} dataRef={item}>
            {this.renderTreeNodes(item.children)}
          </TreeNode>
        );
      }
      return <TreeNode title={item.title} key={item.id} dataRef={item} />;
    });

  onTreeCheck = checkedKeys => {
    const { formVals } = this.state;
    formVals["permissions"] = checkedKeys;
    this.setState({
      formVals
    });
    this.setState({ checkedKeys });
  };

  renderContent = (currentStep, formVals) => {
    const { form, role } = this.props;
    if (currentStep === 1) {
      if (this.state.checkedKeys.length === 0) {
        this.onTreeCheck(role.role.result.permission.select);
      }
      return [
        <Tree
          checkable
          onCheck={this.onTreeCheck}
          checkedKeys={this.state.checkedKeys}
          key="url"
        >
          {this.renderTreeNodes(role.permission.result.url)}
        </Tree>
      ];
    }
    if (currentStep === 2) {
      const dataList = role.permission.result.data;
      const content = [];
      for (const i in dataList) {
        content.push(
          <FormItem
            key={dataList[i].key}
            {...this.formLayout}
            label={dataList[i].title}
          >
            {form.getFieldDecorator(dataList[i].key, {
              initialValue: role.role.result.permission.data[dataList[i].key]
            })(
              <Select style={{ width: "100%" }}>
                {dataList[i].children.map(item => {
                  return (
                    <Option value={item.id} key={item.id}>
                      {item.title}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
        );
      }
      return content;
    }

    return [
      <FormItem
        key="name"
        {...this.formLayout}
        label={formatMessage({ id: "app.role.form.name" })}
      >
        {form.getFieldDecorator("name", {
          roles: [{ required: true, message: "请输入规则名称！" }],
          initialValue: formVals.name
        })(<Input placeholder="请输入" />)}
      </FormItem>
    ];
  };

  renderFooter = currentStep => {
    const { handleUpdateModalVisible } = this.props;
    if (currentStep === 1) {
      return [
        <Button key="back" style={{ float: "left" }} onClick={this.backward}>
          {formatMessage({ id: "app.modal.previous" })}
        </Button>,
        <Button key="cancel" onClick={() => handleUpdateModalVisible()}>
          {formatMessage({ id: "app.modal.cancel" })}
        </Button>,
        <Button
          key="forward"
          type="primary"
          onClick={() => this.handleNext(currentStep)}
        >
          {formatMessage({ id: "app.modal.next" })}
        </Button>
      ];
    }
    if (currentStep === 2) {
      return [
        <Button key="back" style={{ float: "left" }} onClick={this.backward}>
          {formatMessage({ id: "app.modal.previous" })}
        </Button>,
        <Button key="cancel" onClick={() => handleUpdateModalVisible()}>
          {formatMessage({ id: "app.modal.cancel" })}
        </Button>,
        <Button
          key="submit"
          type="primary"
          onClick={() => this.handleNext(currentStep)}
        >
          {formatMessage({ id: "app.modal.ok" })}
        </Button>
      ];
    }
    return [
      <Button key="cancel" onClick={() => handleUpdateModalVisible()}>
        {formatMessage({ id: "app.modal.cancel" })}
      </Button>,
      <Button
        key="forward"
        type="primary"
        onClick={() => this.handleNext(currentStep)}
      >
        {formatMessage({ id: "app.modal.next" })}
      </Button>
    ];
  };

  render() {
    const { updateModalVisible, handleUpdateModalVisible } = this.props;
    const { currentStep, formVals } = this.state;

    return (
      <Modal
        width={640}
        bodyStyle={{ padding: "32px 40px 48px" }}
        destroyOnClose
        title={formatMessage({ id: "app.role.form.operation.deploy.title" })}
        visible={updateModalVisible}
        footer={this.renderFooter(currentStep)}
        onCancel={() => handleUpdateModalVisible()}
      >
        <Steps style={{ marginBottom: 28 }} size="small" current={currentStep}>
          <Step
            title={formatMessage({ id: "app.role.form.operation.deploy.base" })}
          />
          <Step
            title={formatMessage({
              id: "app.role.form.operation.deploy.permissions"
            })}
          />
          <Step
            title={formatMessage({
              id: "app.role.form.operation.deploy.range"
            })}
          />
        </Steps>
        {this.renderContent(currentStep, formVals)}
      </Modal>
    );
  }
}

/* eslint react/no-multi-comp:0 */
@connect(({ role, loading }) => ({
  role,
  loading: loading.models.role
}))
@Form.create()
class RoleManagement extends PureComponent {
  state = {
    modalVisible: false,
    updateModalVisible: false,
    expandForm: false,
    formValues: {},
    stepFormValues: {}
  };

  columns = [
    {
      title: formatMessage({ id: "app.role.form.name" }),
      dataIndex: "name"
    },
    {
      title: formatMessage({ id: "app.form.operation" }),
      render: (text, record) => (
        <Fragment>
          <a onClick={() => this.handleUpdateModalVisible(true, record)}>
            {formatMessage({ id: "app.form.operation.deploy" })}
          </a>
          <Divider type="vertical" />
          <a onClick={() => this.handleDelete(record)}>
            {formatMessage({ id: "app.form.delete" })}
          </a>
        </Fragment>
      )
    }
  ];

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
      type: "role/fetch"
    });
  };

  handleReloadData = () => {
    if (this.timer) {
      clearTimeout(this.timer);
    }
    this.timer = setTimeout(() => {
      this.handleFetch();
    }, 1000);
  };

  handleAdd = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: "role/addRole",
      payload: fields
    });
    message.success(formatMessage({ id: "app.form.create.successMessage" }));
    this.handleModalVisible();
    this.handleReloadData();
  };

  handleUpdate = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: "role/updateRole",
      payload: {
        ...fields
      }
    });

    message.success(formatMessage({ id: "app.form.operation.deploy.success" }));
    this.handleUpdateModalVisible();
    this.handleReloadData();
  };

  handleDelete = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: "role/deleteRole",
      payload: fields.id
    });
    message.success(formatMessage({ id: "app.form.operation.delete.success" }));
    this.handleReloadData();
  };

  render() {
    const {
      role: { roles },
      loading
    } = this.props;
    let roleList = [];
    if (roles.result != null) {
      roleList = roles.result;
    }
    // const roleList = [];
    // roleList["list"] = roles.result;
    const { modalVisible, updateModalVisible, stepFormValues } = this.state;
    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible
    };
    const updateMethods = {
      handleUpdateModalVisible: this.handleUpdateModalVisible,
      handleUpdate: this.handleUpdate
    };
    return (
      <PageHeaderWrapper title={formatMessage({ id: "app.role" })}>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>
              <Button
                icon="plus"
                type="primary"
                onClick={() => this.handleModalVisible(true)}
              >
                {formatMessage({ id: "app.form.create" })}
              </Button>
            </div>
            <Table
              columns={this.columns}
              loading={loading}
              onChange={this.handleStandardTableChange}
              dataSource={roleList}
              rowKey="id"
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

export default RoleManagement;
