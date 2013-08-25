package net.devmond.shell;

import static java.lang.String.format;

import java.util.Iterator;


/**
 * A value option defines a option that can have one or more additional values.
 * For -yourOption yourOptionValue1
 */
public class ValueOptionStrategy extends DefaultOptionStrategy
{
	public ValueOptionStrategy(Options options)
	{
		super(options);
	}

	@Override
	public void interpretFlag(Option option, Iterator<String> iterator)
	{
		if (option.isValueOption())
			addValueForOption(option, iterator);
		else
			super.interpretFlag(option, iterator);
	}

	private void addValueForOption(Option option, Iterator<String> iterator)
	{
		if (iterator.hasNext())
		{
			getOptions().setValueForOption(option, iterator.next());
			return;
		}
		throw new InvalidCommandException(format("Option '%s' requires a value", option));
	}

}
