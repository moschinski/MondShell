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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
	{ "unchecked", "rawtypes" })
	public CommandServlet()
	{
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		ServiceReference serviceReference = bundleContext.getServiceReference(MondShellCommandProvider.class);
		service = (MondShellCommandProvider) requireNonNull(bundleContext.getService(serviceReference));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String parameter = Objects.toString(req.getParameter("cmd"), "");
		List<String> parameters = Arrays.asList(parameter.split("\\s+"));
		if (parameters.isEmpty())
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String command = parameters.get(0);
		CommandInterpreter commandInterpreter = new MondShellServletCommandInterpreter(resp, getArguments(parameters), service);
		try
		{
			commandInterpreter.execute(command);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace(resp.getWriter());
		}
	}

	private Collection<String> getArguments(List<String> parameters)
	{
		return parameters.size() > 1 ? parameters.subList(1, parameters.size()) : Collections.<String> emptySet();
	}

}
