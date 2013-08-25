package net.devmond.shell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class FileOutput implements Output {	

	public static Output createFileOutput(File file) {
		if (file.exists()) {
			System.err.format("File '%s' already exists",
					file.getAbsolutePath());
		}
		
		if (!file.getParentFile().mkdirs()) {
			
		}
		try {
			if (!file.createNewFile()) {
				
			}
			return new FileOutput(file);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
	}
	
	private final PrintWriter printWriter;
	
	private FileOutput(File file) throws FileNotFoundException {
		printWriter = new PrintWriter(file);
	}

	@Override
	public void println(Object output) {
		printWriter.println(output);
	}

	@Override
	public void print(Object output) {
		printWriter.print(output);
	}

	@Override
	public void close() {
		printWriter.flush();
		printWriter.close();
	}


}
