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
		context.registerService(CommandProvider.class.getName(), new MondShellCommandProvider(),
				properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}	

}
