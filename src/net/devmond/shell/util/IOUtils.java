package net.devmond.shell.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public final class IOUtils
{
	private IOUtils()
	{
		// not for initialization
	}

	public static String readStream(InputStream stream)
	{
		try (java.util.Scanner s = new Scanner(stream))
		{
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	public static String readFile(String path) throws IOException
	{
		try (FileInputStream stream = new FileInputStream(new File(path)))
		{
			return readStream(stream);
		}
	}

	public static InputStream toInputStream(String string)
			throws UnsupportedEncodingException
	{
		return new ByteArrayInputStream(string.getBytes(UTF_8));
	}

	public static void writeToFile(File outputFile, String string)
			throws IOException
	{
		try (FileOutputStream fos = new FileOutputStream(outputFile);
				OutputStreamWriter writer = new OutputStreamWriter(fos, UTF_8))
		{
			writer.write(string);
		}
	}
}
