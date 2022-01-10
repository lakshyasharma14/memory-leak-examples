import java.util.ArrayList;
import java.util.List;

public class Leak1 {
    public static List<Double> list = new ArrayList<>();

    static volatile boolean running = true;


    public static void populateList() {
        for (int i = 0; i < 2000; i++) {
            list.add(Math.random());
        }
    }

    static final class RegularThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {

                    MyStack s = new MyStack();
                    while (true) {
                        for (int i = 0; i < 10; i++) {
                            s.push(new Light());
                        }
                        for (int i = 0; i < 10; i++) {
                            s.pop();
                        }
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println("Caught InterruptedException, shutting down.");
                    running = false;
                }
            }
        }
    }

    static final class Thread1 extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    Leak1.populateList();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println("Caught InterruptedException, shutting down.");
                    running = false;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Thread regularThread = new RegularThread();
        Thread1 thread1 = new Thread1();

        try {
            new Leak1().populateList();
            regularThread.start();
            thread1.start();
            System.out.println("Running, press any key to stop.");
            System.in.read();
        } finally {
            running = false;
            regularThread.join();
            thread1.join();
        }

    }
}