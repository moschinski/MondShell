package net.devmond.shell.handler;

import static java.lang.String.format;
import static net.devmond.shell.handler.ResultMaker.textResult;
import static net.devmond.shell.util.IOUtils.readFile;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.script.ScriptException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;

import org.eclipse.osgi.framework.console.CommandInterpreter;

/**
 *
 */
public class FormatCommandHandler extends JavaScriptBasedCommandHandler
{

	private static final String JSON_FORMAT_TYPE = "json";
	private static final String XML_FORMAT_TYPE = "xml";

	public FormatCommandHandler(CommandInterpreter cmdInterpreter) throws ScriptException, IOException
	{
		super(cmdInterpreter);
	}

	@Override
	protected Result executeInternal(CommandInput input) throws Exception
	{
		String formatType = input.nextArgument().toLowerCase();
		Formatter formatter = getFormatterByType(formatType);

		String argument = input.nextArgument();

		String unformattedStr = isPathToFile(argument) ? readFile(argument) : argument;

		return textResult(formatter.format(unformattedStr));
	}

	private boolean isPathToFile(String argument)
	{
		return new File(argument).isFile();
	}

	private Formatter getFormatterByType(String formatType)
	{
		if (JSON_FORMAT_TYPE.equals(formatType))
			return new JsonFormatter();

		if (XML_FORMAT_TYPE.equals(formatType))
			return new XmlFormatter();

		throw new InvalidCommandException(format("No formatter for '%s' found", formatType), getHelpText());
	}

	private interface Formatter
	{
		Object format(String unformattedStr) throws Exception;
	}

	private class JsonFormatter implements Formatter
	{
		@Override
		public Object format(String unformattedStr) throws ScriptException
		{
			int jsonIndent = 4; // make configurable?
			return evalJs(String.format("JSON.stringify(%s, null, %d);", unformattedStr, jsonIndent));
		}
	}

	private class XmlFormatter implements Formatter
	{
		@Override
		// based on stackoverflow answer:
		// http://stackoverflow.com/a/1264912/346899
		public Object format(String unformattedStr) throws TransformerException
		{
			int xmlIndent = 4; // make configurable?
			Source xmlInput = new StreamSource(new StringReader(unformattedStr));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", xmlIndent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter();
		}
	}

}
