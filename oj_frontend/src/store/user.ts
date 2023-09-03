import { StoreOptions } from "vuex";
import AccessEnum from "@/access/accessEnum";
import { UserControllerService } from "../../generated";
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
    },
  },
  getters,
  actions: {
    async getLoginUser({ commit, state }, payload) {
      /**
       * 从远程请求中获取登录信息
       */
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("updateUser", res.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: AccessEnum.NOT_LOGIN,
        });
      }
    },
  },
  mutations: {
    updateUser(state, payload) {
      // 变更状态
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
