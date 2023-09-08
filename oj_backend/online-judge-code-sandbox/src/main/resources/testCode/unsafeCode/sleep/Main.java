
public class Main {
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
