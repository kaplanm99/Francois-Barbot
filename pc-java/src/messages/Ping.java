package messages;

public class Ping extends Command
{
	public Ping()
	{
		
	}

	@Override
	public String getStringRepresentation() 
	{
		return Command.PING_FLAG + "";
	}
}
