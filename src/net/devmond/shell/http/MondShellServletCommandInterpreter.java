/*
 * Copyright (C) 2014 Stefan Moschinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.devmond.shell.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import net.devmond.shell.MondShellCommandProvider;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.Bundle;

public class MondShellServletCommandInterpreter implements CommandInterpreter
{

	private final PrintWriter writer;
	private final Iterator<String> argIterator;
	private final MondShellCommandProvider service;

	MondShellServletCommandInterpreter(HttpServletResponse resp, Collection<String> arguments, MondShellCommandProvider service)
			throws IOException
	{
		this.service = service;
		this.writer = resp.getWriter();
		this.argIterator = arguments.iterator();
	}

	@Override
	public String nextArgument()
	{
		if (argIterator.hasNext())
			return argIterator.next();
		return null;
	}

	@Override
	public Object execute(String cmd)
	{
		try
		{
			return invokeCommand(cmd);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	protected Object invokeCommand(String command) throws Exception
	{
		try
		{
			Method cmd = findCommand("_" + command);
			return cmd.invoke(service, this);
		} catch (InvocationTargetException e)
		{
			if (e.getTargetException() instanceof Exception)
				throw (Exception) e.getTargetException();
			throw (Error) e.getTargetException();
		}
	}

	private Method findCommand(Object commandName)
	{
		for (Method command : MondShellCommandProvider.class.getMethods())
		{
			if (command.getName().equalsIgnoreCase(commandName.toString()))
				return command;
		}
		throw new IllegalArgumentException("Cannot find the command method for: " + commandName);
	}

	@Override
	public void print(Object o)
	{
		writer.print(o);
	}

	@Override
	public void println()
	{
		writer.println();
	}

	@Override
	public void println(Object o)
	{
		writer.println(o);
	}

	@Override
	public void printStackTrace(Throwable t)
	{
		t.printStackTrace(writer);
	}

	@Override
	public void printDictionary(Dictionary<?, ?> dic, String title)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void printBundleResource(Bundle bundle, String resource)
	{
		throw new UnsupportedOperationException();
	}

}
