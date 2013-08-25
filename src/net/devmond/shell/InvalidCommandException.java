package net.devmond.shell;


public class InvalidCommandException extends IllegalArgumentException {

	private static final long serialVersionUID = 5637352459578846139L;

	public InvalidCommandException(String expectedArgurmentException) {
		super(expectedArgurmentException);
	}
	public InvalidCommandException(String expectedArgurmentException, CharSequence HelpText) {
		// TODO Auto-generated constructor stub
	}
	
	public InvalidCommandException(CharSequence HelpText) {
		// TODO Auto-generated constructor stub
	}

}
