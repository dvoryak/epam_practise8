package task1;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            int i = 10;
            while(i > 0) {
                System.out.println(i--);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Bomb!");
        }).start();
        System.out.println("+");

    }
}
