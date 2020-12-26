import { queryRoles, getRoleById, getPermissionTreeByType, updateRole, addRole, deleteRole } from '@/services/user/role';


export default {

  namespace: 'role',

  state: {
    roles: {},
    role: {},
    permission: {}
  },

  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryRoles);
      yield put({
        type: 'queryRoles',
        payload:  response ,
      });
    },
    *getById({ payload }, { call, put }) {
      const response = yield call(getRoleById, payload);
      yield put({
        type: 'getRole',
        payload:  response ,
      });
    },
    *getPermissionTreeByType({ payload }, { call, put }) {
      const response = yield call(getPermissionTreeByType, payload);
      yield put({
        type: 'getPermissionTree',
        payload:  response ,
      });
    },
    *addRole({ payload }, { call, put }) {
      yield call(addRole, payload);
    },
    *updateRole({ payload }, { call, put }) {
      yield call(updateRole, payload);
    },
    *deleteRole({ payload }, { call, put }) {
      yield call(deleteRole, payload);
    },
  },

  reducers: {
    queryRoles(state, action) {
      return {
        ...state,
        roles: action.payload,
      };
    },
    getRole(state, action) {
      return {
        ...state,
        role: action.payload,
      };
    },
    getPermissionTree(state, action) {
      return {
        ...state,
        permission: action.payload,
      };
    },
  },
};