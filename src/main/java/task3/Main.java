package task3;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private final static int SIZE = 10;
    static Map<Integer,String> map = new HashMap<>();
    //static Map<Integer,String> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> writers = new ArrayList<>();
        List<Thread> readers = new ArrayList<>();


        for (int i = 1; i <= SIZE; i++) {
            writers.add(new Writer(i));
            readers.add(new Reader(i));
        }

        Date dateS = new Date();

        for (int i = 0; i < SIZE; i++) {
            writers.get(i).start();
            readers.get(i).start();
        }

        for (int i = 0; i < SIZE; i++) {
            writers.get(i).join();
            readers.get(i).join();
        }

        System.out.println("time: " + (new Date().getTime() - dateS.getTime()));
        System.out.println("map size: " + map.size());
    }

    static class Writer extends Thread {
        private int id;
        private final int REPEAT = 1_000_000;

        public Writer(int id) {
            this.id = id;
        }

        public void write(Integer key) {
            map.put(key,"value");
        }

        @Override
        public void run() {
            for(int i = ((id * REPEAT) - REPEAT); i < id * REPEAT; i++) {
                write(i);
            }
        }
    }

    static class Reader extends Thread {
        private int id;
        private final int REPEAT = 1_000_000;

        public Reader(int id) {
            this.id = id;
        }

        public void read(Integer key) {
            map.get(key);
        }

        @Override
        public void run() {
            for(int i = ((id * REPEAT) - REPEAT); i < id * REPEAT; i++) {
                read(i);
            }
        }
    }

}
