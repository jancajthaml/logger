package logger;

public class Util
{

    private final static MySecurityManager manager = new MySecurityManager();
    
    
	public static String getCallerName(int depth)
	{
		String[] clazz = manager.getCallerClassName(depth).split("\\$");
		return clazz[clazz.length-1];
	}
	
	static class MySecurityManager extends SecurityManager
	{
        public String getCallerClassName(int callStackDepth)
        { return getClassContext()[callStackDepth].getName(); }
    }

}