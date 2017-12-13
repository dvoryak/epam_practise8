package task5;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static StringBuffer sb = new StringBuffer();

    public static void main(String[] args) throws InterruptedException {
        FileWalker fileWalker = new FileWalker(Paths.get("/maven"));

        while (FileWalker.pool.getActiveCount() != 0) {
            Thread.sleep(50);
        }
        for (String s : FileWalker.result) {
            System.out.println(s);
        }
        FileWalker.pool.shutdown();
    }


    static class FileWalker extends Thread {
        public static volatile ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        public static volatile List<String> result = new CopyOnWriteArrayList<>();
        Path path;
        int count = 0;
        String startWith = "a";

        public FileWalker() {
        }

        public FileWalker(Path path) {
            this.path = path;
            pool.execute(this);
        }


        public void getContent(Path path) throws IOException {
            if(Files.isDirectory(path)) {
                List<Path> collect = Files.list(path).collect(Collectors.toList());
                for (Path p : collect) {
                    if(Files.isRegularFile(p) && (p.toString().endsWith(".txt") || p.toString().endsWith(".xml") || p.toString().endsWith(".license"))) {
                        Files.lines(p).forEach((line)->{
                            String[] split = line.split(" ");
                            for (String s : split) {
                                if(s.startsWith(startWith)) {
                                    count++;
                                }
                            }
                        });
                        result.add("Thread name=" + currentThread().getName() + ", file name=" + p.getFileName() +
                                ", count of words which starts with " + startWith + "= " + count);
                        count = 0;
                    } else if(Files.isDirectory(p)) {
                        new FileWalker(p);
                    }
                }
            } else throw new IllegalArgumentException("File is not a directory");
        }

        public void setStartWith(String startWith) {
            this.startWith = startWith;
        }

        @Override
        public void run() {
            try {
                getContent(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
