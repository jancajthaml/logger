package logger;

import java.io.Console;
import java.io.PrintWriter;

import logger.exception.ConsoleException;

public class ConsoleDevice extends TextDevice
{

	private final Console console;

	public ConsoleDevice(Console console)
	{ this.console = console; }

	@Override
	public TextDevice printf(String fmt, Object... params) throws ConsoleException
	{
		console.format(fmt, params);
		return this;
	}

	@Override
	public PrintWriter writer() throws ConsoleException
	{ return console.writer(); }

}