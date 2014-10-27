package net.devmond.shell;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommandInputImpl implements CommandInput
{
	private final Options options;
	private final Iterator<String> iterator;
	private final List<String> arguments;

	public CommandInputImpl(Options options, List<String> arguments)
	{
		this.options = options;
		this.arguments = arguments;
		this.iterator = arguments.iterator();
	}

	@Override
	public boolean hasOption(Option option)
	{
		return options.isOptionSet(option);
	}

	@Override
	public String nextArgument()
	{
		return iterator.next();
	}

	@Override
	public boolean hasNextArgument()
	{
		return iterator.hasNext();
	}

	@Override
	public Object getValueForOption(Option option)
	{
		return options.getValueForOption(option);
	}

	@Override
	public Object getValueForOption(Option option, Object defaultValue)
	{
		return options.getValueForOption(option, defaultValue);
	}


	@Override
	public Collection<String> getArguments()
	{
		return Collections.unmodifiableCollection(arguments);
	}
}
