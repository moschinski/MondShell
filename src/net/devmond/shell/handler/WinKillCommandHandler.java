package net.devmond.shell.handler;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.devmond.shell.handler.ResultMaker.textResult;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;
import net.devmond.shell.util.ProcessExecutor;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class WinKillCommandHandler extends AbstractCommandHandler
{
	private static final String[] WIN_KILL = new String[]
	{ "taskkill", "/F", "/PID" };

	public WinKillCommandHandler(CommandInterpreter cmdInterpreter)
	{
		super(cmdInterpreter);
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		String pid = cmdInput.nextArgument();
		return textResult(killProcess(pid));
	}

	static Object killProcess(String pid) throws InterruptedException, ExecutionException, TimeoutException
	{
		String[] cmdarray = getKillCommand(pid);
		ProcessExecutor processExecutor = new ProcessExecutor(Runtime.getRuntime(), cmdarray);
		return processExecutor.getInputAsString(1, SECONDS);
	}

	private static String[] getKillCommand(String pid)
	{
		if (!pid.matches("\\d+"))
		{
			throw new InvalidCommandException("The found PID is not valid, it is: " + pid);
		}

		String[] cmdarray = Arrays.copyOf(WIN_KILL, WIN_KILL.length + 1);
		cmdarray[cmdarray.length - 1] = pid;
		return cmdarray;
	}


}
