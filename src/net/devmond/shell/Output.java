package net.devmond.shell;

public interface Output {

	void println(Object output);
	
	void print(Object output);

	void close();
}
