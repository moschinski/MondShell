package net.devmond.shell.handler;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.devmond.shell.handler.ResultMaker.textResult;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;
import net.devmond.shell.Option;
import net.devmond.shell.ValueStringOption;
import net.devmond.shell.util.HelpTextBuilder;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class EncodeCommandHandler extends AbstractCommandHandler
{

	private static final String HTML_ENCODING = "html";
	private static final String BASE64_ENCODING = "base64";
	private static final String URL_ENCODING = "url";
	private static final Option CHARSET_FLAG = new ValueStringOption("charset");

	public EncodeCommandHandler(CommandInterpreter cmdInterpreter)
	{
		super(cmdInterpreter, CHARSET_FLAG);
	}

	@Override
	protected Result executeInternal(CommandInput input) throws Exception
	{
		Charset charset = getConfiguredCharset(input.getValueForOption(CHARSET_FLAG));

		String encoding = input.nextArgument().toLowerCase();
		String arg = input.nextArgument();

		switch (encoding)
		{
		case URL_ENCODING:
			return textResult(URLEncoder.encode(arg, charset.toString()));
		case BASE64_ENCODING:
			return textResult(new String(encodeBase64(arg.getBytes(charset)), charset));
		case HTML_ENCODING:
			return textResult(encodeHtml(arg));
		default:
			throw new InvalidCommandException(String.format("The encoding '%s' is not supported.%n%s", encoding,
					getHelpText()));
		}
	}

	@Override
	public CharSequence getHelpText()
	{
		return new HelpTextBuilder("encode", "encodes a given sting")
				.addOption(CHARSET_FLAG, "Defines the charset to use for encoding, default is UTF-8")
				.addArgument("encodingType",
						"defines which encoding should be performed, available are 'html', 'base64' and 'url'")
				.addArgument("stringToEncode", "the sting that should be encoded")
				.toString();
	}

	private Charset getConfiguredCharset(Object configuredCharset)
	{
		return configuredCharset == null ? UTF_8 : Charset.forName(configuredCharset.toString());
	}

	/**
	 * Encodes the given string using HTML (based on a solution posted on
	 * http://forums.thedailywtf.com/forums/p/2806/72054.aspx#72054)
	 * 
	 * @param strToEncode
	 * @return an HTML encoded string
	 * @see http://forums.thedailywtf.com/forums/p/2806/72054.aspx#72054
	 */
	private static StringBuilder encodeHtml(String strToEncode)
	{
		StringBuilder htmlStr = new StringBuilder();
		for (int i = 0; i < strToEncode.length(); i++)
		{
			char c = strToEncode.charAt(i);
			if (c > 127 || c == '"' || c == '<' || c == '>')
				htmlStr.append("&#" + (int) c + ";");

			else
				htmlStr.append(c);
		}
		return htmlStr;
	}
}
