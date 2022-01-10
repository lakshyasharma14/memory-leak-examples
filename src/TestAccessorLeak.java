import java.io.FilterOutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class TestAccessorLeak {
	
	// private static final WhiteBox WHITE_BOX = WhiteBox.getWhiteBox();

	private static final String WORKER_CLASS_NAME = "WorkerClass";
	
	
	private static long COUNTER = 0;

	public static void main(String[] args) throws Exception {

		while (true) {
			verifyNotLeaked(WORKER_CLASS_NAME + "$leakPublicConstructor");
			verifyNotLeaked(WORKER_CLASS_NAME + "$leakPublicMethod");
			verifyNotLeaked(WORKER_CLASS_NAME + "$leakPublicField");
			verifyNotLeaked(WORKER_CLASS_NAME + "$leakProtectedField");
			verifyNotLeaked(WORKER_CLASS_NAME + "$leakProtectedMethod");
		}
	}

	private static void verifyNotLeaked(String classname) throws Exception {

		WeakReference<?> weakLoader = loadAndRunClass(classname);

		// Make sure we hold all of parent methods from freeing in GC
		Constructor c = Object.class.getConstructor();
		Method m = Thread.class.getDeclaredMethod("currentThread");
		Field f = Thread.class.getDeclaredField("NORM_PRIORITY");
		Method m2 = PrintStream.class.getDeclaredMethod("setError");
		Field f2 = FilterOutputStream.class.getDeclaredField("out");

		// Force garbage collection to trigger unloading of class loader
		// WHITE_BOX.fullGC();

		if (COUNTER == 10000) {
			System.out.println("did it for  " + classname);
			//throw new RuntimeException("Class " + classname + " is leaking!");
			COUNTER = 0L;
		}
		
		COUNTER++;

	}

	private static WeakReference<?> loadAndRunClass(String classname) throws Exception {
		URL url = TestAccessorLeak.class.getProtectionDomain().getCodeSource().getLocation();
		URLClassLoader loader = new URLClassLoader(new URL[] { url }, null);

		// Load worker class with custom class loader
		Class<?> workerClass = Class.forName(classname, true, loader);

		workerClass.getDeclaredMethod("test").invoke(null);

		loader.close();

		return new WeakReference<>(loader);
	}
}