import React, {Component, Fragment} from 'react';
import { FormattedMessage, formatMessage } from 'umi/locale';
import {
    Chart,
    Geom,
    Axis,
    Label,
} from "bizcharts";
import moment from 'moment'
import {
    Row,
    Col,
    Card, Pagination, Button, Icon, Table, Tooltip
} from 'antd';
import {connect} from "dva";

@connect(({statistics, loading}) => ({
    statistics,
    loading: loading.models.statistics,
}))
class Analysis extends Component {
    state = {
        selectedRows: []
    }

    componentDidMount() {
        const {dispatch} = this.props;
        dispatch({
            type: 'statistics/fetchWeekStatistics',
        });
        dispatch({
            type: 'statistics/fetchUserList',
            payload: {}
        });
    }

    handlePageChange = (pagination) => {
        const {dispatch} = this.props;
        const params = {
            current: pagination.current,
            size: pagination.pageSize,
        };
        dispatch({
            type: 'statistics/fetchUserList',
            payload: params,
        });
    };
    handleSelectRows = rows => {
        this.setState({
            selectedRows: rows,
        });
    };
    columns = [
        {
            title:formatMessage({id:'app.statistics.form.userId'}),
            dataIndex: 'userId',
        },
        {
            title: formatMessage({id:'app.statistics.form.userName'}),
            dataIndex: 'username',
        },
        {
            title: formatMessage({id:'app.statistics.form.lastMarkdate'}),
            dataIndex: 'lastMarkdate',
        },
        {
            title: formatMessage({id:'app.statistics.form.countDaily'}),
            dataIndex: 'countDaily',
            align: 'center'
        },
        {
            title: formatMessage({id:'app.statistics.form.countWeek'}),
            dataIndex: 'countWeek',
            align: 'center'
        },
        {
            title: formatMessage({id:'app.statistics.form.countTotal'}),
            dataIndex: 'countTotal',
            align: 'center'
        }
    ];

    render() {
        const {statistics: {week, list}, loading} = this.props;
        const data = week.result !== undefined ? week.result : [];
        const cols = {
            countDate: {
                formatter: (val) => {
                    val = moment(val).format('YYYY-MM-DD');
                    return val
                }
            },
            amount: {
                range: [0, 1]
            }

        };
        const {selectedRows} = this.state;

        let userList = [];
        let pagination = []
        if (list.result != null) {
            userList = list.result.records;
            pagination = {
                current: list.result.current,
                pageSize: list.result.size,
                total: list.result.total,
            }
        }

        return (
            <Fragment>
                {data.length !== 0 ?
                    <Card style={{marginBottom: '20px'}}>
                        <span style={{fontSize: '23px', fontWeight: '600', margin: '20px 5px'}}>
                            <FormattedMessage id="app.statistics.overall"  />
                        </span>
                        <Tooltip placement="leftBottom" title={formatMessage({id: 'app.statistics.overall.tip'})}>
                        <Icon type="question-circle" style={{fontSize:"20px",marginRight:"20px",color:"#333",float:"right"}}/>
                        </Tooltip>
                        <Row span={24} style={{margin: '40px 100px 0px'}}>

                            <Chart height={400} data={data} scale={cols} forceFit>

                                <Axis name="countDate"
                                />
                                <Axis name="amount"/>

                                <Geom type="line" position="countDate*amount" size={2}/>
                                <Geom
                                    type="point"
                                    position="countDate*amount"
                                    size={4}
                                    shape={"circle"}
                                    style={{
                                        stroke: "#fff",
                                        lineWidth: 1
                                    }}
                                >
                                    <Label
                                        content="amount"
                                    />
                                </Geom>
                            </Chart>
                        </Row>
                    </Card> : ''
                }
                <Card bordered={false}>
                    <span style={{fontSize: '23px', fontWeight: '600', margin: '0px 0px 20px 5px'}}>
                        <FormattedMessage id="app.statistics.users"  /></span>
                    <Tooltip placement="leftBottom" title={formatMessage({id: 'app.statistics.users.tip'})}>
                        <Icon type="question-circle" style={{fontSize:"20px",marginRight:"20px",color:"#333",float:"right"}}/>
                    </Tooltip>
                    <div style={{margin: '30px 50px'}}>
                        <Table columns={this.columns}
                               loading={loading}
                            // bordered={true}
                               onChange={this.handlePageChange}
                               dataSource={userList}
                               pagination={pagination}
                               rowKey='userId'/>
                    </div>
                </Card>
            </Fragment>
        );
    }
}

export default Analysis;
