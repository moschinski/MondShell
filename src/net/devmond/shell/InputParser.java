package net.devmond.shell;

import static net.devmond.shell.FileOutput.createFileOutput;
import static net.devmond.shell.StringOption.STRING_OPTION_PREFIX;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

/**
 * This class is responsible for parsing the command line input.
 */
public class InputParser
{
	private static final String OUTPUT = ">";
	private final CommandInterpreter cmdInterpreter;
	private final List<String> inputs;

	public InputParser(CommandInterpreter cmdInterpreter)
	{
		this.cmdInterpreter = cmdInterpreter;
		this.inputs = getInputs(cmdInterpreter);
	}

	public ParseResult parseCommandInput(Option... supportedOptions)
	{
		Options options = Options.from(supportedOptions);
		OptionStrategy optionStrategy = getOptionStrategy(options);

		List<String> cmdArguments = new ArrayList<String>();
		Output output = null;

		Iterator<String> inputIterator = inputs.iterator();
		while (inputIterator.hasNext())
		{
			output = parseInput(optionStrategy, options, output, inputIterator, cmdArguments);
		}
		output = getConsoleOutputIfNoneSet(output);
		CommandInput commandInput = new CommandInputImpl(optionStrategy.getOptions(), cmdArguments);
		return new ParseResult(commandInput, output);
	}

	private OptionStrategy getOptionStrategy(Options options)
	{
		OptionStrategy optionStrategy = options.hasValueOption() ? new ValueOptionStrategy(options)
				: new DefaultOptionStrategy(options);
		return optionStrategy;
	}

	private List<String> getInputs(CommandInterpreter cmdInterpreter)
	{
		List<String> inputs = new ArrayList<String>();
		String arg;
		while ((arg = cmdInterpreter.nextArgument()) != null)
			inputs.add(arg);
		return inputs;
	}

	private Output getConsoleOutputIfNoneSet(Output output)
	{
		return output == null ? new ConsoleOutput(cmdInterpreter) : output;
	}

	private static Output parseInput(OptionStrategy optionStrategy, Options options, Output output,
			Iterator<String> inputIterator, List<String> arguments)
	{
		String input = inputIterator.next();
		if (input.startsWith(STRING_OPTION_PREFIX))
		{
			Option option = options.getSupportedOption(input.substring(STRING_OPTION_PREFIX.length()));
			optionStrategy.interpretFlag(option, inputIterator);
		} else if (input.equals(OUTPUT))
		{
			output = createFileOutput(getFileDestination(inputIterator));
			if (inputIterator.hasNext())
				throw new InvalidCommandException("The file destination must be the last argument of a command");
		} else
			arguments.add(input);
		return output;
	}

	private static File getFileDestination(Iterator<String> iterator)
	{
		if (iterator.hasNext())
			return new File(iterator.next());

		throw new InvalidCommandException("No file location provided, it must be provided after the '>'");
	}
}
