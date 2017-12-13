package task4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Main {


    public static void main(String[] args) {
        ArraySummator.array = new int[1024];

        long summ = 0;
        for(int i = 0; i < ArraySummator.array.length; i++) {
            ArraySummator.array[i] = (int)(Math.random() * 100);
            summ += ArraySummator.array[i];
        }

        System.out.println("Check summ ="  + summ);

        ArraySummator arraySummator = new ArraySummator(0,ArraySummator.array.length);
        System.out.println("Summ =" + arraySummator.compute());
    }

    static class ArraySummator extends RecursiveTask<Long> {
        static int[] array;
        int THREAD_POOL_SIZE = 4;
        ForkJoinPool pool = new ForkJoinPool(THREAD_POOL_SIZE);
        int from;
        int to;

        public ArraySummator(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if((to - from) <= array.length / THREAD_POOL_SIZE) {
                long partSumm = 0;
                for(int i = from; i < to; i++) {
                    partSumm += array[i];
                }
                System.out.println("Parts summ : " + partSumm);
                return partSumm;
            } else {
                int mid = (from + to) / 2;
                ArraySummator summator1 = new ArraySummator(mid + 1, to);
                ArraySummator summator2 = new ArraySummator(from, mid + 1);
                summator1.fork();
                summator2.fork();
                return summator1.join() + summator2.join();
            }
        }
    }
}
