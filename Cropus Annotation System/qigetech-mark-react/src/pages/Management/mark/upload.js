import React,{ PureComponent } from 'react'
import { connect } from "dva";
import { Link } from "react-router-dom";
import {
  Form,
  Upload,Button,Icon,message,Card,Menu,
  Tag,Row,Col,Input,Select,Table,Divider,Tabs
} from 'antd';
import PageHeaderWrapper from "@/components/PageHeaderWrapper";
import styles from "./mark.less";
@connect(({ search,loading }) => ({
  search,
  loading: loading.models.search,
}))
@Form.create()
class FormUpload extends PureComponent {
  callback(key) {
    console.log(key);
  }
  state = {
    fileList: [],
    loading: false,
    totalArticle: 0,
    totalOrigin: 0,
    submitButton: true,
    selectedRows: [],
    //工具栏表单数据
    formValues: {},
    word: "",
    status: "",
    source: "",
    total:0,
    keyword:''
  };

  /**
   * 上传
   */
  onSubmitUpload = () => {
    this.setState({ loading: true })
    const { fileList } = this.state;
    const formData = new FormData();
    fileList.forEach(file => {
      formData.append('file',file);
    });
    const { dispatch } = this.props;
    dispatch({
      type: 'search/upload',
      payload: formData,
      callback: (response) => {
        this.setState({ loading: false })
        if(response.result.err === 0) {
          if(response.result.totalArticle===0&&response.result.totalOrigin===0){
            message.warning('该文件在语料库中已存在，请上传新语料！')
          }else {
            message.success(response.result.info)
          }
          this.setState({
            loading: false,
            totalArticle: response.result.totalArticle,
            totalOrigin: response.result.totalOrigin,
            submitButton: true
          })
        } else if(response.result.err === 1) {
          message.error(response.result.info)
          this.setState({
            submitButton: true
          })
        }
      }
    });

  };

  renderUpload = (type,tab) =>{
    const props = {
      name: "file",
      accept: type,
      onRemove: file => {
        this.setState(state => {
          const index = state.fileList.indexOf(file);
          const newFileList = state.fileList.slice();
          newFileList.splice(index,1);
          return {
            fileList: newFileList,
          };
        });
        this.setState({
          totalArticle: 0,
          totalOrigin: 0,
          submitButton: true
        })
      },
      //文件上传之前的操作
      beforeUpload: (file) => {
        const isType = file.name.substr(file.name.length - 4);
        const isLt10M = file.size / 1024 / 1024 < 10;

        if(tab === "tab1"){
          if(isType !== ".txt"){
            message.error('请上传 .txt类型的文件！');
            return false;
          }
        }
        if(tab === "ta2"){
          if(isType !== ".xls"){
            message.error('请上传 .xls类型的文件！');
            return false;
          }
        }

        // if(isType !== ".xls" ) {
        //   if(isType !== ".txt"){
        //     message.error('请上传 .xls or .txt类型的文件！');
        //     return false;
        //   }
        // }
        if(!isLt10M) {
          message.error('上传文件的大小不能超过10M!');
          return false;
        }
        if(this.state.fileList.length === 0) {
          this.setState(state => ({
            fileList: [...state.fileList,file],
            submitButton: false
          }));
        } else {
          message.error('只能上传一个文件');
          this.setState(state => ({
            fileList: [...state.fileList],
          }));
        }
        return false;
      },
      fileList: this.state.fileList
    }


    return <Row>
      <Col span={12}>
        <Upload {...props}>
          <Button>
            <Icon type="upload" /> 上传语料
          </Button>
        </Upload>

        {
          this.state.totalArticle !== 0 && this.state.totalOrigin !== 0 && this.state.fileList.length !== 0 ?
              <div style={{
                fontSize: '15px',
                lineHeight: '15px',
                margin: '12px 0 0 12px',
                color: '#1890ff'
              }}>
                <p>已导入文章：{this.state.totalArticle}</p>
                <p>已导入语料：{this.state.totalOrigin}</p>
              </div>
              : ''
        }
      </Col>

      <Col span={12}>
        <Button
            style={{margin:"10px",float: 'right',right: "50px" }}
            disabled={this.state.submitButton}
            type="primary"
            onClick={this.onSubmitUpload}>
          {
            this.state.loading ? <div>
              <Icon type='loading' />
              &nbsp;上传中...
            </div> : <span>提交</span>
          }

        </Button></Col>
    </Row>
  };

  render() {
    function callback(key) {
      console.log(key);
    }
    const { TabPane } = Tabs;
    const { TextArea } = Input;
    return (
        <PageHeaderWrapper title="上传语料">
          <Card bordered={false} style={{width:'100%'}}>
            <Tabs defaultActiveKey="1" onChange={callback}>
              <TabPane tab="文本上传" key="1">
                {this.renderUpload('.txt','tab1')}
                {/*{*/}
                {/*  this.state.total!==0?*/}
                {/*      <p style={{color:'#999999',marginBottom:'5px'}}>语料库为您找到{this.state.total}条结果</p>:''*/}
                {/*}*/}
                <TextArea rows={4} value={this.state.fileList}/>

              </TabPane>
              <TabPane tab="Excel导入" key="2">
                {this.renderUpload('.xls','tab2')}
              </TabPane>
              <TabPane tab="接口导入" key="3">
                <p>API:</p>
                <p>消息队列:</p>
              </TabPane>
            </Tabs>

          </Card>
        </PageHeaderWrapper>

    )
  }
}

export default FormUpload
