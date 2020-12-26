import { getList,submitComment } from '@/services/comment/comment';


export default {

  namespace: 'comment',

  state: {
    list: {}
  },

  effects: {
    *getList({ payload }, { call, put }) {
      const response = yield call(getList, payload);
      yield put({
        type: 'setList',
        payload:  response ,
      });
    },
    *submitComment({ payload }, { call, put }) {
      yield call(submitComment, payload);
    },
  },

  reducers: {
    setList(state, action) {
      return {
        ...state,
        list: action.payload,
      };
    },
  },
};