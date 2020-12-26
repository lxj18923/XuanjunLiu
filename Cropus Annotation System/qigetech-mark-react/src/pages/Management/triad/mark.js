import {connect} from "dva";
import { Button, Card, Form, Icon, Input, message, Row, Select } from "antd";
import {PureComponent} from "react";
import React from "react";

const {TextArea} = Input;
import {DraggableAreasGroup} from 'react-draggable-tags';
import styles from './mark.less';


let id = 0;

const formItemLayoutWithOutLabel = {
    wrapperCol: {
        xs: {span: 24, offset: 0},
        sm: {span: 20, offset: 4},
    },
};

const formItemLayout = {
    labelCol: {
        xs: {span: 24},
        sm: {span: 4},
    },
    wrapperCol: {
        xs: {span: 24},
        sm: {span: 20},
    },
};
const { Option } = Select;


@connect(({ triad, loading }) => ({
    triad,
    loading: loading.models.triad,
}))
@Form.create()
class CreateComment extends PureComponent {

    componentDidMount() {
        this.handleFetch();
    }

    remove = k => {
        const {form} = this.props;
        // can use data-binding to get
        const keys = form.getFieldValue('keys');
        // We need at least one passenger
        if (keys.length === 1) {
            return;
        }

        // can use data-binding to set
        form.setFieldsValue({
            keys: keys.filter(key => key !== k),
        });
    };

    add = () => {
        const {form} = this.props;
        // can use data-binding to get
        const keys = form.getFieldValue('keys');
        const nextKeys = keys.concat(id++);
        // can use data-binding to set
        // important! notify form to detect changes
        form.setFieldsValue({
            keys: nextKeys,
        });

    };

    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const { keys,first, second,third,originId } = values;
                let triads = "";
                keys.map(key=>{
                    let firstStr = "";
                    let secondStr = "";
                    let thirdStr = "";
                    first[key].sort();
                    second[key].sort();
                    third[key].sort();
                    for(let i = 0 ;i<first[key].length;i++){
                        firstStr += first[key][i];
                        if(i!==first[key].length-1){
                            firstStr += "、";
                        }
                    }
                    for(let i = 0 ;i<second[key].length;i++){
                        secondStr += second[key][i] ;
                        if(i!==second[key].length-1){
                            secondStr += "、";
                        }
                    }
                    for(let i = 0 ;i<third[key].length;i++){
                        thirdStr += third[key][i];
                        if(i!==third[key].length-1){
                            thirdStr += "、";
                        }
                    }
                    triads += firstStr +","+secondStr+","+thirdStr+";";
                });

                this.props.dispatch({
                    type: 'triad/save',
                    payload:{
                        content:triads,
                        originId:originId
                    }
                });
            }
        });
    };



    handleFetch = () => {
        const { dispatch } = this.props;
        dispatch({
            type: 'triad/getOrigin',
            payload:{}
        });
    };

    renderDraggable() {
        const {getFieldDecorator, getFieldValue } = this.props.form;
        const {triad: { origin }} = this.props;
        getFieldDecorator('keys', {initialValue: []});
        let originId = origin.result===null || typeof origin.result ==='undefined' ? "" : origin.result.originId;
        getFieldDecorator('originId', {initialValue: originId});
        let markContent = origin.result===null || typeof origin.result ==='undefined' ? " " : origin.result.markContent;
        let words = markContent.split(" ");
        const children = [];
        words.map((word,index)=>{
            children.push(<Option key={index}>{word +'\t' + index}</Option>);
        });

        const keys = getFieldValue('keys');
        return keys.map((k, index) => (
            <Form.Item
                {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
                label={index === 0 ? '三元组' : ''}
                required={false}
                key={k}
            >
                {getFieldDecorator(`first[${k}]`, {
                })(
                    <Select
                        mode="multiple"
                        size={'default'}
                        placeholder="实体"
                        style={{ width: '30%' }}
                        optionFilterProp={"children"}
                    >
                        {children}
                    </Select>
                )}
                {getFieldDecorator(`second[${k}]`, {
                })(
                    <Select
                        mode="multiple"
                        size={'default'}
                        placeholder="关系"
                        style={{ width: '30%' }}
                        optionFilterProp={"children"}
                    >
                        {children}
                    </Select>
                )}
                {getFieldDecorator(`third[${k}]`, {
                })(
                    <Select
                        mode="multiple"
                        size={'default'}
                        placeholder="实体"
                        style={{ width: '30%' }}
                        optionFilterProp={"children"}
                    >
                        {children}
                    </Select>
                )}
                {keys.length > 1 ? (
                    <Icon
                        className="dynamic-delete-button"
                        type="minus-circle-o"
                        onClick={() => this.remove(k)}
                    />
                ) : null}
            </Form.Item>

        ));
    }


    render() {
        const {triad: { origin } } = this.props;
        let markContent = origin.result===null || typeof origin.result ==='undefined' ? " " : origin.result.markContent;
        return (
            <div>
                <Card bordered={false}style={{fontSize:16}}>
                    {markContent}
                </Card>
                <Card bordered={false} style={{marginTop:10}}>
                    <Form onSubmit={this.handleSubmit}>
                        {this.renderDraggable()}
                        <Form.Item {...formItemLayoutWithOutLabel}>
                            <Button type="dashed" onClick={this.add} style={{width: '60%'}}>
                                <Icon type="plus"/> 添加
                            </Button>
                        </Form.Item>
                        <Form.Item {...formItemLayoutWithOutLabel}>
                            <Button type="primary" htmlType="submit">
                                提交
                            </Button>
                        </Form.Item>
                    </Form>
                </Card>

            </div>
        )
    }
}

export default CreateComment;