/*
 * Copyright (C) 2013 Stefan Moschinski
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
import java.util.Dictionary;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.Bundle;

public class ServletCommandInterpreter implements CommandInterpreter
{

	private final HttpServletResponse resp;
	private final PrintWriter writer;
	private final Iterator<String> argIterator;

	ServletCommandInterpreter(HttpServletResponse resp, Iterable<String> arguments) throws IOException
	{
		this.resp = resp;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printBundleResource(Bundle bundle, String resource)
	{
		// TODO Auto-generated method stub

	}

}
