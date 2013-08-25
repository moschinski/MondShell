package net.devmond.shell;

public final class ParseResult
{

	private final CommandInput commandInput;
	private final Output output;

	public ParseResult(CommandInput commandInput, Output output)
	{
		this.commandInput = commandInput;
		this.output = output;
	}

	public CommandInput getCommandInput()
	{
		return commandInput;
	}

	public Output getOutput()
	{
		return output;
	}

}
