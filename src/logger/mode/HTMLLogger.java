package logger.mode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static logger.Util.getCallerName;
import logger.Logger;

public class HTMLLogger implements LogWorker
{
	public static boolean CREATE_NEW_ON_START	= false;
	private String filename						= "temp";
    private boolean once						= false;
    
	public HTMLLogger(String filename)
	{
		this.filename=filename+".html";
    		
		try
		{
			if(!test(this.filename))
			{
				FileOutputStream erasor = new FileOutputStream(this.filename);
    			erasor.write(new byte[]{});
    			erasor.close();
				echo("<!DOCTYPE html>\n<html>\n" +
						"<head>\n" +
						"\t<meta charset=\"UTF-8\">\n" +
						"\t<title>Log for "+getCallerName(4)+".class</title>\n" +
	    				"\t<style>\n" +
	    				"\t\t*\t\t\t\t\t\t\t\t{ margin: 0; padding: 0; font-size: 1.9ex; letter-spacing: .02em; }\n" +
						"\t\tbody\t\t\t\t\t\t\t{ background: #fff; font: .9em Arial; color: #222; }\n" +
						"\t\tdiv.new\t\t\t\t\t\t\t{ border-style: solid; border-color: #000; border-width: 1px 0; background: #222 !important; height: 5em; width: 100%; text-align: center; line-height:5em; color: #555; font-size: 1.3em; text-shadow: 0px 1px 1px #000; filter: dropshadow(color=#000, offx=0, offy=1);}\n" +
						"\t\tdiv.line\t\t\t\t\t\t{ border-style: solid; border-color: #fff; border-width: 0; }\n" +
						"\t\tdiv.warning\t\t\t\t\t\t{ border-color:#FCE42D !important; }\n" +
						"\t\tdiv.warning div.date\t\t\t{ background: #FCE42D !important; border-color:#E8CE05 !important; color: #877700 !important; }\n" +
						"\t\tdiv.warning div.message\t\t\t{ background: #FCF5C0 !important; }\n" +
						"\t\tdiv.warning div.invoker\t\t\t{ color: #877700 !important; background: #FCE42D !important; border-color:#E8CE05 !important; }\n" +
						"\t\tdiv.date\t\t\t\t\t\t{ float: left; padding: .5em !important; background: #F2F0F0; border-right: 1px dotted #ccc !important; width: 20em; text-align: right; height: 100%;}\n" +
						"\t\tdiv.line:last-child\t\t\t\t{ border-bottom: 1px dotted #ccc !important; }\n" +
						"\t\tdiv.message\t\t\t\t\t\t{ clear: right; padding: .5em 22em !important; background: #fff; height: 100%; }\n" +
						"\t\tspan.class\t\t\t\t\t\t{ color: #7400F0; }\n" +
						"\t\tspan.method\t\t\t\t\t\t{ color: #008CF0; font-style: italic; }\n" +
						"\t\tdiv.invoker\t\t\t\t\t\t{ border-style: dotted; border-width: 0 1px 0 0; border-color: #ccc; float: left; font-weight: bold; background: #F2F0F0; color: #222 !important; font-weight: normal; margin: -.5em 1em -.5em -1em; padding: .5em 1em; width: 10em; text-align: right; overflow: hidden; }\n" +
						"\t\tspan.line\t\t\t\t\t\t{ position: absolute; right: 1em; color: rgba(0,0,0,.3); }\n" +
						"\t\tspan.line font\t\t\t\t\t{ color: rgba(0,0,0,.6); font-weight: bold; }\n" +
						"\t\tdiv.error\t\t\t\t\t\t{ border-color:#DB1714 !important;}\n" +
						"\t\tdiv.error div.date\t\t\t\t{ background: #F7716F !important; border-color:#DB1714 !important; color: #fff !important; font-weight: bold; }\n" +
						"\t\tdiv.error div.message\t\t\t{ background: #FFDEDE !important; }\n" +
						"\t\tdiv.error div.invoker\t\t\t{ color: #fff !important; background: #F7716F !important;font-weight: bold; border-color:#DB1714 !important;}\n" +
	    				"\t</style>\n" +
	    				"</head>\n\n" +
	    				"<body>\n\n<!-- Template ends here -->\n\n");
    	    }
			if(!once && !CREATE_NEW_ON_START)
			{
				once=true;
				echo("<div class=\"line new\">new log file</div>\n");
			}
    	}
    	catch (IOException e)	{ Logger.error("Could not prepare HTML template of "+filename+" reason "+e.getMessage()); }
    }
    	
