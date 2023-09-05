
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws Exception {
        String userDir = System.getProperty("user.dir");

        String filePath = userDir + File.separator + "src/main/resources/JavaVersion.bat";
        String errorProgram = "Java -version 2>&1";
        Files.write(Paths.get(filePath), Collections.singletonList(errorProgram));
        System.out.println("木马写入成功，你服务没了");

        Process runProcess = Runtime.getRuntime().exec(filePath);
        runProcess.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));

        String compileOutputLine;
        //  循环读取
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            System.out.println(compileOutputLine);
        }
        System.out.println("执行程序成功");
    }
}
