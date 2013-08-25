package net.devmond.shell.handler;

import net.devmond.shell.StringOption;
import net.devmond.shell.Option;
import net.devmond.shell.HelpTextProvider;

public interface CommandHandler extends HelpTextProvider {

	Option HELP_FLAG = new StringOption("help");
	
	void execute() throws Exception;

}
