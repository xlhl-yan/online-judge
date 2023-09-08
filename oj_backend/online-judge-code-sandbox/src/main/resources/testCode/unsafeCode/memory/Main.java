import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<byte[]> arrayList = new ArrayList<>();
        while (true) {
            arrayList.add(new byte[1000]);
        }
    }
}
