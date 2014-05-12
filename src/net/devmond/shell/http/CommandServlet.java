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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.devmond.shell.MondShellCommandProvider;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class CommandServlet extends HttpServlet
{
	private static final long serialVersionUID = 7953717768106412018L;

	private final MondShellCommandProvider service;

	@SuppressWarnings(
	/* because of problems with Java 7 */{ "unchecked", "rawtypes" })
	public CommandServlet()
	{
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		ServiceReference serviceReference = bundleContext.getServiceReference(MondShellCommandProvider.class);
		service = (MondShellCommandProvider) bundleContext.getService(serviceReference);
	}

	/*
	 * Not a best practice, but to simplify the integration we offer also a get
	 * method
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String parameter = req.getParameter("cmd");
		List<String> paramters = Arrays.asList(parameter.split("\\s+"));
		if (paramters.isEmpty())
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String command = paramters.get(0);
		CommandInterpreter commandInterpreter = new ServletCommandInterpreter(resp, getArguments(paramters));
		try
		{
			invokeCommand(command, commandInterpreter);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private Collection<String> getArguments(List<String> split)
	{
		return split.size() > 1 ? split.subList(1, split.size()) : Collections.<String> emptySet();
	}

	public Object invokeCommand(String command, CommandInterpreter commandInterpreter) throws Exception
	{
		try
		{
			Method cmd = findCommand("_" + command);
			return cmd.invoke(service, commandInterpreter);
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
}
