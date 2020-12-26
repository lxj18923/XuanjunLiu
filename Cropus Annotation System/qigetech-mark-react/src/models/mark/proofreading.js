import { getProofreading,saveProofreading } from '@/services/mark/proofreading';


export default {

  namespace: 'proofreading',

  state: {
    proofreading: {}
  },

  effects: {
    *getProofreading({ payload }, { call, put }) {
      const response = yield call(getProofreading);
      yield put({
        type: 'setOrigin',
        payload:  response ,
      });
    },
    *saveProofreading({ payload }, { call, put }) {
      yield call(saveProofreading, payload);
      window.location.reload();
    },
  },

  reducers: {
    setOrigin(state, action) {
      return {
        ...state,
        proofreading: action.payload,
      };
    },
  },
};