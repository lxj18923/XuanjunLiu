import { getList,update,deleteCover } from '@/services/mark/list';


export default {

  namespace: 'markList',

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
    *update({ payload }, { call, put }) {
      console.log(payload);
      yield call(update, payload);
      window.location.reload();
    },
    *cover({ payload }, { call, put }) {
      yield call(deleteCover, payload);
      // window.location.reload();
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
