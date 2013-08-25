package net.devmond.shell;

import java.util.NoSuchElementException;

public interface CommandInput {

	/**
	 * @param option
	 *            the option for which the value is needed
	 * @return the value for the option or <code>null</code> if no value exists
	 */
	Object getValueForOption(Option option);

	/**
	 * @return <code>true</code> if the input has another argument
	 */
	boolean hasNextArgument();

	/**
	 * @return the value of the next argument
	 * @throws NoSuchElementException
	 *             if no next argument exists
	 */
	String nextArgument();

	boolean hasOption(Option option);

}
