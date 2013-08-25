package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;
import net.devmond.shell.Option;
import net.devmond.shell.ValueStringOption;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class EncodeCommandHandler extends AbstractCommandHandler {

	private static final String UTF_8 = "UTF-8";
	private static final String HTML_ENCODING = "html";
	private static final String BASE64_ENCODING = "base64";
	private static final String URL_ENCODING = "url";
	private static final Option CHARSET_FLAG = new ValueStringOption("charset");

	public EncodeCommandHandler(CommandInterpreter cmdInterpreter) {
		super(cmdInterpreter, CHARSET_FLAG);
	}

	@Override
	protected Result executeInternal(CommandInput input) throws Exception
	{
		Charset charset = getConfiguredCharset(input.getValueForOption(CHARSET_FLAG));

		String encoding = input.nextArgument().toLowerCase();
		String arg = input.nextArgument();

		if (URL_ENCODING.equals(encoding))
			return textResult(URLEncoder.encode(arg, charset.toString()));

		if (BASE64_ENCODING.equals(encoding))
			return textResult(new String(encodeBase64(arg.getBytes(charset)), charset));

		if (HTML_ENCODING.equals(encoding))
			return textResult(encodeHtml(arg));

		throw new InvalidCommandException(getHelpText());
	}

	private Charset getConfiguredCharset(Object configuredCharset) {
		return Charset.forName(configuredCharset == null ? UTF_8
				: configuredCharset.toString());
	}

	// based on a solution posted on http://forums.thedailywtf.com/forums/p/2806/72054.aspx#72054
	private static StringBuilder encodeHtml(String strToEncode) {
		StringBuilder htmlStr = new StringBuilder();
		for (int i = 0; i < strToEncode.length(); i++) {
			char c = strToEncode.charAt(i);
			if (c > 127 || c == '"' || c == '<' || c == '>')
				htmlStr.append("&#" + (int) c + ";");

			else
				htmlStr.append(c);
		}
		return htmlStr;
	}
}
