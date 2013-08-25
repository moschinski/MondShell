package net.devmond.shell.util;

import net.devmond.shell.Option;

public class HelpTextBuilder implements CharSequence
{

	/** Strings used to format other strings */
	private final static String tab = "\t"; //$NON-NLS-1$
	private final static String newline = "\r\n"; //$NON-NLS-1$
	private final StringBuilder help;

	public HelpTextBuilder(String command, String arguments)
	{
		this.help = new StringBuilder();
		help.append("Usage: ").append(command).append(" [-options] ").append(arguments);
	}

	public HelpTextBuilder addHeader(String header)
	{
		help.append("---"); //$NON-NLS-1$
		help.append(header);
		help.append("---"); //$NON-NLS-1$
		help.append(newline);
		return this;
	}

	/**
	 * Private helper method for getHelp. Formats the command descriptions.
	 * 
	 * @return
	 */
	public HelpTextBuilder addCommand(String command, String description)
	{
		help.append(tab);
		help.append(command);
		help.append(" - "); //$NON-NLS-1$
		help.append(description);
		help.append(newline);
		return this;
	}

	/**
	 * Private helper method for getHelp. Formats the command descriptions with
	 * command arguments.
	 * 
	 * @return
	 */
	public HelpTextBuilder addCommand(String command, String parameters,
			String description)
	{
		help.append(tab);
		help.append(command);
		help.append(" "); //$NON-NLS-1$
		help.append(parameters);
		help.append(" - "); //$NON-NLS-1$
		help.append(description);
		help.append(newline);
		return this;
	}

	public HelpTextBuilder addParameter(Option parameter, String description)
	{
		help.append(tab);
		help.append(parameter);
		help.append(" - "); //$NON-NLS-1$
		help.append(description);
		help.append(newline);
		return this;
	}

	@Override
	public int length()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char charAt(int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
