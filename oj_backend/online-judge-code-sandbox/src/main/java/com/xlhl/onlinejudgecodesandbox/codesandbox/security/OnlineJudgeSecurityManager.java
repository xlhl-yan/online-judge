package com.xlhl.onlinejudgecodesandbox.codesandbox.security;

import java.security.Permission;

/**
 * DefaultSecurityManager
 *
 * @author xlhl
 * @version 1.0
 * @description 默认Java安全管理器
 */
public class OnlineJudgeSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
//        super.checkPermission(perm);
    }

    /**
     * 程序是否可执行文件
     *
     * @param cmd
     */
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec禁止执行其他文件:" + cmd);
    }

    /**
     * 程序是否允许读文件
     *
     * @param file
     */
    @Override
    public void checkRead(String file) {
//        System.out.println(file);
//        if (file.contains("C:\\JavaCode\\online-oj\\oj_backend\\online-judge-code-sandbox")) {
//            return;
//        }
//        throw new SecurityException("checkRead禁止读文件:" + file);
    }

    /**
     * 程序是否允许写文件
     *
     * @param file
     * @param file
     */
    @Override
    public void checkWrite(String file) {
//        throw new SecurityException("checkWrite禁止写文件:" + file);
    }

    /**
     * 程序是否允许删除文件
     *
     * @param file
     * @param file
     */
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete禁止删除文件:" + file);
    }

    /**
     * 程序是否允许连接网络
     *
     * @param host
     * @param port
     */
    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect禁止连接网络:" + port + ":" + host);
    }
}
