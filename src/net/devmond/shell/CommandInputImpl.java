package net.devmond.shell;

import java.util.Iterator;
import java.util.List;

public class CommandInputImpl implements CommandInput
{
	private final Options options;
	private final Iterator<String> iterator;

	public CommandInputImpl(Options options, List<String> arguments)
	{
		this.options = options;
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

}
