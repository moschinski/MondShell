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
package net.devmond.shell;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * The activator class controls the plug-in life cycle
 */
public class MondShellActivator extends Plugin
{
	private static final String START_MONDSHELL_SERVLET = "net.devmond.shell.startMondshellServlet";

	private final boolean startMondshellServlet = Boolean.getBoolean(START_MONDSHELL_SERVLET);

	public static final String PLUGIN_ID = "MondShell"; //$NON-NLS-1$

	private static final Collection<String> REQUIRED_SERVLET_BUNDLES = Arrays.asList("org.eclipse.equinox.common",
			"org.eclipse.equinox.http.jetty", "org.eclipse.equinox.http.registry");

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, ?> properties = new Hashtable<String, String>();
		MondShellCommandProvider mondShell = new MondShellCommandProvider();

		context.registerService(CommandProvider.class, mondShell, properties);

		// also register it for use in servlet
		context.registerService(MondShellCommandProvider.class, mondShell, properties);

		startMondshellServletIfRequired(context);
	}

	private void startMondshellServletIfRequired(BundleContext context)
	{
		if (!startMondshellServlet)
		{
			return;
		}
		getLog().log(
				new Status(Status.INFO, PLUGIN_ID, String.format(
						"'%s' is set to 'true - loading required servlet bundles", START_MONDSHELL_SERVLET)));
		try
		{
			enforceStartOfRequiredServletBundles(context.getBundles());
		} catch (BundleException e)
		{
			getLog().log(
					new Status(
							Status.WARNING,
							PLUGIN_ID,
							"Exception while loading required Servlet bundles, MondShell servlet may be not accessible",
							e));
		}
	}

	private void enforceStartOfRequiredServletBundles(Bundle[] bundles) throws BundleException
	{
		// should we consider bundle version?
		for (Bundle bundle : bundles)
		{
			if (REQUIRED_SERVLET_BUNDLES.contains(bundle.getSymbolicName()) && bundle.getState() != Bundle.ACTIVE)
			{
				getLog().log(
						new Status(Status.INFO, PLUGIN_ID,
								String.format("Bundle '%s' is not active yet, starting it programmatically",
										bundle.getSymbolicName())));
				bundle.start();
			}
		}
	}

}
