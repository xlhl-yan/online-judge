package com.xlhl.onlinejudgecodesandbox.codesandbox.unsafe;

/**
 * SleepError
 *
 * @author xlhl
 * @version 1.0
 * @description 无限睡眠演示(阻塞程序执行 ）
 */
public class SleepError {
    public static void main(String[] args) {
        long oneHour = 1000 * 60 * 60;
        try {
            Thread.sleep(oneHour);
            System.out.println("我直接在你服务器里睡觉");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
