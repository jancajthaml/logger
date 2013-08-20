package logger;

import java.io.PrintWriter;

import logger.exception.ConsoleException;

public class CharacterDevice extends TextDevice
{
	private final PrintWriter writer;

	public CharacterDevice(PrintWriter writer)
	{
		this.writer = writer;
	}

	@Override
	public CharacterDevice printf(String fmt, Object... params) throws ConsoleException
	{
		writer.printf(fmt, params);
		return this;
	}

	@Override
	public PrintWriter writer() throws ConsoleException
	{ return writer; }

}