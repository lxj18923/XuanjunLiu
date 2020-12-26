import React,{ PureComponent } from 'react'
import { connect } from "dva";
import { Link } from "react-router-dom";
import {
    Form,
    Upload,Button,Icon,message,Card,Menu,
    Tag,Row,Col,Input,Select,Table,Divider
} from 'antd';
import PageHeaderWrapper from "@/components/PageHeaderWrapper";
import styles from "./mark.less";
const { Option } = Select;
@connect(({ search,loading }) => ({
    search,
    loading: loading.models.search,
}))
@Form.create()
class FormUpload extends PureComponent {
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
     * 高亮
     */
    highText(text){
            const {keyword}=this.state
                const reg = new RegExp("(" + keyword + ")","g");
                let html = { __html: text.replace(reg,"<font style='background:#ff0;'>$1</font>") };
                text = <div dangerouslySetInnerHTML={html}></div>
                return text
    }
    /**
     *  table的数据
     * @info {*[]}
     */
    columns = [
        {
            title: '原句',
            dataIndex: 'sentence',
            key: 'sentence',
            width:400,
            render: text =>this.highText(text)
        },
        {
            title: '系统分词',
            dataIndex: 'systemLabel',
            width:400,
        },
        {
            title: '语言',
            dataIndex: 'language',
        },
        {
            title: '来源',
            dataIndex: 'source',
        },
        {
            title: '状态',
            dataIndex: 'status',
            render: (value) => {
                return this.status[value];
            }
        }
    ];
    status = [
        <Tag color="lime">未标注</Tag>,
        <Tag color="blue">标注一次</Tag>,
        <Tag color="red">交叉验证失败</Tag>,
        <Tag color="blue">标注三次</Tag>,,
        <Tag color="green">交叉验证成功</Tag>,,,,
        <Tag color="red">已删除</Tag>,
        <Tag color="red">未进行交叉验证</Tag>,
        <Tag color="red">无</Tag>
    ];

    componentDidMount() {
        this.handleFetch(null, null, null,1);
    }

    handleFetch(status,source,word,current) {
        const { dispatch } = this.props;
        let params = {
            status: status,
            source: source,
            word: word,
            current: current
        };
        dispatch({
            type: "search/searchBar",
            payload: params,
            callback: (response) => {
                if(response.result!==null){
                    if(response.result.total!==0){
                        this.setState({
                            total:response.result.total
                        })
                    }
                    if(response.result.records.length===0){
                        message.error("抱歉，系统未能找到相关结果！")
                    }
                }
            }
        });
    }


    /**
     * table修改事件控制，如分页、筛选、排序
     * @param pagination
     */
    handleStandardTableChange = (pagination) => {
        const { word,status,source } = this.state;
        this.handleFetch(status,source,word,pagination.current);
    };

    /**
     * 重置
     */
    handleFormReset = () => {
        const { form } = this.props;
        form.resetFields();
        this.setState({
            formValues: {}
        });
    };

    /**
     * 搜索
     */
    handleSearch = e => {
        e.preventDefault();
        const { form } = this.props;
        form.validateFields((err,fieldsValue) => {
            console.log(fieldsValue)
            if(err) return;
            let status = fieldsValue.status;
            console.log(status)
            let source = fieldsValue.source;
            let word = fieldsValue.word;
            this.setState({
                status: status,
                source: source,
                word: word,
                keyword:word
            });
            this.handleFetch(status,source,word,1);
        });
    };

