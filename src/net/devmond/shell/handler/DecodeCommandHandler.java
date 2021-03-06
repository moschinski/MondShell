/*
 * Copyright (C) 2013 Stefan Moschinski
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

import java.net.URLDecoder;
import java.nio.charset.Charset;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;
import net.devmond.shell.Option;
import net.devmond.shell.ValueStringOption;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class DecodeCommandHandler extends AbstractCommandHandler {

	private static final String UTF_8 = "UTF-8";
	private static final String HTML_ENCODING = "html";
	private static final String BASE64_ENCODING = "base64";
	private static final String URL_ENCODING = "url";
	private static final Option CHARSET_FLAG = new ValueStringOption("charset");

	public DecodeCommandHandler(CommandInterpreter cmdInterpreter) {
		super(cmdInterpreter, CHARSET_FLAG);
	}

	@Override
	protected Result executeInternal(CommandInput input) throws Exception
	{
		Charset charset = getConfiguredCharset(input.getValueForOption(CHARSET_FLAG));

		String encoding = input.nextArgument().toLowerCase();
		String arg = input.nextArgument();

		if (URL_ENCODING.equals(encoding))
			return textResult(URLDecoder.decode(arg, charset.toString()));

		if (BASE64_ENCODING.equals(encoding))
			return textResult(new String(decodeBase64(arg.getBytes(charset)), charset));

		if (HTML_ENCODING.equals(encoding))
			throw new UnsupportedOperationException();

		throw new InvalidCommandException(getHelpText());
	}

	private Charset getConfiguredCharset(Object configuredCharset) {
		return Charset.forName(configuredCharset == null ? UTF_8
				: configuredCharset.toString());
	}

}
