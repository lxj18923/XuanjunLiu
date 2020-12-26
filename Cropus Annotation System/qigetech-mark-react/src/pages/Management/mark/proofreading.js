import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import {
    Button,
    Card, Col, Divider, Empty,
    Form, Icon, Input, Row, Select
} from "antd";
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import styles from './mark.less';
import {CopyToClipboard} from 'react-copy-to-clipboard';
import partOfSpeechJson from './partOfSpeech'
let id = 0;

const formItemLayout = {
    labelCol: {
        xs: { span: 24 },
        sm: { span: 4 },
    },
    wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 },
    },
};

/* eslint react/no-multi-comp:0 */
@connect(({ proofreading, loading }) => ({
    proofreading,
    loading: loading.models.proofreading,
}))
@Form.create()
class Label extends PureComponent {

    componentDidMount() {

        const { dispatch } = this.props;
        dispatch({
            type: 'proofreading/getProofreading',
            payload:{}
        });

    }

    handleSubmit = e => {
        const { dispatch } = this.props;
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const {  character,partOfSpeech,originId } = values;
                //对字进行拼接成词
                let words = [];
                let word = "";
                let location = 1;
                console.log(character);
                for(let index in character){
                    word += character[index];
                    if(partOfSpeech[index] !==""){
                        words.push({
                            "word":word,
                            "location":location,
                            "partOfSpeech":partOfSpeech[index]
                        });
                        word = "";
                        location++;
                    }
                }
                let data = {
                    "originId":originId,
                    "words":words
                };
                dispatch({
                    type: 'proofreading/saveProofreading',
                    payload:data
                });
                window.location.reload()
            }
        });
    };

    renderCharacters = segment =>{
        const { getFieldDecorator, } = this.props.form;
        getFieldDecorator('keys', { initialValue: [] });
        let words = segment.split(' ');
        const characterList = words.map((word,index)=>{
            let characters = word.split('/')[0];
            let characterHtml = [];
            for(let i = 0; i<characters.length; i++){
                let partOfSpeech = "";
                //如果是一个词就标注词性
                if(i===characters.length-1){
                    partOfSpeech = word.split('/')[1]
                }
                characterHtml.push(
                  <Col xs={8} sm={6} md={4} lg={3} xl={2} key={characters[i]+"_"+index}>
                      <Form.Item style={{marginRight:0}}>
                          {getFieldDecorator(`character[${(index*10)+i}]`, {
                              initialValue: characters[i]
                          })(<Input placeholder="输入词语" disabled={true} disabled={true}  style={{color:'#000',textAlign:'center',backgroundColor:'white'}}/>)}
                      </Form.Item>
                      <Form.Item>
                          {getFieldDecorator(`partOfSpeech[${(index*10)+i}]`, {
                              initialValue: partOfSpeech
                          })(<Select showSearch maxTagTextLength={15} optionFilterProp="children">
                              <Select.Option value={""}>--</Select.Option>
                              {partOfSpeechJson.map(value=>{
                                  return (
                                    <Select.Option key={value.value} value={value.value}>{value.name}</Select.Option>
                                  )
                              })}
                          </Select>)}
                      </Form.Item>
                  </Col>
                );
            }
            return characterHtml;
        });
        return characterList;
    };

    render() {
        const {
            proofreading: { proofreading },
        } = this.props;
        const { getFieldDecorator } = this.props.form;
        let originId = '';
        let sentence = "暂无数据";
        let segment = "暂无数据/";
        let userResult = [];
        if(typeof proofreading.result != 'undefined'&&proofreading.result !=null){
            let origin = proofreading.result.origin;
            userResult = proofreading.result.labelResults;
            sentence = origin.sentence;
            segment = origin.systemLabel;
            originId = origin.id;
        }else {
            return (<Empty/>)
        }
        getFieldDecorator('originId', { initialValue: originId });
        let charactersHtml = this.renderCharacters(segment);
        return (
            <PageHeaderWrapper title="语料库标注">
                <Card bordered={false}>
                    <Row type="flex" >
                        <Col>
                            <p>{sentence}</p>
                        </Col>
                    </Row>
                    <Form style={{marginTop:15}}>
                        <Row>
                            {charactersHtml}
                        </Row>
                        <Row type="flex">
                            <Col span={2}>
                                <Form.Item {...formItemLayout}>
                                    <Button type="primary" onClick={this.handleSubmit}>
                                        提交
                                    </Button>
                                </Form.Item>
                            </Col>
                        </Row>
                    </Form>
                    <Row type="flex" >
                        {
                            userResult.map(result => (
                                <Col key={result.username} span={6}>
                                    <h3>{result.username}</h3>
                                    <p>{result.markContent}</p>
                                    <p>{result.markDate}</p>
                                </Col>
                            ))
                        }
                    </Row>
                </Card>
            </PageHeaderWrapper>
        );
    }
}

export default Label;
