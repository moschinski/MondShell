package net.devmond.shell;

import java.util.Iterator;

public interface OptionStrategy
{

	/**
	 * Implementing classes may override this method to provide their own
	 * handling strategy for options
	 * 
	 * @param option
	 * @param iterator
	 *            not used in this option strategy
	 */
	void interpretFlag(Option option, Iterator<String> iterator);

	Options getOptions();

}
