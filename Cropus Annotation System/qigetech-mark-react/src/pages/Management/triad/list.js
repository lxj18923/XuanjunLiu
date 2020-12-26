import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import {
  Row,
  Col,
  Card,
  Form,
  Input,
  Select,
  Modal,
  message,
  Divider, Table, Tag, Cascader, Button
} from "antd";
import StandardTable from '@/components/StandardTable';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import styles from './mark.less';
const FormItem = Form.Item;
const { Option } = Select;

@connect(({ triad, loading }) => ({
  triad,
  loading: loading.models.triad,
}))
@Form.create()
class AddForm extends PureComponent {

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'triad/getOrigin',
      payload:{}
    });
  }

  render() {
    const { modalVisible, handleModalVisible, form, dispatch,triad: { origin } } = this.props;
    const {getFieldDecorator} = form;
    const okHandle = () => {
      form.validateFields((err, fieldsValue) => {
        if (err) return;
        dispatch({
          type: 'triad/save',
          payload:fieldsValue
        });
      });
    };
    let markContent = origin.result===null || typeof origin.result ==='undefined' ? " " : origin.result.markContent;
    let words = markContent.split(" ");
    let originId = origin.result===null || typeof origin.result ==='undefined' ? " " : origin.result.originId;

    return (
      <Modal
        width={720}
        bodyStyle={{ padding: '32px 40px 48px' }}
        destroyOnClose
        title="添加标注"
        visible={modalVisible}
        onCancel={() => handleModalVisible()}
        onOk={okHandle}
      >
        <Form  style={{marginTop:15}}>
          {
            words.map( (word,index) => {
              return (
              <a key={index} style={{fontSize:16,color:'#000',borderColor:'#ccc',borderStyle:'solid',margin:5,borderWidth:1,borderRadius:5}}>{index+ " " + word}</a>
              )
            })
          }
          <Form.Item style={{marginBottom:'0'}}>
            {getFieldDecorator(`originId`, {
              initialValue: originId
            })(
              <Input hidden/>
            )}
          </Form.Item>
          <Form.Item style={{marginBottom:'0',marginTop:10}}>
            {getFieldDecorator(`content`, {
            })(
              <Input.TextArea rows={4} />
            )}
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

@connect(({ triad, loading }) => ({
  triad,
  loading: loading.models.triad,
}))
@Form.create()
class UpdateForm extends PureComponent {

  render() {
    const { updateModalVisible, handleUpdateModalVisible, form, handleUpdate,values ,dispatch} = this.props;
    const {getFieldDecorator} = form;
    const okHandle = () => {
      form.validateFields((err, fieldsValue) => {
        const {  character,partOfSpeech } = fieldsValue;
        if (err) return;
        dispatch({
          type: 'triad/update',
          payload:fieldsValue
        });
      });
    };
    let markContent = values===null || typeof values.originMarkContent ==='undefined' ? " " : values.originMarkContent;
    let words = markContent.split(" ");
    return (
      <Modal
        width={720}
        bodyStyle={{ padding: '32px 40px 48px' }}
        destroyOnClose
        title="修改标注内容"
        visible={updateModalVisible}
        onCancel={() => handleUpdateModalVisible()}
        onOk={okHandle}
      >
        <Form  style={{marginTop:15}}>
          {
            words.map( (word,index) => {
              return (
                <a key={index} style={{fontSize:16,color:'#000',borderColor:'#ccc',borderStyle:'solid',margin:5,borderWidth:1,borderRadius:5}}>{index+ " " + word}</a>

              )
            })
          }
          <Form.Item style={{marginBottom:'0'}}>
            {getFieldDecorator(`originId`, {
              initialValue: values.originId
            })(
              <Input hidden/>
            )}
          </Form.Item>
          <Form.Item style={{marginBottom:'0'}}>
            {getFieldDecorator(`id`, {
              initialValue: values.id
            })(
              <Input hidden/>
            )}
          </Form.Item>
          <Form.Item style={{marginBottom:'0',marginTop:10}}>
            {getFieldDecorator(`content`, {
              initialValue: values.content
            })(
              <Input.TextArea rows={4} />
            )}
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

/* eslint react/no-multi-comp:0 */
@connect(({ triad, loading }) => ({
  triad,
  loading: loading.models.triad,
}))
@Form.create()
class ListManagement extends PureComponent {
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
      title: '分词结果',
      dataIndex: 'originMarkContent',
    },
    {
      title: '标注内容',
      dataIndex: 'content',
    },
    {
      title: '标注时间',
      dataIndex: 'markDate',
    },
    {
      title: "操作", key: "operation", render: (record) =>
        <div>
          <Divider type="vertical"/>
          <a onClick={() => this.handleUpdateModalVisible(true, record)}>修改</a>
        </div>
    }
  ];

  componentDidMount() {
    this.handleFetch();
  }

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const { dispatch } = this.props;
    console.log(pagination);
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
    };
    dispatch({
      type: 'triad/getList',
      payload: params,
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
      type: 'triad/getList',
      payload:{}
    });
  };

  handleUpdate = fields => {
    console.log(fields);
    const { dispatch } = this.props;
    dispatch({
      type: 'triad/update',
      payload: fields,
    });
    message.success('配置成功');
    this.handleUpdateModalVisible();

  };

  render() {
    const {
      triad: { list },
    } = this.props;
    const labelList = [];
    if(list.result!=null){
      labelList["list"] = list.result.records;
      labelList["pagination"] = {
        current: list.result.current,
        pageSize: list.result.size,
        total: list.result.total,
      }
    }
    const { selectedRows, modalVisible, updateModalVisible, stepFormValues } = this.state;

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };
    const updateMethods = {
      handleUpdateModalVisible: this.handleUpdateModalVisible,
      handleUpdate: this.handleUpdate,
    };
    return (
      <PageHeaderWrapper title="标注结果列表">
        <Card bordered={false}>
          <div className={styles.tableListOperator}>
            <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
              标注三元组
            </Button>
          </div>
          <div className={styles.tableList}>
            <StandardTable
              selectedRows={[]}
              data={labelList}
              columns={this.columns}
              rowKey='id'
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>
        <AddForm
          {...parentMethods}
          modalVisible={modalVisible}
        />
        <UpdateForm
          {...updateMethods}
          updateModalVisible={updateModalVisible}
          values={stepFormValues}
        />
      </PageHeaderWrapper>
    );
  }
}

export default ListManagement;
