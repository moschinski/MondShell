package net.devmond.shell.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	public static void main(String[] args)
	{

		String[] a = new String[]
		{ "bla", "muh" };
		List<String> asList = Collections.unmodifiableList(Arrays.asList(a));
		System.out.println(asList);
		a[0] = null;
		System.out.println(asList);
	}

}
