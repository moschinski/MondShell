package net.devmond.shell;

import static net.devmond.shell.handler.StringCommandHandler.Mode.UPPER_CASE;
import net.devmond.shell.handler.CalculateCommandHandler;
import net.devmond.shell.handler.DecodeCommandHandler;
import net.devmond.shell.handler.EncodeCommandHandler;
import net.devmond.shell.handler.FormatCommandHandler;
import net.devmond.shell.handler.FreeportCommandHandler;
import net.devmond.shell.handler.WinKillCommandHandler;
import net.devmond.shell.handler.PortuseCommandHandler;
import net.devmond.shell.handler.RandomCommandHandler;
import net.devmond.shell.handler.RefreshCommandHandler;
import net.devmond.shell.handler.RegexCommandHandler;
import net.devmond.shell.handler.StringCommandHandler;
import net.devmond.shell.util.NetstatUtil;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

public class MondShellCommandProvider implements CommandProvider
{

	@Override
	public String getHelp()
	{
		return null;
	}

	public void _match(CommandInterpreter ci) throws Exception
	{
		new RegexCommandHandler(ci).execute();
	}

	public void _calc(CommandInterpreter ci) throws Exception
	{
		// does currently not work with '(' and ')' ==> escaping necessary '\'
		new CalculateCommandHandler(ci).execute();
	}

	public void _refresh(CommandInterpreter ci) throws Exception
	{
		new RefreshCommandHandler(ci).execute();
	}

	public void _string(CommandInterpreter ci) throws Exception
	{
		StringCommandHandler.createByMode(ci, UPPER_CASE).execute();
	}

	public void _encode(CommandInterpreter ci) throws Exception
	{
		new EncodeCommandHandler(ci).execute();
	}

	public void _decode(CommandInterpreter ci) throws Exception
	{
		new DecodeCommandHandler(ci).execute();
	}

	public void _format(CommandInterpreter ci) throws Exception
	{
		new FormatCommandHandler(ci).execute();
	}

	public void _random(CommandInterpreter ci) throws Exception
	{
		new RandomCommandHandler(ci).execute();
	}

	public void _portuse(CommandInterpreter ci) throws Exception
	{
		NetstatUtil netstatUtil = new NetstatUtil(Runtime.getRuntime());
		new PortuseCommandHandler(ci, netstatUtil).execute();
	}

	public void _freeport(CommandInterpreter ci) throws Exception
	{
		NetstatUtil netstatUtil = new NetstatUtil(Runtime.getRuntime());
		new FreeportCommandHandler(ci, netstatUtil).execute();
	}

	public void _kill(CommandInterpreter ci) throws Exception
	{
		new WinKillCommandHandler(ci).execute();
	}

}
