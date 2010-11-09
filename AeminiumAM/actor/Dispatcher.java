package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public class Dispatcher {
	static Object object;

	public static void dispatcToAM(final Actor actor, final String name,
			final Object msg) {
		final Method m;
		final Class<?> c = actor.getClass();

		if ((m = checkMethod(c, name)) != null) {

			if (methodCanBeParallelized(c, m)) {
				System.out.println("going to be par from dispatcher");

				Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current)
							throws Exception {
						try {
							Object t = c.newInstance();

							try {
								m.setAccessible(true);

								m.invoke(t, msg);

								// Handle any exceptions thrown by method to be
								// invoked.
							} catch (InvocationTargetException x) {
								x.getCause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, Runtime.NO_HINTS);

				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						Runtime.NO_DEPS);

			} else {
				System.out
						.println("is going to be an Atomic task from dispatcher");

				/* Useless Datagroup created to pass as arg in createAtomicTask */
				DataGroup dg = AeminiumRuntime.rt.createDataGroup();

				Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current)
							throws Exception {
						try {
							Object t = c.newInstance();

							try {
								m.setAccessible(true);

								m.invoke(t, msg);

								// Handle any exceptions thrown by method to be
								// invoked.
							} catch (InvocationTargetException x) {
								x.getCause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, dg, Runtime.NO_HINTS);

				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						Runtime.NO_DEPS);
			}
		}
	}

	private static boolean methodCanBeParallelized(Class<?> c, Method m) {

		if (m.getAnnotations().length == 0) {
			return false;
		}

		for (Annotation an : m.getAnnotations()) {
			if (an instanceof readOnly && ((readOnly) an).isReadOnly() == false) {
				return false;
			}
		}

		return true;
	}

	private static Method checkMethod(Class<?> c, String name) {

		Method[] allMethods = c.getDeclaredMethods();
		for (Method m : allMethods) {
			if (m.getName() == name)
				return m;
		}

		return null;
	}

}
