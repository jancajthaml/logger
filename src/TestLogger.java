
import static logger.Logger.error;
import static logger.Logger.info;
import logger.Logger;
import logger.exception.LoggableException;

public class TestLogger
{

	/**
	 * @param args
	 * @throws LoggableException 
	 */
	public static void main(String[] args){
		
		info("Cely program zacina neco delat");
		try {
			new Level1Nested().log();
		} catch (LoggableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		info("Program zkoncil");
		}
		
	}
	static
	{
		Logger.setTarget(Logger.Target.HTML, "./soubor1");
	}
	
	
	static class Level1Nested
	{
		static
		{
			Logger.setTarget(Logger.Target.HTML, "./soubor1");
		}
		public void log() throws LoggableException
		{
			new Level2Nested().log();
			info("Level 1 neco rekl PO");
		}
		static class Level2Nested{
			static
			{
				Logger.setTarget(Logger.Target.HTML, "./soubor1");
			}
			public void log() throws LoggableException
			{
				info("Level 2 neco rekl pred");
				new Level3Nested().log();
				info("Level 2 neco rekl po");
			}
			static class Level3Nested
			{
				static
				{
					Logger.setTarget(Logger.Target.HTML, "./soubor1");
				}
				
				public void log() throws LoggableException
				{
					info("Miska ma velkou hlavu");
					info("B");
					info("C");
					info("D");
					error("E");
					info("F");
					
					throw new LoggableException("Pozor!!! Honzik zakopnul o mys");
				}	
			}
		}
	}
	
}
