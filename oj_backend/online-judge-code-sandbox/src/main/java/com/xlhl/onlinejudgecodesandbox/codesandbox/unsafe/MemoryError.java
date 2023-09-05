package com.xlhl.onlinejudgecodesandbox.codesandbox.unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * MemoryError
 *
 * @author xlhl
 * @version 1.0
 * @description 无限占用空间（浪费系统内存）
 */
public class MemoryError {
    public static void main(String[] args) {
        List<byte[]> arrayList = new ArrayList<>();
        while (true) {
            arrayList.add(new byte[1000]);
        }
    }
}
