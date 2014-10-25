package net.devmond.shell;

import org.eclipse.ui.IStartup;

/**
 * This class guarantees that the MondShell is started when Eclipse starts
 * (i.e., the MondShellActivator is called).
 */
public class MondShellStartup implements IStartup {

	@Override
	public void earlyStartup() {
	}

}
