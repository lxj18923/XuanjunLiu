import {getWeekStatistics, getUserList} from '@/services/statistics';

export default {
    namespace: 'statistics',

    state: {
        week: [],
        list: []
    },

    effects: {
        * fetchWeekStatistics({payload}, {call, put}) {
            const response = yield call(getWeekStatistics);
            yield put({
                type: 'saveWeekStatistics',
                payload: response,
            });
        },
        * fetchUserList({payload}, {call, put}) {
            // console.log(payload)
            const response = yield call(getUserList,payload);
            yield put({
                type: 'saveUserList',
                payload: response,
            });
        },
    },

    reducers: {
        saveWeekStatistics(state, action) {
            return {
                ...state,
                week: action.payload,
            };
        },
        saveUserList(state, action) {
            return {
                ...state,
                list: action.payload,
            };
        }
    },
};
