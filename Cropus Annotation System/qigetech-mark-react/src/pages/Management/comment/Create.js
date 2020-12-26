import { connect } from "dva";
import { Button, Form, Input, message, Row } from "antd";
import { PureComponent } from "react";
import React from "react";
const { TextArea } = Input;
import { formatMessage, FormattedMessage } from 'umi/locale';

@connect(({ comment, loading }) => ({
  comment,
  loading: loading.models.comment,
}))
@Form.create()
class CreateComment extends PureComponent {


  handleSubmit = e => {
    const { dispatch ,actionFunction} = this.props;
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        dispatch({
          type: 'comment/submitComment',
          payload:values
        });
        message.success(formatMessage({id:'app.comment.addMessage.success'}));
        actionFunction();
      }
    });
  };

  render() {
    const { getFieldDecorator, } = this.props.form;
    return (
      <Row>
        <div>
          <Form.Item style={{marginBottom:'0'}}>
            {getFieldDecorator(`parentId`, {
              initialValue: this.props.commentId
            })(<Input hidden />)}
          </Form.Item>
          <Form.Item style={{marginBottom:'0'}}>
            {getFieldDecorator(`originId`, {
              initialValue: this.props.originId
            })(<Input hidden />)}
          </Form.Item>
          <Form.Item style={{marginBottom:'0'}}>
            {getFieldDecorator(`content`, {
            })(<TextArea rows={4} />)}
          </Form.Item>
          <Form.Item>
            <Button htmlType="submit" type="primary" onClick={this.handleSubmit}>
              {formatMessage({id:'app.comment.addMessage'})}
            </Button>
          </Form.Item>
        </div>
      </Row>
    )
  }
}

export default CreateComment;
