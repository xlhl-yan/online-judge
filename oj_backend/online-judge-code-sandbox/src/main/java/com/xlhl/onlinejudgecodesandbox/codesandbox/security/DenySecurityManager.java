package com.xlhl.onlinejudgecodesandbox.codesandbox.security;

import java.security.Permission;

/**
 * DefaultSecurityManager
 *
 * @author xlhl
 * @version 1.0
 * @description 禁止全部权限的 Java 安全管理器
 */
public class DenySecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {

        throw new SecurityException("不满足权限" + perm.getActions());
    }

}
