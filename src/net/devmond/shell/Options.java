package net.devmond.shell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Options
{
	private static final String NO_OPTION_VALUE = null;

	private final Map<Option, Object> setOptions;
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
		this.setOptions = new HashMap<Option, Object>(supportedOptions.length);
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

		setOptions.put(option, value);
	}

	Object getValueForOption(Option option)
	{
		return setOptions.get(option);
	}

	boolean isOptionSet(Option option)
	{
		return setOptions.containsKey(option);
	}

	void setInputHasOption(Option option)
	{
		setOptions.put(option, NO_OPTION_VALUE);
	}

}