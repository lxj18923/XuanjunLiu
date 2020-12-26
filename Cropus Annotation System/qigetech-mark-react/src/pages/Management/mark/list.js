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
  Divider, Table, Popconfirm, Tag, Cascader
} from "antd";
import StandardTable from '@/components/StandardTable';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import styles from './mark.less';
const { TextArea } = Input;

@connect(({ markList, loading }) => ({
  markList,
  loading: loading.models.markList,
}))

class UpdateForm extends PureComponent {

  state = {
    InputValue: '',
  };
  handleGetInputValue = event => {
    this.setState({
      InputValue: event.target.value,
    })
  };
  renderCharacters = segment =>{
    if(typeof segment ==='undefined'){
      return []
    }
    const marked=segment.substr(0, segment.length - 1)
    const characterList =(
      <TextArea
          defaultValue={marked}
          className={styles.input}
          onChange={this.handleGetInputValue}
          autoSize={{minRows: 2, maxRows: 6}}
      />
    )
      return characterList;
  };

  render() {
    const { updateModalVisible, handleUpdateModalVisible, form, handleUpdate,values } = this.props;
    const okHandle = () => {
      const mark=values.markContent.substr(0, values.markContent.length - 1)
      const markSentence = this.state.InputValue ? this.state.InputValue : mark
      const words = [];
      const markWords = markSentence.split(' ');
      for (let i = 0; i < markWords.length; i++) {
        const markWord = markWords[i].split('/');
        words.push({
          word: markWord[0],
          location: i + 1,
          partOfSpeech: markWord[1],
        });
      }
      let spaceNum = 0;
      let skewNum = 0;
      for (let i = 0; i < markSentence.length; i++) {
        if (markSentence.charAt(i) === ' ') {
          spaceNum++;
        } else if (markSentence.charAt(i) === '/') {
          skewNum++;
        }
      }
      if (spaceNum +1 !== skewNum) {
        message.warning(formatMessage({id:'app.label.submit.error'}))
      } else {
        console.log(words)
        let data = {
          "originId": values.originId,
          "words": words
        };
        handleUpdate(data);
      }
    }
    return (
      <Modal
        width={720}
        bodyStyle={{ padding: '32px 40px 48px' }}
        destroyOnClose
        title={formatMessage({id:'app.markList.form.modify.title'})}
        visible={updateModalVisible}
        onCancel={() => handleUpdateModalVisible()}
        onOk={okHandle}
      >
          <Row>
            {this.renderCharacters(values.markContent)}
          </Row>

      </Modal>
    );
  }
}
/* eslint react/no-multi-comp:0 */
@connect(({ markList, loading }) => ({
  markList,
  loading: loading.models.markList,
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
      title: formatMessage({id:'app.markList.form.sentence'}),
      dataIndex: 'sentence',
      render: (text, record) => (
          <div style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
            {text}
          </div>
      )
    },
    {
      title: formatMessage({id:'app.markList.form.systemLabel'}),
      dataIndex: 'systemLabel',
      render: (text, record) => (
          <div style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
            {text}
          </div>
      )
    },
    {
      title: formatMessage({id:'app.markList.form.language'}),
      dataIndex: 'language',
    },
    {
      title: formatMessage({id:'app.markList.form.source'}),
      dataIndex: 'source',
      width:"12%"
    },
    {
      title: formatMessage({id:'app.markList.form.status'}),
      dataIndex: 'status',
      render:(value)=>{
        return this.status[value];
      }
    }
  ];

  status = [
    <Tag color="lime">{formatMessage({id:'app.markList.status.unlabelled'})}</Tag>,
    <Tag color="blue">{formatMessage({id:'app.markList.status.labelledOnce'})}</Tag>,
    <Tag color="red">{formatMessage({id:'app.markList.status.failed'})}</Tag>,
    <Tag color="blue">{formatMessage({id:'app.markList.status.three'})}</Tag>,,
    <Tag color="green">{formatMessage({id:'app.markList.status.success'})}</Tag>,
    <Tag color="green">{formatMessage({id:'app.markList.status.finish'})}</Tag>,,,,
    <Tag color="red">{formatMessage({id:'app.markList.status.notCross'})}</Tag>
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
      type: 'markList/getList',
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
      type: 'markList/getList',
      payload:{}
    });
  };

  handleUpdate = fields => {
    console.log(fields);
    const { dispatch } = this.props;
    dispatch({
      type: 'markList/update',
      payload: fields,
    });
    message.success(formatMessage({id:'app.form.operation.deploy.success'}));
    this.handleUpdateModalVisible();

  };

  handleCover = fields => {
    let data={
      originId:fields.originId
    }
    console.log(data)
    const { dispatch } = this.props;
    dispatch({
      type: "markList/cover",
      payload: data
    });
    message.success(formatMessage({id: 'app.form.operation.delete.success'}));
    this.handleReloadData();
  };

  handleReloadData = ()=>{
    if (this.timer) {
      clearTimeout(this.timer);
    }
    this.timer = setTimeout(() => {
      this.handleFetch()
    }, 1000);
  };

  /**
   * 嵌套表格渲染
   * @param record
   * @returns {*}
   */
  expandedRowRender = (record) => {
    let data = record.labelResults;
    const columns = [
      { title:formatMessage({id:'app.markList.form.markContent'}), dataIndex: "markContent", key: "markContent",width:"50%",
        render: (text, record) => (
            <div style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
              {text}
            </div>
        )},
      { title: formatMessage({id:'app.markList.form.username'}), dataIndex: "username", key: "username" },
      { title: formatMessage({id:'app.markList.form.markDate'}), dataIndex: "markDate", key: "markDate" },
      {
        title: formatMessage({id:'app.form.operation'}), key: "operation", render: (record) =>
          <div>
            <a onClick={() => this.handleUpdateModalVisible(true, record)}>{formatMessage({id:'app.markList.form.modify'})}</a>
            {/*<Divider type="vertical"/>*/}
            {/*<Button type="primary" onClick={() =>{this.handleCover(record)}}>覆盖</Button>*/}
          </div>
      }
    ];

    return <Table columns={columns} dataSource={data} rowKey={"markDate"} pagination={false}/>;
  };

  render() {
    const {
      markList: { list },loading
    } = this.props;
    let labelList = [];
    let labelListPagination=[]
    if(list.result!=null){
      labelList = list.result.records;
      labelListPagination = {
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
      <PageHeaderWrapper title={formatMessage({id:'app.markList'})}>
        <Card bordered={false}>
          <div className={styles.tableList} style={{width:'100%'}}>
            <Table columns={this.columns}
                   expandedRowRender={this.expandedRowRender}
                   loading={loading}
                   dataSource={labelList}
                   rowKey={"id"}
                   onChange={this.handleStandardTableChange}
                   pagination={labelListPagination}/>
          </div>
        </Card>
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
