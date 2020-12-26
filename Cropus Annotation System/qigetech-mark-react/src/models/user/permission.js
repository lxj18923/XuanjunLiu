import { queryPermissions, addPermission, updatePermission, deletePermission } from '@/services/user/permission';


export default {

  namespace: 'permission',

  state: {
    permission: {}
  },

  effects: {
    *getPermissionTree({ payload }, { call, put }) {
      const response = yield call(queryPermissions);
      yield put({
        type: 'queryPermissions',
        payload:  response ,
      });
    },
    *addPermission({ payload }, { call, put }) {
      yield call(addPermission, payload);
    },
    *updatePermission({ payload }, { call, put }) {
      yield call(updatePermission, payload);
    },
    *deletePermission({ payload }, { call, put }) {
      yield call(deletePermission, payload);
    },
  },

  reducers: {
    queryPermissions(state, action) {
      return {
        ...state,
        permission: action.payload,
      };
    },
  },
};