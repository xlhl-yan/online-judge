import { StoreOptions } from "vuex";
import AccessEnum from "@/access/accessEnum";
// state
// 存储的信息（比如用户信息）

// getters  获取用户信息
const getters = {};

// mutations
// （sync）：定义了对变量的增删改（更新）的方法

// actions
// （async）：执行异步操作，并且触发 mutations 的更改（actions 调用 mutations）

export default {
  namespaced: true,
  state: {
    loginUser: {
      userName: "未登录",
      userRole: AccessEnum.NOT_LOGIN,
    },
  },
  getters,
  actions: {
    getLoginUser({ commit, state }, payload) {
      // todo 改为远程登录 获取用户信息
      commit("updateUser", payload);
    },
  },
  mutations: {
    updateUser(state, payload) {
      // 变更状态
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
