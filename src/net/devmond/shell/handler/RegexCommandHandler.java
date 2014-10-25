package net.devmond.shell.handler;

import static java.lang.String.format;
import static net.devmond.shell.handler.ResultMaker.textResult;
import static net.devmond.shell.util.StringConstant.NEW_LINE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.devmond.shell.CommandInput;
import net.devmond.shell.Option;
import net.devmond.shell.StringOption;
import net.devmond.shell.ValueStringOption;
import net.devmond.shell.util.HelpTextBuilder;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class RegexCommandHandler extends AbstractCommandHandler
{

	private static final Option MATCH_PART_OPTIONS = new StringOption("part");
	private static final Option SHOW_GROUPS_OPTIONS = new StringOption("groups");
	private static final Option REGEX_FLAGS_OPTIONS = new ValueStringOption("flags");

	public RegexCommandHandler(CommandInterpreter interpreter)
	{
		super(interpreter, MATCH_PART_OPTIONS, SHOW_GROUPS_OPTIONS,
				REGEX_FLAGS_OPTIONS);
	}

	@Override
	protected Result executeInternal(CommandInput cmdInput)
	{
		String input = cmdInput.nextArgument();
		String regex = cmdInput.nextArgument();

		Pattern compile = Pattern.compile(regex, parseFlags(cmdInput.getValueForOption(REGEX_FLAGS_OPTIONS)));

		boolean showGroups = cmdInput.hasOption(SHOW_GROUPS_OPTIONS);

		Matcher matcher = compile.matcher(input);
		if (cmdInput.hasOption(MATCH_PART_OPTIONS))
		{
			StringBuilder buff = getPartMatches(showGroups, matcher);

			if (buff.length() > 0)
				return textResult(buff);
		}

		if (matcher.matches())
			return textResult("matches: true\n" + printGroup(matcher, showGroups));

		return textResult("matches: false");
	}

	private StringBuilder getPartMatches(boolean showGroups, Matcher matcher)
	{
		StringBuilder buff = new StringBuilder();
		while (matcher.find())
		{
			buff.append(printGroup(matcher, showGroups));
		}

		boolean matches = buff.length() == 0 ? false : true;
		buff.insert(0, format("matches partly: %s%s", matches, NEW_LINE));

		return buff;
	}

	private static int parseFlags(Object flagsObj)
	{
		if (flagsObj == null)
			return 0;

		String flagStr = flagsObj.toString();

		String[] flags = flagStr.split(",");
		int patternFlag = 0;

		for (String flag : flags)
		{
			patternFlag |= getIntValue(flag);
		}

		return patternFlag;
	}

	private static int getIntValue(String flag)
	{
		if (flag.equalsIgnoreCase("insensitive"))
			return Pattern.CASE_INSENSITIVE;

		if (flag.equalsIgnoreCase("canon"))
			return Pattern.CANON_EQ;

		return 0;
	}

	private CharSequence printGroup(Matcher matcher, boolean includeSubGroups)
	{
		int groupCount = matcher.groupCount();
		StringBuilder buff = new StringBuilder();
		for (int i = 1; i <= groupCount; i++) // group 0 is the whole string,
												// useless for our purpose
		{
			buff.append(format("group %d: '%s'%s", i, matcher.group(i), NEW_LINE));
		}
		return buff;
	}

	@Override
	public CharSequence getHelpText()
	{
		return new HelpTextBuilder("match", "tests whether an input matches a regex")
				.addOption(MATCH_PART_OPTIONS, "The regex need only to match a part of the given input")
				.addOption(SHOW_GROUPS_OPTIONS, "The groups that are matched are shown")
				.addOption(REGEX_FLAGS_OPTIONS,
						"Using this option you can define regex flags, e.g., flags insensitive,canon")
				.addArgument("input", "the string that should be matched")
				.addArgument("regex", "the regex that should be used for string matching").toString();
	}

	public static void main(String[] args)
	{
		System.out.println(new HelpTextBuilder("match", "tests whether an input matches a regex")
				.addOption(MATCH_PART_OPTIONS, "The regex need only to match a part of the given input")
				.addOption(SHOW_GROUPS_OPTIONS, "The groups that are matched are shown")
				.addOption(REGEX_FLAGS_OPTIONS,
						"Using this option you can define regex flags, e.g., flags insensitive,canon")
				.addArgument("input", "the string that should be matched")
				.addArgument("regex", "the regex that should be used for string matching").toString());
	}
}
