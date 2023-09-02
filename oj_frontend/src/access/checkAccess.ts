import AccessEnum from "@/access/accessEnum";

/**
 * 检查权限 判断用户是否有指定的权限
 * @param loginUser 登录用户
 * @param needAccess 需要有的权限
 * @return boolean true ? "有" : "无"
 */
const checkAccess = (loginUser: any, needAccess = AccessEnum.NOT_LOGIN) => {
  // 获取当前用户具有的权限
  const loginUserRole = loginUser?.userRole ?? AccessEnum.NOT_LOGIN;
  //  不需要用户登录可访问
  if (needAccess === AccessEnum.NOT_LOGIN) {
    return true;
  }
  //  需要登录
  if (needAccess === AccessEnum.USER) {
    if (loginUserRole === AccessEnum.NOT_LOGIN) {
      return false;
    }
  }
  //  需要管理员
  if (needAccess === AccessEnum.ADMIN) {
    if (loginUserRole !== AccessEnum.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
