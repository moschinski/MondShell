package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;

import java.io.IOException;

import javax.script.ScriptException;

import net.devmond.shell.CommandInput;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class CalculateCommandHandler extends JavaScriptBasedCommandHandler {

	public CalculateCommandHandler(CommandInterpreter cmdInterpreter) throws ScriptException, IOException
	{
		super(cmdInterpreter);
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		 StringBuilder calculation = new StringBuilder();
		
		 while (cmdInput.hasNextArgument())
			 calculation.append(cmdInput.nextArgument());
		 
		return textResult(evalJs(calculation.toString()));
	}
	
	
}
