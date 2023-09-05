package com.xlhl.onlinejudgecodesandbox.codesandbox.security;

import java.security.Permission;

/**
 * DefaultSecurityManager
 *
 * @author xlhl
 * @version 1.0
 * @description 默认Java安全管理器
 */
public class DefaultSecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何权限限制");
        System.out.println(perm);
        super.checkPermission(perm);
    }
}
