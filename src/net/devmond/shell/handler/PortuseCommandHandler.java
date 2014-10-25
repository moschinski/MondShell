package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;
import net.devmond.shell.CommandInput;
import net.devmond.shell.util.NetstatUtil;
import net.devmond.shell.util.NetstatUtil.Portuse;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class PortuseCommandHandler extends AbstractCommandHandler {

	private final NetstatUtil netstatUtil;

	public PortuseCommandHandler(CommandInterpreter cmdInterpreter,
			NetstatUtil netstatUtil)
	{
		super(cmdInterpreter);
		this.netstatUtil = netstatUtil;
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		// actually the port has be given --> exception?
		int port = cmdInput.hasNextArgument() ? Integer.valueOf(cmdInput
				.nextArgument()) : -1;

		Portuse process = netstatUtil.findProcessForPort(port);
		return textResult(process == null ? String.format("Port %s is not used", port) : process);
	}

}

