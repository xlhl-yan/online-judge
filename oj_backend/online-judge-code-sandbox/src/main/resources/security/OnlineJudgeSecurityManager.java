
import java.security.Permission;

public class OnlineJudgeSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
//        super.checkPermission(perm);
    }

    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec禁止执行其他文件:" + cmd);
    }

    @Override
    public void checkRead(String file) {
//        System.out.println(file);
//        if (file.contains("C:\\JavaCode\\online-oj\\oj_backend\\online-judge-code-sandbox")) {
//            return;
//        }
//        throw new SecurityException("checkRead禁止读文件:" + file);
    }

    @Override
    public void checkWrite(String file) {
//        throw new SecurityException("checkWrite禁止写文件:" + file);
    }

    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete禁止删除文件:" + file);
    }

    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect禁止连接网络:" + port + ":" + host);
    }
}