    private static boolean test(String filename) throws IOException
    {
    	File file = new File(filename);
    	if(!file.exists())
    	{
    		file.createNewFile();
    		return false;
    	}
    	else
    	{
    		if(CREATE_NEW_ON_START) return false;
    		else
    		{
    			BufferedReader input	= new BufferedReader( new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
    			String line				= input.readLine();
    			input.close();
    			if(line!=null && line.contains("html")) return true;
    		}
    	}
    	return false;
    }
		
    public void info(String message, boolean info)
    {
    	message=prepare(message);
    	
    	if(info)	echo("<div class=\"line\"><div class=\"date\">"+Logger.getHumanReadableTimestamp()+"</div><div class=\"message\">"+message+"</div></div>\n");
    	else		echo("<div class=\"line\"><div class=\"date\">&nbsp;</div><div class=\"message\">"+message+"</div></div>\n");
    }
    
    public void error(String message, boolean info)
    {
    	message=prepare(message);
    	
    	if(info)	echo("<div class=\"line error\"><div class=\"date\">"+Logger.getHumanReadableTimestamp()+"</div><div class=\"message\">"+message+"</div></div>\n");
    	else		echo("<div class=\"line error\"><div class=\"date\">&nbsp;</div><div class=\"message\">"+message+"</div></div>\n");
    }
    	
    public void warning(String message, boolean info)
    {
    	message=prepare(message);
    	
    	if(info)	echo("<div class=\"line warning\"><div class=\"date\">"+Logger.getHumanReadableTimestamp()+"</div><div class=\"message\">"+message+"</div></div>\n");
    	else		echo("<div class=\"line warning\"><div class=\"date\">&nbsp;</div><div class=\"message\">"+message+"</div></div>\n");
    }
    	
    private static String prepare(String message)
    {
    	message=message.replaceAll("\t", "&nbsp; &nbsp; ").replaceAll("\n", "<br/>");
    	
    	Matcher INVOKED	= Pattern.compile("%invoked\\((.*?)\\)").matcher(message);
    	Matcher CLASS	= Pattern.compile("%class\\((.*?)\\)").matcher(message);
    	Matcher METHOD	= Pattern.compile("%method\\((.*?)\\)").matcher(message);
    	Matcher LINE	= Pattern.compile("%line\\((.*?)\\)").matcher(message);
    	
    	
        if (INVOKED.find())
        {
        	String[] clz = INVOKED.group(1).split("\\$");
        	message=message.replaceAll("%invoked\\((.*?)\\)", "<div class=\"invoker\">"+clz[clz.length-1]+"</div>");
        }
        else
        {
        	message="<div class=\"invoker\">&nbsp;</div>"+message;
        }
        if (CLASS.find())
        {
        	String[] clz = CLASS.group(1).split("\\$");
        	message=message.replaceAll("%class\\((.*?)\\)", "<span class=\"class\">"+clz[clz.length-1]+"</span>");
        }
        if (METHOD.find())
        {
        	message=message.replaceAll("%method\\((.*?)\\)", ".<span class=\"method\">"+METHOD.group(1)+"()</span>");
        }
        if (LINE.find())
        {
        	message=message.replaceAll("%line\\((.*?)\\)", "<span class=\"line\">at line &nbsp;<font>"+LINE.group(1)+"</font></span>");
        	
        }
        
    	return message;
    }

    private final void echo(String message)
    {	
    	try
    	{	
    		PrintWriter bw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename,true), "UTF-8"));
    		bw.printf("%s",message);
    		bw.close();    			
    	}
    	catch(IOException e)
    	{ Logger.error("Failed to log into "+filename+" reason "+e.getMessage()); }	
    }

}