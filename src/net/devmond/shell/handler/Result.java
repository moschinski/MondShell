package net.devmond.shell.handler;

interface Result
{
	@Override
	String toString();

	boolean isResultDisplayable();
}
