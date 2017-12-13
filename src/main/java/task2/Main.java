package task2;

public class Main {
    static Value var = new Value();

    public static void main(String[] args) throws InterruptedException {
        Thread counter = new Counter();
        Thread printer = new Printer();
        counter.start();
        printer.start();
    }

    static class Counter extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                try {
                    var.setVal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    static class Printer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                try {
                    var.printVal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class Value {
        Integer val = 0;
        boolean flag;

        public synchronized void setVal() throws InterruptedException {
            if (flag) {
                wait();
            }
            val++;
            flag = true;
            notify();
        }

        public synchronized void printVal() throws InterruptedException {
            if (!flag) {
                wait();
            }
            System.out.println(val);
            flag = false;
            notify();
        }
    }

}
