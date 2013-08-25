package net.devmond.shell;



/**
 * Each string option represents an option of a command.
 * 
 */
public class StringOption implements Option
{

	public static final String STRING_OPTION_PREFIX = "-";
	private final String optionName;

	public StringOption(String optionName) {
		if (optionName == null)
			throw new NullPointerException("Null is not allowed as parameter for a " + this.getClass().getSimpleName());
		this.optionName = optionName;
	}
	
	@Override
	public boolean matches(String optionString)
	{
		return optionName.equals(optionString);
	}

	@Override
	public String toString()
	{
		return STRING_OPTION_PREFIX + optionName;
	}

	@Override
	public boolean isValueOption()
	{
		return false;
	}
	
}
