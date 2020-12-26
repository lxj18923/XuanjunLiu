import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import { formatMessage, FormattedMessage } from 'umi/locale';
import {
  Card,
  Form,
  Select,
  message,
  Table, Drawer, Button, Col, Row, Comment
} from "antd";
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import styles from './comment.less';
import moment from "moment";
const FormItem = Form.Item;
const { Option } = Select;
import CreateComment from './Create';
import StandardTable from '@/components/StandardTable';
/* eslint react/no-multi-comp:0 */
@connect(({ comment, loading }) => ({
  comment,
  loading: loading.models.comment,
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
    drawerVisible: false,
    commendInfo:{}
  };

  columns = [
    {
      title: formatMessage({id:'app.comment.form.content'}),
      dataIndex: 'content'

    },
    {
      title: formatMessage({id:'app.comment.form.username'}),
      dataIndex: 'username',
    },
    {
      title:formatMessage({id:'app.comment.form.postDate'}),
      dataIndex: 'postDate',
    },
    {
      title: formatMessage({id:'app.form.operation'}),
      key: 'x',
      render:(record)=>
        <Button onClick={()=>{this.onShowDrawer(record)}}>{formatMessage({id:'app.comment.form.operation.read'})}</Button>
    }
  ];


  componentDidMount() {
    this.handleFetch();
  }

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
      type: 'comment/getList',
      payload:{}
    });
  };

  handleUpdate = fields => {
    console.log(fields);
    const { dispatch } = this.props;
    dispatch({
      type: 'comment/update',
      payload: fields,
    });
    message.success('配置成功');
    this.handleUpdateModalVisible();
  };

  onShowDrawer = (record) =>{
    this.setState({
      drawerVisible: true,
      commendInfo:record
    });
  }

  onDrawerClose = () => {
    this.setState({
      drawerVisible: false,
    });
  };

  renderDrawer = () => {
    let record = this.state.commendInfo;
    let labelResults = typeof record.labelResults!=='undefined'&&record.labelResults!=null?record.labelResults:[];
    let comments = typeof record.comments!=='undefined'||record.comments!=null?record.comments:[];
    return (
      <Drawer
        width={720}
        title={formatMessage({id:'app.comment.modal.detailMessage'})}
        placement="right"
        closable={false}
        onClose={this.onDrawerClose}
        visible={this.state.drawerVisible}
      >

        <Row type="flex">
          <p>{record.content}</p>
        </Row>
        <Row >
          <h3 style={{color:"#ff2d51",display:labelResults.length===0?'none':''}}>{formatMessage({id:'app.comment.modal.detailMark'})}</h3>
          <div>
            {
              labelResults.map(result => (
                <Col key={result.username} span={6} style={{marginLeft:10}}>
                  <h3>{result.username}</h3>
                  <p>{result.markContent}</p>
                  <p>{result.markDate}</p>
                </Col>
              ))
            }
          </div>
        </Row>
        <Row >
          <h3 style={{color:"#ff2d51",display:comments.length===0?'none':''}}>{formatMessage({id:'app.comment.modal.allReply'})}</h3>
          <div>
            {
              comments.map(result => (
                <Comment
                  key={result.id}
                  author={<a>{result.username}</a>}
                  content={
                    <p>
                      {result.content}
                    </p>
                  }
                  datetime={
                    <span>{result.postDate}</span>
                  }
                />
              ))
            }
          </div>
        </Row>
        <h3 style={{color:"#ff2d51"}}>{formatMessage({id:'app.comment.modal.reply'})}</h3>
        <CreateComment
          actionFunction={()=>{setTimeout(() => {
            window.location.reload()}, 1000);}}
          commentId={record.id}/>
      </Drawer>
    )
  };

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const { dispatch } = this.props;
    console.log(pagination);
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
    };
    dispatch({
      type: 'comment/getList',
      payload: params,
    });
  };

  render() {
    const {
      comment: { list },loading
    } = this.props;
    let commentList = [];
    let commentPagination=[]
    if(list.result!=null){
      commentList = list.result.records;
      commentPagination = {
        current: list.result.current,
        pageSize: list.result.size,
        total: list.result.total,
      }
    }
    return (
      <PageHeaderWrapper title={formatMessage({id:'app.comment'})}>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <Table
              loading={loading}
              dataSource={commentList}
              pagination={commentPagination}
              columns={this.columns}
              rowKey='id'
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>
        <Card bordered={false} style={{marginTop:10}}>
          <h3 style={{color:"#ff2d51"}}>{formatMessage({id:'app.comment.message'})}</h3>
          <CreateComment  actionFunction={()=>{setTimeout(() => {
            window.location.reload()}, 1000);}} />
        </Card>
        {this.renderDrawer()}
      </PageHeaderWrapper>
    );
  }
}

export default ListManagement;
