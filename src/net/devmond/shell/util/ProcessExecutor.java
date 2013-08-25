package net.devmond.shell.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;



/**
 * In {@link NetstatUtil} we had the problem that the {@link Process#getInputStream()} never returns.
 * So this class provides the easy possibility to wait for the input stream of the process using a timeout.
 * 
 * @author stefan.moschinski
 */
public class ProcessExecutor {

	private final String[] args;
	private final Runtime runtime;

	public ProcessExecutor(Runtime runtime, String[] args) {
		this.args = args;
		this.runtime = runtime;
	}

	/**
	 * 
	 * @param timeout defines how long the method should wait until a result is computed, if the timout is exceeded a
	 *        {@link TimeoutException} is thrown
	 * @param unit {@link TimeUnit} of the timeout
	 * @return the input stream of the spawned process as a {@link String}
	 * @throws InterruptedException if the Thread waiting for a result is interrupted
	 * @throws ExecutionException if an exception during spawning the subprocess and getting its input stream happens
	 * @throws TimeoutException if the {@code timeout} is exceeded
	 * @author stefan.moschinski
	 */
	public String getInputAsString(int timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executor = newProcessExecutor();
		final AtomicReference<Process> process = new AtomicReference<Process>(null);
		Future<String> result = executor.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				process.set(runtime.exec(args));
				return IOUtils.toString(process.get().getInputStream());
			}
		});

		try {
			return result.get(timeout, unit);
		} finally {
			if (process.get() != null) {
				process.get().destroy();
			}
		}
	}

	private ExecutorService newProcessExecutor() {
		return Executors.newSingleThreadExecutor();
	}
}
