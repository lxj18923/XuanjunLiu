import { queryUsers, queryCurrent, getUserById, getRoles, getRoleByUserId, addUser, updateUser, deleteUser } from '@/services/user/user';

export default {
  namespace: 'user',

  state: {
    users: [],
    currentUser: {},
    user: {},
    role: {},
    roles: {}
  },

  effects: {
    *fetch({payload}, { call, put }) {
      const response = yield call(queryUsers,payload);
      yield put({
        type: 'save',
        payload: response,
      });
    },
    *add({payload}, { call, put }) {
      yield call(addUser,payload);
    },
    *update({payload}, { call, put }) {
      yield call(updateUser,payload);
    },
    *delete({payload}, { call, put }) {
      yield call(deleteUser,payload);
    },
    *getUserById({payload}, { call, put }) {
      const response = yield call(getUserById,payload);
      yield put({
        type: 'getById',
        payload: response,
      });
    },
    *getRoleByUserId({payload}, { call, put }) {
      const response = yield call(getRoleByUserId,payload);
      yield put({
        type: 'setRole',
        payload: response,
      });
    },
    *getRoles(_, { call, put }) {
      const response = yield call(getRoles);
      yield put({
        type: 'getAllRoles',
        payload: response,
      });
    },
  },

  reducers: {
    save(state, action) {
      return {
        ...state,
        users: action.payload,
      };
    },
    getById(state, action) {
      return {
        ...state,
        user: action.payload,
      };
    },
    getAllRoles(state, action) {
      return {
        ...state,
        roles: action.payload,
      };
    },
    setRole(state, action) {
      return {
        ...state,
        role: action.payload,
      };
    },
  },
};
