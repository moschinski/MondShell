package net.devmond.shell;

import java.util.Iterator;

public class DefaultOptionStrategy implements OptionStrategy
{

	private final Options options;

	public DefaultOptionStrategy(Options options)
	{
		this.options = options;
	}

	@Override
	public void interpretFlag(Option option, Iterator<String> iterator)
	{
		options.setInputHasOption(option);
	}

	@Override
	public Options getOptions()
	{
		return this.options;
	}

}
