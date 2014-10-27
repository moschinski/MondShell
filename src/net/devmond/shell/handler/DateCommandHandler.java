/*
 * Copyright (C) 2013 Stefan Moschinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.devmond.shell.handler;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;
import net.devmond.shell.ValueStringOption;
import net.devmond.shell.util.HelpTextBuilder;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class DateCommandHandler extends AbstractCommandHandler
{
	private static final ValueStringOption LOCALE_OPTION = new ValueStringOption("locale");
	private static final ValueStringOption PATTERN_OPTION = new ValueStringOption("pattern");
	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public DateCommandHandler(CommandInterpreter cmdInterpreter)
	{
		super(cmdInterpreter, LOCALE_OPTION, PATTERN_OPTION);
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		Collection<String> arguments = cmdInput.getArguments();
		Long timestamp = arguments.size() > 0 ? Long.valueOf(arguments.iterator().next()) : System.currentTimeMillis();

		String pattern = getPattern(cmdInput);
		Locale locale = getLocale(cmdInput);

		return ResultMaker.textResult(formatDate(timestamp, pattern, locale));
	}

	private String getPattern(CommandInput cmdInput)
	{
		return cmdInput.getValueForOption(PATTERN_OPTION, DEFAULT_PATTERN).toString();
	}

	private Locale getLocale(CommandInput cmdInput)
	{
		Object localeStr = cmdInput.getValueForOption(LOCALE_OPTION);
		final Locale locale;
		if (localeStr == null)
		{
			return Locale.getDefault(Locale.Category.FORMAT);
		} else
		{
			locale = getLocaleFor(localeStr.toString());
			if (locale == null)
				throw new InvalidCommandException(String.format("Cannot find a locale for country '%s'", localeStr));
			return locale;
		}
	}

	private static Locale getLocaleFor(String countryName)
	{
		for (Locale l : Locale.getAvailableLocales())
		{
			if (l.toString().equalsIgnoreCase(countryName))
			{
				return l;
			}
		}
		return null;
	}

	private String formatDate(Long timestamp, String pattern, Locale locale)
	{
		return new SimpleDateFormat(pattern, locale).format(new Date(timestamp));
	}

	@Override
	public CharSequence getHelpText()
	{
		return new HelpTextBuilder("date",
				"Formats the epoch timestamp (i.e., the milliseconds since the January 1, 1970, 00:00:00 GMT")
				.addOption(LOCALE_OPTION,
						"Let you define a custom locale (e.g., en_US), by default Locale.getDefault(Locale.Category.FORMAT) is used")
				.addOption(PATTERN_OPTION,
						String.format("Defines how the date is formatted, by default '%s' is used", DEFAULT_PATTERN))
				.addOptionalArgument("timestamp",
						"The timestamp that should be formatted, if none is given the current timestamp is used")
				.toString();
	}

}
