package logger.mode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logger.Logger;

public class FileLogger implements LogWorker
{
	public static boolean CREATE_NEW_ON_START	= false;
	private String filename						= "temp";
	
	public FileLogger(String filename)
	{
		this.filename = filename+".txt";
		try
		{
			if(!test(this.filename))
			{
				FileOutputStream erasor = new FileOutputStream(this.filename);
    			erasor.write(new byte[]{});
    			erasor.close();
    	    }
    	}
    	catch (IOException e)	{ Logger.error("Could not prepare "+filename+" reason "+e.getMessage()); }
	}
	
	private static boolean test(String filename) throws IOException
	{
		File file = new File(filename);
		if(!file.exists())
		{
			file.createNewFile();
			return false;
		}
		else return !CREATE_NEW_ON_START;
	}
	
	public void error(String message,boolean info)
	{ echo(info?(Logger.getHumanReadableTimestamp()+"\tERROR   :\t"+message+"\n"):("\t\t\t\t\t"+message+"\n")); }

	public void info(String message, boolean info)
	{ echo(info?(Logger.getHumanReadableTimestamp()+"\tINFO    :\t"+message+"\n"):("\t\t\t\t\t"+message+"\n")); }

	public void warning(String message, boolean info)
	{ echo(info?(Logger.getHumanReadableTimestamp()+"\tWARNING :\t"+message+"\n"):("\t\t\t\t\t"+message+"\n")); }
	
	private final void echo(String message)
	{
		message = prepare(message);
		
		try
		{	
			PrintWriter bw = new PrintWriter(new FileWriter(filename,true));
			bw.printf("%s",message);
			bw.close();    			
		}
		catch(IOException e)
		{ Logger.error("Failed to log into "+filename+" reason "+e.getMessage()); }
	}

	private static String prepare(String message)
	{

		Matcher INVOKED	= Pattern.compile("%invoked\\((.*?)\\)").matcher(message);
		Matcher CLASS	= Pattern.compile("%class\\((.*?)\\)").matcher(message);
		Matcher METHOD	= Pattern.compile("%method\\((.*?)\\)").matcher(message);
		Matcher LINE	= Pattern.compile("%line\\((.*?)\\)").matcher(message);

		if (INVOKED.find())
		{
			String[] clz = INVOKED.group(1).split("\\$");
			message=message.replaceAll("%invoked\\((.*?)\\)", clz[clz.length-1]+": ");
		}
		if (CLASS.find())
		{
			String[] clz = CLASS.group(1).split("\\$");
			message=message.replaceAll("%class\\((.*?)\\)", clz[clz.length-1]);
		}
		if (METHOD.find())
		{
			message=message.replaceAll("%method\\((.*?)\\)", "."+METHOD.group(1)+"()");
		}
		if (LINE.find())
		{
	        message=message.replaceAll("%line\\((.*?)\\)", " at line "+LINE.group(1)+"");    	
		}

		return message;
	}
	
}