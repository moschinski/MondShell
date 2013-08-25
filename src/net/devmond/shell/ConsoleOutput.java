package net.devmond.shell;

import org.eclipse.osgi.framework.console.CommandInterpreter;

class ConsoleOutput implements Output {

	private final CommandInterpreter ci;

	public ConsoleOutput(CommandInterpreter ci) {
		this.ci = ci;
	}

	@Override
	public void println(Object output) {
		ci.println(output);
	}

	@Override
	public void print(Object output) {
		ci.print(output);
	}

	@Override
	public void close() {
		// nothing to do here
	}

}
