import {
    getOriginByRandom,
    saveResult,
    getList,
    skipOrigin,
    getCount,
    getUserResults,
    searchResult,
    getUserInfo,
    deleteSentence,
} from '@/services/mark/label';

export default {
    namespace: 'label',
    state: {
        origin: {},
        labels: {},
        count: {},
        labelResults: [],
        searchResult: '',
        userInfo: '',

    },

    effects: {
        * getOriginByRandom({payload}, {call, put}) {
            const response = yield call(getOriginByRandom);
            yield put({
                type: 'setOrigin',
                payload: response,
            });
        },
        * getUserResults({payload}, {call, put}) {
            const response = yield call(getUserResults);
            yield put({
                type: 'setUserResults',
                payload: response,
            });
        },
        * getList({payload}, {call, put}) {
            const response = yield call(getList);
            yield put({
                type: 'setLabels',
                payload: response,
            });
        },
        * saveResult({payload}, {call, put}) {
            yield call(saveResult, payload);
            window.location.reload();
        },
        * skip({payload}, {call, put}) {
            yield call(skipOrigin, payload);
            window.location.reload();
        },
        * deleteSentence({ payload }, { call, put }) {
            yield call(deleteSentence, payload);
            window.location.reload();
        },
        * count({payload}, {call, put}) {
            const response = yield call(getCount);
            yield put({
                type: 'setCount',
                payload: response,
            });

        },
        * searchResult({payload}, {call, put}) {
            const response = yield call(searchResult, payload);
            yield put({
                type: 'setSearch',
                payload: response,
            });
        },
        * getUserInfo({payload}, {call, put}) {
            const response = yield call(getUserInfo);
            yield put({
                type: 'setUserInfo',
                payload: response,
            });
        }
    },

    reducers: {
        setOrigin(state, action) {
            return {
                ...state,
                origin: action.payload,
            };
        },
        setUserResults(state, action) {
            return {
                ...state,
                labelResults: action.payload.result
            };
        },
        setLabels(state, action) {
            return {
                ...state,
                labels: action.payload,
            };
        },
        setCount(state, action) {
            return {
                ...state,
                count: action.payload,
            };
        },
        setSearch(state, action) {
            return {
                ...state,
                searchResult: action.payload.result,
            };
        },
        setUserInfo(state, action) {
            return {
                ...state,
                userInfo: action.payload,
            };
        },
    },
};
