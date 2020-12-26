import { getList,update,getOrigin,save } from '@/services/triad/triad';


export default {

  namespace: 'triad',

  state: {
    list: {},
    origin:{}
  },

  effects: {
    *getList({ payload }, { call, put }) {
      const response = yield call(getList, payload);
      yield put({
        type: 'setList',
        payload:  response ,
      });
    },
    *getOrigin({ payload }, { call, put }) {
      const response = yield call(getOrigin, payload);
      yield put({
        type: 'setOrigin',
        payload:  response ,
      });
    },
    *save({ payload }, { call, put }) {
      console.log(payload);
      yield call(save, payload);
      window.location.reload();
    },
    *update({ payload }, { call, put }) {
      console.log(payload);
      yield call(update, payload);
      window.location.reload();
    },
  },

  reducers: {
    setList(state, action) {
      return {
        ...state,
        list: action.payload,
      };
    },
    setOrigin(state, action) {
      return {
        ...state,
        origin: action.payload,
      };
    },
  },
};