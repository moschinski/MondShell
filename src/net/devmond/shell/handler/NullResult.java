package net.devmond.shell.handler;

/**
 * Represents a result of a command that does not contain a value.
 */
class NullResult implements Result
{

	static Result nullResult()
	{
		return new NullResult();
	}

	@Override
	public String toString()
	{
		return null;
	}

	@Override
	public boolean isResultDisplayable()
	{
		return false;
	}

	private NullResult()
	{

	}
}
