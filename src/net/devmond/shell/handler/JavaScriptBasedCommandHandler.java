package net.devmond.shell.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.devmond.shell.Option;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.FrameworkUtil;

abstract class JavaScriptBasedCommandHandler extends AbstractCommandHandler
{

	private static final String JAVA_SCRIPT = "JavaScript";
	private static final String JSON_LIB = "json3.min.js";
	private static ScriptEngine JAVASCRIPT_ENGINE;


	JavaScriptBasedCommandHandler(CommandInterpreter cmdInterpreter, Option... options)
			throws ScriptException, IOException
	{
		super(cmdInterpreter, options);
		createJavaScriptEngine();
	}

	private static synchronized void createJavaScriptEngine() throws ScriptException, IOException
	{
		if (JAVASCRIPT_ENGINE != null)
			return;

		ScriptEngineManager factory = new ScriptEngineManager();
		JAVASCRIPT_ENGINE = factory.getEngineByName(JAVA_SCRIPT);

		/* JAVASCRIPT_ENGINE. */loadRequiredLibs();
	}

	private static void loadRequiredLibs() throws ScriptException, IOException
	{
		InputStream jsonInput = getJsonLib();
		JAVASCRIPT_ENGINE.eval(new InputStreamReader(jsonInput));
	}

	private static InputStream getJsonLib() throws IOException
	{
		InputStream jsonInput = JavaScriptBasedCommandHandler.class.getResourceAsStream(JSON_LIB);
		return jsonInput == null ? loadJsonLibViaOsgi() : jsonInput;
	}

	private static InputStream loadJsonLibViaOsgi() throws IOException
	{
		Enumeration<URL> jsonLib = FrameworkUtil.getBundle(JavaScriptBasedCommandHandler.class).findEntries("lib",
				JSON_LIB, false);
		if (!jsonLib.hasMoreElements())
		{
			throw new IllegalStateException("Could not find required JSON JavaScript lib");
		}
		return jsonLib.nextElement().openStream();
	}

	protected Object evalJs(String script) throws ScriptException
	{
		return JAVASCRIPT_ENGINE.eval(script);
	}

}
