package net.devmond.shell.util;

public class StringUtils
{

	public static String removeStart(String string, String prefix)
	{
		if (string.startsWith(prefix))
		{
			return string.substring(prefix.length());
		}
		return string;
	}

	public static String removeEnd(String string, String suffix)
	{
		if (string.endsWith(suffix))
		{
			return string.substring(0, string.length() - suffix.length());
		}
		return string;
	}

}
