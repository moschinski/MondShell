package net.devmond.shell.handler;

import static net.devmond.shell.handler.ResultMaker.textResult;
import net.devmond.shell.CommandInput;
import net.devmond.shell.InvalidCommandException;

import org.eclipse.osgi.framework.console.CommandInterpreter;

public abstract class StringCommandHandler extends AbstractCommandHandler {
	public enum Mode {
		LOWER_CASE, UPPER_CASE;
	}
	
	StringCommandHandler(CommandInterpreter cmdInterpreter) {
		super(cmdInterpreter);
	}

	public static StringCommandHandler createByMode(CommandInterpreter ci, Mode mode) {
		switch (mode) {
		case LOWER_CASE:
			return new StringCommandHandler(ci) {

				@Override
				String transform(String str) {
					return str.toLowerCase();
				}
			};
		case UPPER_CASE:
			return new StringCommandHandler(ci) {

				@Override
				String transform(String str) {
					return str.toUpperCase();
				}
			};
		default:
			throw new InvalidCommandException("Character mode '" + mode + "' is not supported");
		}
	}


	@Override
	protected Result executeInternal(CommandInput cmdInput) throws Exception
	{
		String str = cmdInput.nextArgument();
		return textResult(transform(str));
	}

	abstract String transform(String str);

}
