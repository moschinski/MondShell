package net.devmond.shell.util;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * In {@link NetstatUtil} we had the problem that the
 * {@link Process#getInputStream()} never returns. So this class provides the
 * easy possibility to wait for the input stream of the process using a timeout.
 */
public class ProcessExecutor
{

	private final String[] args;
	private final Runtime runtime;

	public ProcessExecutor(Runtime runtime, String[] args)
	{
		this.args = args;
		this.runtime = runtime;
	}

	/**
	 * 
	 * @param timeout
	 *            defines how long the method should wait until a result is
	 *            computed, if the timout is exceeded a {@link TimeoutException}
	 *            is thrown
	 * @param unit
	 *            {@link TimeUnit} of the timeout
	 * @return the input stream of the spawned process as a {@link String}
	 * @throws InterruptedException
	 *             if the Thread waiting for a result is interrupted
	 * @throws ExecutionException
	 *             if an exception during spawning the subprocess and getting
	 *             its input stream happens
	 * @throws TimeoutException
	 *             if the {@code timeout} is exceeded
	 */
	public String getInputAsString(int timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
			TimeoutException
	{
		Future<String> result = newSingleThreadExecutor().submit(new Callable<String>()
		{

			@Override
			public String call() throws Exception
			{
				Process proc = runtime.exec(args);
				try
				{
					return IOUtils.readStream(proc.getInputStream());
				} finally
				{
					proc.destroy();
				}
			}
		});

		return result.get(timeout, unit);
	}
}
