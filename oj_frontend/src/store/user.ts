import { StoreOptions } from "vuex";
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
      username: "未登录",
      role: "notLogin",
    },
  },
  getters,
  actions: {
    getLoginUser({ commit, state }, payload) {
      // todo 改为远程登录 获取用户信息
      commit("updateUser", { username: "Helena" });
    },
  },
  mutations: {
    updateUser(state, payload) {
      // 变更状态
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
