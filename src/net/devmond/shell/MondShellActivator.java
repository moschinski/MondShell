package net.devmond.shell;

import java.util.Hashtable;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The activator class controls the plug-in life cycle
 */
public class MondShellActivator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "MondShell"; //$NON-NLS-1$

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, ?> properties = new Hashtable<String, String>();
		context.registerService(CommandProvider.class.getName(), new MondShellCommandProvider(),
				properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ServiceReference<?> serviceReference = context.
				  getServiceReference(MondShellCommandProvider.class.getName());
		context.ungetService(serviceReference);
	}	

}