    /**
     * 加载更多工具栏
     * 页头部
     * @returns {*}
     */
    renderAdvancedForm() {
        const {
            form: { getFieldDecorator }
        } = this.props;
        const formItemLayout = {
            labelCol: { span: 3 },
            wrapperCol: { span: 12 },
        };
        return (
            <Form onSubmit={this.handleSearch} {...formItemLayout}>
                        <Form.Item label="状态">
                            {getFieldDecorator("status")(
                                <Select placeholder="请选择">
                                    <Option value="0">未标注</Option>
                                    <Option value="1">标注一次</Option>
                                    <Option value="2">交叉验证失败</Option>
                                    <Option value="3">标注三次</Option>
                                    <Option value="5">交叉验证成功</Option>
                                    <Option value="9">已删除</Option>
                                    <Option value="10">未进行交叉验证</Option>
                                </Select>
                            )}
                        </Form.Item>
                        <Form.Item label="来源">
                            {getFieldDecorator("source",{ rules: [{message: '请选择' }]})(
                                <Select placeholder="请选择">
                                    <Option value="香港律政司">香港律政司</Option>
                                    <Option value="行政长官2018年施政报告">行政长官2018年施政报告</Option>
                                    <Option value="明报">明报</Option>
                                    <Option value="香港政府一站通">香港政府一站通</Option>
                                    <Option value="政府账目及报告">政府账目及报告</Option>
                                    <Option value="香港特別行政区政府新闻公报">香港特別行政区政府新闻公报</Option>
                                </Select>
                                )}
                        </Form.Item>
                        <Form.Item label="词语">
                            {getFieldDecorator("word")(<Input placeholder="请输入" />)}
                        </Form.Item>

                <div style={{ overflow: "hidden" }}>
                    <div style={{ float: "right",marginBottom: 24 }}>
                        <Button type="primary" htmlType="submit">
                            查询
                        </Button>
                        <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
                            重置
                        </Button>
                    </div>
                </div>
            </Form>
        );
    }

    /**
     * 嵌套表格渲染
     * @param record
     * @returns {*}
     */
    expandedRowRender = (record) => {
        let data = record.labelResults;
        const columns = [
            { title: "用户标注内容", dataIndex: "markContent", key: "markContent",width:600 },
            { title: "用户名", dataIndex: "username", key: "username" },
            { title: "标注时间", dataIndex: "markDate", key: "markDate" }
        ];

        return <Table columns={columns} dataSource={data} rowKey={"markDate"} pagination={false}/>;
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
                    message.success(response.result.info)}
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

    render() {
        const {
            search: { searchBarResult },
            loading
        } = this.props;
        //赋值通知列表数据
        let searchBarResultList = [];
        let searchBarPagination = [];

        if(searchBarResult.result !=null) {
            searchBarResultList = searchBarResult.result.records;
            searchBarPagination = {
                current: searchBarResult.result.current,
                pageSize: searchBarResult.result.size,
                total: searchBarResult.result.total
            };
        }
        const { selectedRows } = this.state;

        const props = {
            name: "file",
            accept: ".xls",
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
                const isExl = file.name.substr(file.name.length - 4);
                const isLt10M = file.size / 1024 / 1024 < 10;
                if(isExl !== ".xls") {
                    message.error('请上传 .xls 类型的文件！');
                    return false;
                }
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
        return (
            <PageHeaderWrapper title="语料搜索">
                <Card bordered={false} style={{width:'100%'}}>
                    <Row>
                        <Col span={8}>
                    <Upload {...props}>
                        <Button>
                            <Icon type="upload" /> 上传语料
                        </Button>
                    </Upload>
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

                    </Button>
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

                        <Col span={16}>{this.renderAdvancedForm()}</Col>
                    </Row>

                    {
                       this.state.total!==0?
                            <p style={{color:'#999999',marginBottom:'5px'}}>语料库为您找到{this.state.total}条结果</p>:''
                    }
                    <Table columns={this.columns}
                           tableLayout={"auto"}
                           expandedRowRender={this.expandedRowRender}
                           loading={loading}
                           dataSource={searchBarResultList}
                           rowKey={"id"}
                           onChange={this.handleStandardTableChange}
                           pagination={searchBarPagination}/>

                </Card>
            </PageHeaderWrapper>

        )
    }
}

export default FormUpload
