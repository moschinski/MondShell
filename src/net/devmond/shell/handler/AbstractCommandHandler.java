/*
 * Copyright (C) 2013 Stefan Moschinski
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.devmond.shell.handler;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InputParser;
import net.devmond.shell.Option;
import net.devmond.shell.Output;
import net.devmond.shell.ParseResult;

import org.eclipse.osgi.framework.console.CommandInterpreter;

abstract class AbstractCommandHandler implements CommandHandler
{
	private final CommandInput cmdInput;
	private final Output output;

	protected AbstractCommandHandler(CommandInterpreter cmdInterpreter)
	{
		this(cmdInterpreter, new Option[0]);
	}

	protected AbstractCommandHandler(CommandInterpreter cmdInterpreter,
			Option... supportedOptions)
	{
		InputParser inputParser = new InputParser(cmdInterpreter);
		ParseResult result = inputParser.parseCommandInput(supportedOptions);
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
