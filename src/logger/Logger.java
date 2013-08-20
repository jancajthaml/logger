package logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import logger.mode.ConsoleLogger;
import logger.mode.FileLogger;
import logger.mode.HTMLLogger;
import logger.mode.LogWorker;
import static logger.Util.getCallerName;

public class Logger
{
	
	private static final LogWorker DEFAULT					= new ConsoleLogger();
	private static final HashMap<String,LogWorker> options	= new HashMap<String,LogWorker>();
	
	public static enum Target
	{
		CONSOLE,
		FILE,
		HTML,
		STREAM;
	}

	public static void info(String msg)
	{ info(msg,true,""); }
	
	public static void error(String msg)
	{ error(msg,true,""); }

	public static void warning(String msg)
	{ warning(msg,true,""); }
	
	public static void clear()
	{
		//FIXME temp
		boolean inIDE = true;
		
		if(inIDE) clearForce();
		else try
		{ Runtime.getRuntime().exec((System.getProperty("os.name").contains("Windows"))?"cls":"clear"); }
		catch (Exception exception)
		{ clearForce(); }
	}
	
	public static void setTarget(Target target, Object ... options)
	{
		
		String clazz = getCallerName(3);
		
		switch(target)
		{
			case CONSOLE	: break;
			case STREAM		: break;
			case FILE		:
			case HTML		:
			{
				try
				{
					if(options.length!=1) throw new ClassCastException();
					
					File f = new File((String)options[0]);
					
					if(f.exists() && !f.canWrite()) throw new IOException();

					switch(target)
					{
						case FILE	: Logger.options.put(clazz, new FileLogger((String)options[0])); break;
						case HTML	: Logger.options.put(clazz, new HTMLLogger((String)options[0])); break;
						default		: break;
					}
					
				}
				catch(ClassCastException e)
				{
					error("BadParamException: usage : setTarget(Logger.Target target,String filename)");
					return;
				}
				catch(IOException e)
				{
					error("IOException: usage : setTarget(Logger.Target target,String filename)");
					return;
				}
			}			
		}
		warning("Logger started",true,clazz);
	}
	
	
	private static void clearForce()
	{
		try								{ Thread.sleep(10);		}
		catch (InterruptedException e)	{ e.printStackTrace();	}
		
		info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
	public static void info(String msg,boolean info,String invoker)
	{
		String clazz		= invoker.equalsIgnoreCase("")?getCallerName(4):invoker;
		LogWorker target	= options.get(clazz);
		
		if(target==null) target=Logger.DEFAULT;
		
		target.info(info?("%invoked("+clazz+")"+msg):msg,info);
	}

	public static void error(String msg, boolean info, String invoker)
	{
		String clazz		= invoker.equalsIgnoreCase("")?getCallerName(4):invoker;
		LogWorker target	= options.get(clazz);
		
		if(target==null) target=Logger.DEFAULT;
		
		target.error(info?("%invoked("+clazz+")"+msg):msg,info);
		
	}
	
	public static void warning(String msg, boolean info, String invoker)
	{
		String clazz		= invoker.equalsIgnoreCase("")?getCallerName(4):invoker;
		LogWorker target	= options.get(clazz);
		
		if(target==null) target=Logger.DEFAULT;
		
		target.warning(info?("%invoked("+clazz+")"+msg):msg,info);
		
	}
	
    public static String getHumanReadableTimestamp()
    { return new SimpleDateFormat("EEEE, d.M.yyyy  HH:mm:ss").format(new Date()); }
    
    static
    {
    	Runtime.getRuntime().addShutdownHook(new Thread()
    	{
    		public void run()
    		{
    			for(String invoke : options.keySet())
    				warning("Logger terminated",true,invoke);
    		}
    	});
    }

}