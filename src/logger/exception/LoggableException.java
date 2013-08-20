package logger.exception;

import java.io.PrintStream;
import java.io.Serializable;
import logger.Logger;

@SuppressWarnings("serial")
public class LoggableException extends Exception implements Serializable
{
	
	private Throwable exception = this;

	public LoggableException()
	{}

	public LoggableException(String msg)
	{ super(msg); }

	public LoggableException(String msg, Throwable t)
	{
		super(msg);
		exception = t;
	}
		
	public Throwable getException()
	{ return exception;	 }
		
	public void printStackTrace()
	{
		printStackTrace(System.err);
	}
		
	public void printStackTrace(PrintStream ps)
	{
		StackTraceElement[] trace	= this.exception.getStackTrace();
		String tab					= "";
		String[] elem				= trace[0].getClassName().split("\\$");
		String invoker				= elem[elem.length-1];
			
		for(int i=trace.length-1; i>=0; i--)
		{
			StackTraceElement element = trace[i];
			Logger.error(tab+"%class("+element.getClassName()+")"+"%method("+element.getMethodName()+")"+"%line("+element.getLineNumber()+")"+(i==0?("\t\""+this.exception.getMessage()+"\""):("")),(i==trace.length-1),invoker);
			tab+="\t";
		}
	}

}