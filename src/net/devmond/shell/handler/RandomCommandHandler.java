package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;

import java.security.SecureRandom;
import java.util.UUID;

import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public class RandomCommandHandler extends AbstractCommandHandler
{

	private static final String DOUBLE_RANDOM = "double";
	private static final String INT_RANDOM = "int";
	private static final String UUID_RANDOM = "uuid";

	public RandomCommandHandler(CommandInterpreter cmdInterpreter)
	{
		super(cmdInterpreter);
	}

	@Override
	protected Result executeInternal(CommandInput input) throws Exception
	{
		String type = input.hasNextArgument() ? input.nextArgument() : INT_RANDOM;

		if (UUID_RANDOM.equals(type))
			return textResult(UUID.randomUUID());

		if (INT_RANDOM.equals(type))
			return textResult(new SecureRandom().nextInt());

		if (DOUBLE_RANDOM.equals(type))
			return textResult(new SecureRandom().nextDouble());

		throw new InvalidCommandException(getHelpText());
	}

}
