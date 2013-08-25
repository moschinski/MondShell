package net.devmond.shell;


public interface Option {

	boolean matches(String optionString);

	boolean isValueOption();
}
