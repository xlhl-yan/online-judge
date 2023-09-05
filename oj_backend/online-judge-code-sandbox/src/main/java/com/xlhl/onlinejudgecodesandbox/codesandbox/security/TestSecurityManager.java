package com.xlhl.onlinejudgecodesandbox.codesandbox.security;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * TestSecurityManager
 *
 * @author xlhl
 * @version 1.0
 * @description Java安全管理器测试
 */
public class TestSecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new OnlineJudgeSecurityManager());

        File file = FileUtil.writeString("aa", "aaa", StandardCharsets.UTF_8);

    }
}
