package net.devmond.shell.handler;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InputParser;
import net.devmond.shell.Option;
import net.devmond.shell.Output;
import net.devmond.shell.ParseResult;

import org.eclipse.osgi.framework.console.CommandInterpreter;

abstract class AbstractCommandHandler implements CommandHandler
{

	protected static final Object NO_COMMAND_MATCHES = new Object();

	private final CommandInput cmdInput;
	private final Output output;

	protected AbstractCommandHandler(CommandInterpreter cmdInterpreter)
	{
		this(cmdInterpreter, new Option[0]);
	}

	protected AbstractCommandHandler(CommandInterpreter cmdInterpreter,
			Option... options)
	{
		InputParser inputParser = new InputParser(cmdInterpreter);
		ParseResult result = inputParser.parseCommandInput(options);
		this.cmdInput = result.getCommandInput();
		this.output = result.getOutput();
	}

	@Override
	public final void execute() throws Exception
	{
		try
		{
			Result result = executeInternal(cmdInput);
			if (result.isResultDisplayable())
				output.println(result);
		} finally
		{
			output.close();
		}
	}

	protected abstract Result executeInternal(CommandInput cmdInput) throws Exception;

	@Override
	public CharSequence getHelpText()
	{
		return "No help text";
	}

	// @TestOnly
	CommandInput getCmdInput()
	{
		return cmdInput;
	}

}
