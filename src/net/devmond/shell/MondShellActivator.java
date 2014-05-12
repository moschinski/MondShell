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
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * The activator class controls the plug-in life cycle
 */
public class MondShellActivator extends Plugin
{
	public static final String PLUGIN_ID = "MondShell"; //$NON-NLS-1$

	private static final Collection<String> ENFORCE_START_BUNDLES = Arrays.asList("org.eclipse.equinox.common",
			"org.eclipse.equinox.http.jetty", "org.eclipse.equinox.http.registry");

	@SuppressWarnings("unused")
	private static MondShellActivator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		Hashtable<String, ?> properties = new Hashtable<String, String>();
		MondShellCommandProvider mondShell = new MondShellCommandProvider();

		context.registerService(CommandProvider.class, mondShell, properties);

		// also register it for use in servlet
		context.registerService(MondShellCommandProvider.class, mondShell, properties);

		enforceStartOfRequiredBundles(context.getBundles());
	}

	private void enforceStartOfRequiredBundles(Bundle[] bundles)
	{
		// should we consider bundle version?
		for (Bundle bundle : bundles)
		{
			if (ENFORCE_START_BUNDLES.contains(bundle.getSymbolicName()) && bundle.getState() != Bundle.ACTIVE)
			{
				System.out.println("Starting " + bundle.getSymbolicName() + " programatically");
				try
				{
					bundle.start();
				} catch (BundleException e)
				{
					e.printStackTrace();
				}
			}

			// install the bundles that are missing?
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}	

}
