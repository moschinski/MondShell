package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;
import net.devmond.shell.CommandInput;
import net.devmond.shell.util.NetstatUtil;
import net.devmond.shell.util.NetstatUtil.Portuse;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class FreeportCommandHandler extends AbstractCommandHandler
{

	private final NetstatUtil netstatUtil;

	public FreeportCommandHandler(CommandInterpreter cmdInterpreter, NetstatUtil netstatutil)
	{
		super(cmdInterpreter);
		this.netstatUtil = netstatutil;
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		int port = Integer.valueOf(cmdInput.nextArgument());
		Portuse process = netstatUtil.findProcessForPort(port);
		if (process.getProcessPid() > 0)
		{
			return textResult(WinKillCommandHandler.killProcess(String.valueOf(process.getProcessPid())));
		}
		return textResult("No process found for port: " + port);
	}

}
