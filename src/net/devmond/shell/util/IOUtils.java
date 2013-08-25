package net.devmond.shell.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public final class IOUtils
{

	public static String toString(String path) throws IOException
	{
		FileInputStream stream = new FileInputStream(new File(path));
		// get charset of a file?
		try
		{
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally
		{
			stream.close();
		}
	}

	public static String toString(InputStream stream)
	{
		java.util.Scanner s = new Scanner(stream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private IOUtils()
	{
		// not for initialization
	}

	public static String readFile(String path) throws IOException
	{
		FileInputStream stream = new FileInputStream(new File(path));
		// get charset of a file?
		try
		{
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally
		{
			stream.close();
		}
	}

	public static InputStream toInputStream(String string)
			throws UnsupportedEncodingException
	{
		return new ByteArrayInputStream(string.getBytes("UTF-8"));
	}

	public static void writeToFile(File outputFile, String string)
			throws IOException
	{
		Writer writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFile), "utf-8"));
			writer.write(string);
		} finally
		{
			if (writer != null)
				writer.close();

		}
	}
}
