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

import java.util.Hashtable;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MondShellActivator extends Plugin
{
	public static final String PLUGIN_ID = "MondShell"; //$NON-NLS-1$

	@SuppressWarnings("unused")
	private static MondShellActivator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		Hashtable<String, ?> properties = new Hashtable<String, String>();
		MondShellCommandProvider mondShell = new MondShellCommandProvider();

		context.registerService(CommandProvider.class, mondShell, properties);
		context.registerService(MondShellCommandProvider.class, mondShell, properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}	

}
