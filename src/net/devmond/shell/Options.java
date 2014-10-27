package net.devmond.shell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Options
{
	private static final String NO_OPTION_VALUE = null;

	private final Map<Option, Object> optionValues;
	private final List<Option> supportedOptions;

	static Options from(Option... options)
	{
		return new Options(options);
	}

	/**
	 * @param supportedOptions
	 *            defines which options the handler supports, this does not mean
	 *            that these options are set
	 */
	private Options(Option[] supportedOptions)
	{
		this.supportedOptions = Arrays.asList(supportedOptions);
		this.optionValues = new HashMap<Option, Object>(supportedOptions.length);
	}

	Option getSupportedOption(String optionString)
	{
		for (Option option : supportedOptions)
		{
			if (option.matches(optionString)) 
				return option;
		}
		return null;
	}

	boolean hasValueOption()
	{
		for (Option option : supportedOptions)
		{
			if (option.isValueOption())
				return true;
		}
		return false;
	}

	/**
	 * @param option
	 * @param value
	 *            must not be <code>null</code>
	 */
	void setValueForOption(Option option, Object value)
	{
		if (value == null)
			throw new InvalidCommandException("value must not be null");

		if (value.toString().startsWith("-")) // prevents incidental misuse
			throw new InvalidCommandException("The value of an option is not allowed to start with '-'");

		optionValues.put(option, value);
	}

	Object getValueForOption(Option option)
	{
		return optionValues.get(option);
	}

	boolean isOptionSet(Option option)
	{
		return optionValues.containsKey(option);
	}

	/**
	 * @param option
	 *            the option for which the value is wanted
	 * @param defaultValue
	 *            the value that is returned if no value for the option is given
	 * @return the value for the {@code option} or the {@code defaultValue} if
	 *         no option value is given
	 */
	Object getValueForOption(Option option, Object defaultValue)
	{
		Object optionVal = optionValues.get(option);
		return optionVal == null ? defaultValue : optionVal;
	}

	void setInputHasOption(Option option)
	{
		optionValues.put(option, NO_OPTION_VALUE);
	}

}