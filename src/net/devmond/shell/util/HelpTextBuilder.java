package net.devmond.shell.util;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.devmond.shell.Option;

public class HelpTextBuilder
{

	private static class CmdValueDesc
	{
		String name;
		String desc;

		CmdValueDesc(String name, String desc)
		{
			this.name = requireNonNull(name);
			this.desc = requireNonNull(desc);
		}
	}

	/** Strings used to format other strings */
	private final static String tab = "\t"; //$NON-NLS-1$
	private final static String newline = "\r\n"; //$NON-NLS-1$
	private final String command;
	private final String description;

	private final Collection<CmdValueDesc> options = new ArrayList<>(1);
	private final Collection<CmdValueDesc> arguments = new ArrayList<>(1);

	public HelpTextBuilder(String command, String description)
	{
		this.command = command;
		this.description = description;
	}

	public HelpTextBuilder addArgument(String command, String description)
	{
		arguments.add(new CmdValueDesc(command, description));
		return this;
	}

	public HelpTextBuilder addOption(Option option, String optionDesc)
	{
		options.add(new CmdValueDesc(option.toString(), optionDesc));
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder helpText = new StringBuilder();
		helpText.append(command).append(": \"").append(description).append("\"").append(newline).append("* USAGE: ")
				.append(command).append(getOptionsList())
				.append(getArguments().append(getDetailedOptionDesc()).append(getDetailedArgumentDesc()));
		return helpText.toString();
	}

	private StringBuilder getOptionsList()
	{
		return getListing(options, true);
	}

	private StringBuilder getArguments()
	{
		return getListing(arguments, false);
	}

	private StringBuilder getListing(Collection<CmdValueDesc> options, boolean addBrackets)
	{
		StringBuilder optionsBuilder = new StringBuilder();
		if (options.isEmpty())
		{
			return optionsBuilder;
		}
		Iterator<CmdValueDesc> iterator = options.iterator();
		optionsBuilder.append(" ");
		while (iterator.hasNext())
		{
			optionsBuilder.append(addBrackets ? "[" : "").append(iterator.next().name).append(addBrackets ? "]" : "");
			if (iterator.hasNext())
				optionsBuilder.append(" ");
		}
		return optionsBuilder;
	}

	private StringBuilder getDetailedOptionDesc()
	{
		return getDetailedList("OPTIONS", options);
	}

	private StringBuilder getDetailedArgumentDesc()
	{
		return getDetailedList("ARGUMENTS (mandatory)", arguments);
	}

	private StringBuilder getDetailedList(String headline, Collection<CmdValueDesc> describables)
	{
		StringBuilder detail = new StringBuilder();
		if (describables.isEmpty())
		{
			return detail;
		}
		detail.append(newline);
		detail.append("* ").append(headline).append(":");
		detail.append(newline);

		Iterator<CmdValueDesc> iterator = describables.iterator();
		while (iterator.hasNext())
		{
			CmdValueDesc option = iterator.next();
			detail.append(tab);
			detail.append(option.name);
			detail.append(" - "); //$NON-NLS-1$
			detail.append("\""); //$NON-NLS-1$
			detail.append(option.desc);
			detail.append("\""); //$NON-NLS-1$
			if (iterator.hasNext())
				detail.append(newline);
		}
		return detail;
	}

}
