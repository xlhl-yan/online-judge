import router from "@/router";
import AccessEnum from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";
import store from "@/store";

router.beforeEach(async (to, from, next) => {
  let loginUser = store.state.user.loginUser;

  // 如果没登录就自动登录
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  const needAccess = (to.meta.access as string) ?? AccessEnum.NOT_LOGIN;
  //  如果不是无需登录页面
  if (needAccess !== AccessEnum.NOT_LOGIN) {
    //  是否登录
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === AccessEnum.NOT_LOGIN
    ) {
      console.log("/user/login");
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    //  登录用户权限不足，跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  next();
});
