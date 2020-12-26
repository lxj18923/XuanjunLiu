import React, { Component, Fragment } from "react";
import { connect } from "dva";
import { formatMessage, FormattedMessage } from "umi/locale";
import { Row, Col } from "antd";
import {
  ChartCard,
  MiniArea,
  MiniBar,
  MiniProgress,
  Field,
  Bar,
  Pie,
  TimelineChart
} from "@/components/Charts";
import GridContent from "@/components/PageHeaderWrapper/GridContent";
import { getTimeDistance } from "@/utils/utils";

@connect(({ label, loading }) => ({
  label,
  loading: loading.models.label
}))
class Analysis extends Component {
  constructor(props) {
    super(props);
    // this.rankingListData = [];
    // for (let i = 0; i < 7; i += 1) {
    //   this.rankingListData.push({
    //     title: formatMessage({ id: 'app.analysis.test' }, { no: i }),
    //     total: 323234,
    //   });
    // }
  }

  state = {
    salesType: "all",
    currentTabKey: "",
    rangePickerValue: getTimeDistance("year"),
    loading: true
  };

  componentDidMount() {
    const { dispatch } = this.props;
    this.reqRef = requestAnimationFrame(() => {
      dispatch({
        type: "label/count"
      });

      this.timeoutId = setTimeout(() => {
        this.setState({
          loading: false
        });
      }, 600);
    });
  }

  render() {
    const { loading: stateLoading } = this.state;
    const {
      loading: propsLoading,
      label: { count }
    } = this.props;

    const loading = propsLoading || stateLoading;
    // console.log(count);
    const topColResponsiveProps = {
      xs: 24,
      sm: 12,
      md: 12,
      lg: 12,
      xl: 6,
      style: { marginBottom: 24 }
    };
    let total = typeof count.result !== "undefined" ? count.result.total : 0;
    let week = typeof count.result !== "undefined" ? count.result.week : 0;
    let daily = typeof count.result !== "undefined" ? count.result.daily : 0;
    let triadTotal =
      typeof count.result !== "undefined" ? count.result.triadTotal : 0;
    let triadWeek =
      typeof count.result !== "undefined" ? count.result.triadWeek : 0;
    let triadDaily =
      typeof count.result !== "undefined" ? count.result.triadDaily : 0;
    return (
      <Fragment>
        <GridContent>
          <Row gutter={24}>
            <Col {...topColResponsiveProps}>
              <ChartCard
                bordered={false}
                title="today"
                loading={loading}
                total={daily + triadDaily}
                contentHeight={46}
              />
            </Col>
            <Col {...topColResponsiveProps}>
              <ChartCard
                bordered={false}
                title="week"
                loading={loading}
                total={week + triadWeek}
                contentHeight={46}
              />
            </Col>
            <Col {...topColResponsiveProps}>
              <ChartCard
                bordered={false}
                title="total"
                loading={loading}
                total={total + triadTotal}
                contentHeight={46}
              />
            </Col>
          </Row>
        </GridContent>
        {/*<GridContent>*/}
        {/*<Row>*/}
        {/*<Col span={12}>*/}
        {/*<ChartCard*/}
        {/*bordered={false}*/}
        {/*title={*/}
        {/*<FormattedMessage id="app.analysis.total-warming" defaultMessage="过去七天标注数量" />*/}
        {/*}*/}
        {/*loading={loading}*/}
        {/*>*/}
        {/*<TimelineChart*/}
        {/*data={chartData}*/}
        {/*titleMap={{ y1: '标注数量' }}*/}
        {/*height={350}*/}
        {/*/>*/}
        {/*</ChartCard>*/}

        {/*</Col>*/}
        {/*</Row>*/}
        {/*</GridContent>*/}
      </Fragment>
    );
  }
}

export default Analysis;
