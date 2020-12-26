import React, { Component } from "react";
import { connect } from "dva";
import {
  Button,
  Card,
  Col,
  Divider,
  Empty,
  Form,
  Cascader,
  Input,
  Row,
  Select,
  Icon
} from "antd";
import styles from "./mark.less";
import partOfSpeechJson from "./partOfSpeech";

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 4 }
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 20 }
  }
};

/* eslint react/no-multi-comp:0 */
@connect(({ label, loading }) => ({
  label,
  submitting: loading.effects["label/label"]
}))
@Form.create()
class Label extends Component {
  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: "label/getOriginByRandom"
    });
  }

  handleSubmit = e => {
    const { dispatch } = this.props;
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const { character, partOfSpeech, originId } = values;
        //对字进行拼接成词
        let words = [];
        let word = "";
        let location = 1;
        for (let index in character) {
          word += character[index];
          if (
            partOfSpeech[index][1] !== "" &&
            typeof partOfSpeech[index][1] !== "undefined"
          ) {
            words.push({
              word: word,
              location: location,
              partOfSpeech: partOfSpeech[index][1]
            });
            word = "";
            location++;
          }
        }
        let data = {
          originId: originId,
          words: words
        };
        dispatch({
          type: "label/saveResult",
          payload: data
        });
      }
    });
  };

  handleSkip = () => {
    const { dispatch } = this.props;
    dispatch({
      type: "label/skip",
      payload: {}
    });
  };

  renderCharacters = segment => {
    const { getFieldDecorator } = this.props.form;
    getFieldDecorator("keys", { initialValue: [] });
    let words = segment.split(" ");
    function filter(inputValue, path) {
      return path.some(
        option =>
          option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
      );
    }
    const characterList = words.map((word, index) => {
      let characters = word.split("/")[0];
      let characterHtml = [];
      for (let i = 0; i < characters.length; i++) {
        let partOfSpeech = "";
        //如果是一个词就标注词性
        if (i === characters.length - 1) {
          partOfSpeech = word.split("/")[1];
        }
        let partOfSpeechs = [];
        partOfSpeechs.push(partOfSpeech.substring(0, 1));
        partOfSpeechs.push(partOfSpeech);

        // console.log(`partOfSpeech[${index * 100 + i}]`);

        characterHtml.push(
          <Col
            xs={8}
            sm={6}
            md={4}
            lg={2}
            xl={2}
            key={characters[i] + "_" + index + "_" + i}
          >
            <Form.Item style={{ marginBottom: "0" }}>
              {getFieldDecorator(`character[${index * 100 + i}]`, {
                initialValue: characters[i]
              })(
                <Input
                  placeholder="输入词语"
                  disabled={true}
                  style={{
                    fontSize: 16,
                    color: "#000",
                    textAlign: "center",
                    backgroundColor: "white",
                    border: "0px solid #d9d9d9"
                  }}
                />
              )}
            </Form.Item>
            <Form.Item style={{ marginBottom: "0" }}>
              {getFieldDecorator(`partOfSpeech[${index * 100 + i}]`, {
                initialValue: partOfSpeechs
              })(
                <Cascader options={partOfSpeechJson} showSearch={{ filter }} />
              )}
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
      label: { origin }
    } = this.props;
    const { getFieldDecorator } = this.props.form;
    let originId = "";
    let sentence = "暂无数据";
    let segment = "暂无数据/";
    if (typeof origin.result != "undefined" && origin.result != null) {
      sentence = origin.result.sentence;
      segment = origin.result.systemLabel;
      originId = origin.result.id;
    } else {
      return <Empty />;
    }
    getFieldDecorator("originId", { initialValue: originId });
    let charactersHtml = this.renderCharacters(segment);
    return (
      <div>
        <Card className={styles.selectCard}>
          <Button style={{ float: "right" }} type="default">
            <a href="./label">返回</a>
          </Button>
          <Row type="flex" justify="start">
            <Col>
              <p className={styles.sentence}>
                <Icon type="profile" theme="twoTone" className={styles.icon} />
                {sentence}
              </p>
            </Col>
          </Row>
          <Form style={{ marginTop: 15 }}>
            <Row>{charactersHtml}</Row>
            <Row type="flex" style={{ margin: "20px 0" }}>
              <Col span={2}>
                <Form.Item {...formItemLayout}>
                  <Button type="primary" onClick={this.handleSubmit}>
                    提交
                  </Button>
                </Form.Item>
              </Col>
              <Col span={2}>
                <Form.Item {...formItemLayout}>
                  <Button type="default" onClick={this.handleSkip}>
                    跳过
                  </Button>
                </Form.Item>
              </Col>
            </Row>
          </Form>
        </Card>
      </div>
    );
  }
}

export default Label;
