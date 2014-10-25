package net.devmond.shell;





/**
 * Each instance of this class represents an option that must have a value.
 */
public class ValueStringOption extends StringOption
{
	
	public ValueStringOption(String flagName)
	{
		super(flagName);
	}

	@Override
	public boolean isValueOption()
	{
		return true;
	}

	@Override
	public String toString()
	{
		return super.toString() + " <value(s)>";
	}
	
}
