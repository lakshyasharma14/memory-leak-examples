import java.util.StringTokenizer;

public class Leak3 {
    public native void displayHelloWorld();

    static {
        System.loadLibrary("leak");
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            new Leak3().displayHelloWorld();
            Thread.sleep(100);
        }
    }
}