package net.devmond.shell.handler;

class TextResult implements Result
{
	private final String result;

	TextResult(String result)
	{
		this.result = result;
	}

	@Override
	public String toString()
	{
		return result;
	}

	@Override
	public boolean isResultDisplayable()
	{
		return true;
	}

	static Result textResult(Object string)
	{
		return new TextResult(string.toString());
	}

}
