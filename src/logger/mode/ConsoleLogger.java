package logger.mode;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import logger.CharacterDevice;
import logger.ConsoleDevice;
import logger.TextDevice;

public class ConsoleLogger implements LogWorker
{
	
	private final TextDevice console_out = (System.console() == null) ? new CharacterDevice(new PrintWriter(System.out, true)) : new ConsoleDevice(System.console());
	private final TextDevice console_err = (System.console() == null) ? new CharacterDevice(new PrintWriter(System.err, true)) : new ConsoleDevice(System.console());
	
	public void info(String message, boolean info)
	{ console_out.printf("%s\n",prepare(message)); }
	
	public void warning(String message, boolean info)
	{ console_out.printf("%s\n",prepare(message)); }
	
	public void error(String message,boolean info)
	{ console_err.printf("%s\n",prepare(message)); }
	
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