package logger;

import java.io.PrintWriter;

import logger.exception.ConsoleException;

public abstract class TextDevice
{

	public abstract TextDevice	printf(String fmt, Object... params)	throws ConsoleException;
	public abstract PrintWriter	writer()								throws ConsoleException;

}

