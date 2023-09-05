package com.xlhl.onlinejudgecodesandbox.codesandbox.unsafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * WriteFileError
 *
 * @author xlhl
 * @version 1.0
 * @description 修改服务器文件（植入危险争取）
 */
public class WriteFileError {
    public static void main(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir");

        String filePath = userDir + File.separator + "src/main/resources/JavaVersion.bat";
        String errorProgram = "Java -version 2>&1";
        Files.write(Paths.get(filePath), Collections.singletonList(errorProgram));
        System.out.println("木马写入成功，你服务没了");
    }
}
