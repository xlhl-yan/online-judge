
public class Main {
    public static void main(String[] args) {
        long oneHour = 1000 * 60 * 60;
        try {
            Thread.sleep(oneHour);
            System.out.println("我已睡醒,任务速来");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
