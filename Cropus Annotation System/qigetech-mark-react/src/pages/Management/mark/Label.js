import React, { Component } from "react";
import {
  Card,
  Row,
  Col,
  Divider,
  message,
  Form,
  Icon,
  Button,
  Input,
  Tooltip
} from "antd";
import { formatMessage, FormattedMessage } from "umi/locale";
import { connect } from "dva";
import styles from "./mark.less";


const { TextArea } = Input;
const { Search } = Input;

@connect(({ label, loading }) => ({
  label,
  submitting: loading.effects["label/label"]
}))
@Form.create()
class mark extends Component {
  state = {
    btnStatus:false,
    InputValue: "",
    isClick: true,
    check: true
  };

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: "label/getOriginByRandom"
    });
    dispatch({
      type: "label/getUserResults"
    });
  }

  handleGetInputValue = event => {
    this.setState({
      InputValue: event.target.value
    });
  };

  handleSkip = () => {
    const { dispatch } = this.props;
    dispatch({
      type: "label/skip",
      payload: {}
    });
  };

  handleDelete = () => {
    const { dispatch } = this.props;
    dispatch({
      type: "label/deleteSentence"
    });
    message.success(formatMessage({ id: "app.form.operation.delete.success" }));
  };

  handleSearch = value => {
    const { dispatch } = this.props;
    dispatch({
      type: "label/searchResult",
      payload: value
    });
    setTimeout(() => {
      this.props.label.searchResult.length === 0
        ? message.error(formatMessage({ id: "app.label.search.error" }))
        : "";
    }, 500);
  };

  handlePost = (systemLabel, id) => {
    const { isClick } = this.state;
    if (isClick) {
      //如果为true 开始执行
      this.setState({btnStatus:true});
      this.setState({ isClick: false });
      let markSentence = this.state.InputValue
        ? this.state.InputValue
        : systemLabel;
      const words = [];
      const markWords = markSentence.split(" ");
      for (let i = 0; i < markWords.length; i++) {
        const markWord = markWords[i].split("/");
        // console.log(markWord)
        words.push({
          word: markWord[0],
          location: i + 1,
          partOfSpeech: markWord[1]
        });
      }
      let check = words.some(item => {
        if (item.partOfSpeech === "" || item.word === "") {
          return true;
        }
      });
      let spaceNum = 0;
      let skewNum = 0;
      for (let i = 0; i < markSentence.length; i++) {
        if (markSentence.charAt(i) === " ") {
          spaceNum++;
        } else if (markSentence.charAt(i) === "/") {
          skewNum++;
        }
      }
      if (spaceNum + 1 !== skewNum || check === true) {
        message.warning(formatMessage({ id: "app.label.submit.error" }));
        this.setState({btnStatus:false});

      } else {
        // 在此做提交操作，比如发dispatch等
        let data = {
          originId: id,
          words: words
        };
        const { dispatch } = this.props;
        dispatch({
          type: "label/saveResult",
          payload: data
        });
        message.success(formatMessage({ id: "app.label.submit.success" }));
      }
      const that = this;
      setTimeout(function() {
        // 设置延迟事件，1秒后将执行

        that.setState({ isClick: true }); // 将isClick设置为true
      }, 3000);
    }
  };

  render() {
    const {
      label: { origin, labelResults, searchResult }
    } = this.props;

    let sentence, systemLabel, id;
    if (origin.result != null) {
      sentence = origin.result.sentence;
      systemLabel = origin.result.systemLabel;
      id = origin.result.id;
    }
    const text = "]/nt";
    let compoundWord =
      systemLabel !== undefined ? systemLabel.indexOf(text) : "";
    return (
      <div>
        <Card className={styles.card}>
          <Button style={{ float: "right" }} type="default">
            <a href="./selectLabel">选择标注</a>
          </Button>
          <p className={styles.sentence}>
            <Icon type="profile" theme="twoTone" className={styles.icon} />
            {sentence}
          </p>
          <p>
            <Icon type="bulb" theme="twoTone" className={styles.icon} />
            {formatMessage({ id: "app.label.systemLabel" })}
            {systemLabel}
          </p>
          {compoundWord > 0 && compoundWord != null ? (
            <p style={{ fontSize: "14px", color: "red" }}>
              {formatMessage({ id: "app.label.tip" })}
            </p>
          ) : (
            ""
          )}
          <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
            <Col md={21} sm={24}>
              <TextArea
                key={id}
                defaultValue={systemLabel}
                className={styles.input}
                onChange={this.handleGetInputValue}
                rows={4}
                // autoSize={{ minRows: 2, maxRows: 6 }}
              />
            </Col>
          </Row>
          <Row style={{ margin: "1% 0 2%" }}>
            <Tooltip
              placement="bottom"
              title={formatMessage({ id: "app.label.submit.tip" })}
            >
              <Button
                type="primary"
                style={{ float: "right" }}
                size="large"
                disabled={this.state.btnStatus}
                onClick={() => {
                  this.handlePost(systemLabel, id);
                }}
              >

                {formatMessage({ id: "app.label.submit" })}
              </Button>
            </Tooltip>
          </Row>

          <Button
            style={{ margin: "20px 0 0" }}
            type="default"
            onClick={this.handleSkip}
          >
            {formatMessage({ id: "app.label.skip" })}
          </Button>

          {labelResults.length === 0 ? (
            <Tooltip
              placement="right"
              title={formatMessage({ id: "app.label.delete.tip" })}
            >
              <Button
                style={{
                  margin: "20px 0 0 50px",
                  color: "red",
                  border: "1px solid red"
                }}
                type="default"
                onClick={this.handleDelete}
              >
                {formatMessage({ id: "app.label.delete" })}
              </Button>
            </Tooltip>
          ) : (
            ""
          )}
        </Card>
        <Card className={styles.card}>
          <Row style={{ margin: "10px 0 20px 0" }}>
            <div
              style={{
                fontWeight: "600",
                fontSize: "22px",
                lineHeight: "44px"
              }}
            >
              {formatMessage({ id: "app.label.dictionary" })}
            </div>
            <Col span={8}>
              <Search
                placeholder={formatMessage({
                  id: "app.label.search.placeholder"
                })}
                enterButton
                size="large"
                onSearch={this.handleSearch}
                style={{ padding: "10px 30px 10px 10px" }}
              />
            </Col>
            {searchResult !== "" ? (
              <div>
                {searchResult.map((item, index) => {
                  const { partOfSpeech, count } = item;
                  return (
                    <Col key={index} span={8}>
                      <div
                        key={index}
                        style={{
                          border: "1px solid #1890ff",
                          fontSize: "17px",
                          lineHeight: "34px",
                          margin: "10px",
                          padding: "5px"
                        }}
                      >
                        {formatMessage({ id: "app.label.search.partOfSpeech" })}
                        {partOfSpeech}
                        {/*<Divider type="vertical"*/}
                        {/*         style={{border: '1px solid rgba(0, 0, 0, 0.65)'}}/>*/}
                        <br />
                        {formatMessage({ id: "app.label.search.count" })}
                        {count}
                      </div>
                    </Col>
                  );
                })}
              </div>
            ) : (
              ""
            )}
          </Row>

          <Row gutter={{ md: 8, lg: 24, xl: 48 }} key={id}>
            {labelResults.map((item, index) => {
              const {
                markContent,
                userId,
              } = item;
              console.log();
              const markNum = `${formatMessage({
                id: "app.label.userResult.left"
              })} ${index + 1} ${formatMessage({
                id: "app.label.userResult.right"
              })}`;
              return (
                <Col md={24} sm={24} key={userId}>
                  <Card title={markNum} className={styles.markCard}>
                    {markContent}
                  </Card>
                </Col>
              );
            })}
          </Row>
        </Card>
      </div>
    );
  }
}

export default mark;
