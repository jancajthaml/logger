package logger.mode;

public interface LogWorker
{

	void	info	(String message, boolean info);
	void	error	(String message, boolean info);
	void	warning	(String message, boolean info);
	
}
