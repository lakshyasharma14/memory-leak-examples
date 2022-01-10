import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WorkerClass {
	public static class leakPublicConstructor {
		public static void test() throws Exception {
			Constructor c = Object.class.getConstructor();
			c.newInstance();
		}
	}

	public static class leakPublicMethod {
		public static void test() throws Exception {
			Method m = Thread.class.getDeclaredMethod("currentThread");
			m.invoke(null);
		}
	}

	public static class leakPublicField {
		public static void test() throws Exception {
			Field f = Thread.class.getDeclaredField("NORM_PRIORITY");
			f.get(null);
		}
	}

	public static class leakProtectedField extends PrintStream {
		leakProtectedField(OutputStream out) {
			super(out);
		}

		public static void test() throws Exception {
			PrintStream c = new leakProtectedField(new ByteArrayOutputStream());

			Field f = FilterOutputStream.class.getDeclaredField("out");
			f.get(c);
		}
	}

	public static class leakProtectedMethod extends PrintStream {
		leakProtectedMethod(OutputStream out) {
			super(out);
		}

		public static void test() throws Exception {
			PrintStream c = new leakProtectedMethod(new ByteArrayOutputStream());

			Method m = PrintStream.class.getDeclaredMethod("setError");
			m.invoke(c);
		}
	}
}