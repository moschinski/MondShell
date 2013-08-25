package net.devmond.shell.handler;

class ResultMaker
{

	static Result nullResult()
	{
		return NullResult.nullResult();
	}

	static Result textResult(Object string)
	{
		return TextResult.textResult(string);
	}
}
