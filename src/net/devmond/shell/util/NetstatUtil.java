package net.devmond.shell.util;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * TODO ADAPT IT FOR *NIX
 * 
 * 
 * Uses the netstat tool to check which process is using a specific port. Works
 * currently only on Windows machines.
 * 
 * 
 * @author stefan.moschinski
 */
public class NetstatUtil {
	private static final Logger log = Logger.getLogger(NetstatUtil.class.getName());

	private static final int NETSTAT_RESPONSE_TIMEOUT = 5;
	private static final String[] NETSTAT_CMD = new String[] { "netstat", "-abno" };
	private static final String PROCESS_REGEX = "(\\[[\\.\\w]+\\]|Can not obtain ownership information)";
	private static final Pattern PROCESS_PATTERN = Pattern.compile(PROCESS_REGEX, Pattern.CASE_INSENSITIVE);

	private static final String IP_REGEX = "[a-f0-9:.]+";
	
	private final Runtime runtime;


	/**
	 * Creates a new {@link NetstatUtil} instance
	 * 
	 * @param runtime the {@link Runtime} instance to use, must not be <code>null</code>
	 * @throws NullPointerException if the passed runtime is null
	 * @author stefan.moschinski
	 */
	public NetstatUtil(Runtime runtime) {
		if (runtime == null)
			throw new NullPointerException("The runtime must not be null");
		this.runtime = runtime;

	}

	/**
	 * 
	 * @param port port to check whether it is used by a process
	 * @return the process name that is using the port or <code>null</code> if no process was found, the used OS is not Windows, or Netstat
	 * checking was disabled by 
	 * @author stefan.moschinski
	 */
	public Portuse findProcessForPort(int port)
	{
		String netstatResp = getNetstatResponse(NETSTAT_RESPONSE_TIMEOUT, SECONDS);
		if (netstatResp == null)
			return null;

		Pattern portRegex = Pattern.compile("(TCP|UDP|TCPv6|UDPv6)\\s+"
				+ IP_REGEX + ":" + port + "\\s+.*\\s+(\\d+)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = portRegex.matcher(netstatResp);

		if (matcher.find()) {
			int pid = Integer.valueOf(matcher.group(2));
			Matcher subMatcher = PROCESS_PATTERN.matcher(netstatResp);
			if (subMatcher.find(matcher.end())) {
				String processName = subMatcher.group(0);

				processName = StringUtils.removeStart(processName, "[");
				processName = StringUtils.removeEnd(processName, "]");

				return new Portuse(processName, pid);
			}
		}

		return null;
	}

	private String getNetstatResponse(int timeout, TimeUnit unit) {
		String netstatResp = null;
		try {
			netstatResp = getNetstatRespInternal(timeout, unit);
		} catch (IOException e) {
			log.log(Level.WARNING, String.format("An exeption happened executing '%s'", Arrays.toString(NETSTAT_CMD)), e);
		} catch (InterruptedException e) {
			log.log(Level.WARNING, String.format("The execution of '%s' was interrupted", Arrays.toString(NETSTAT_CMD)), e);
		} catch (ExecutionException e) {
			log.log(Level.WARNING, String.format("An exeption happened executing '%s'", Arrays.toString(NETSTAT_CMD)),
					e.getCause());
		} catch (TimeoutException e) {
			log.log(Level.WARNING, String.format("The execution of '%s' did not return in time", Arrays.toString(NETSTAT_CMD)), e);
		}
		return netstatResp;
	}


	private String getNetstatRespInternal(int timeout, TimeUnit unit) throws IOException, InterruptedException,
			ExecutionException,
			TimeoutException {
		ProcessExecutor netstatExec = new ProcessExecutor(runtime, NETSTAT_CMD);
		return netstatExec.getInputAsString(timeout, unit);
	}

	public static class Portuse
	{
		public static final Portuse NO_PORT_USE_FOUND = new Portuse(null, -1);

		private final String processName;
		private final int pid;

		public Portuse(String processName, int pid)
		{
			this.processName = processName;
			this.pid = pid;
		}

		@Override
		public String toString()
		{
			return "[PROCESS]: " + processName + "     [PID]: " + pid;
		}

		public String getProcessName()
		{
			return processName;
		}

		public int getProcessPid()
		{
			return pid;
		}

	}

}