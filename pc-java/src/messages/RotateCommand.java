package messages;

public class RotateCommand extends Command
{
	public RotateCommand()
	{
		
	}

	@Override
	public String getStringRepresentation() 
	{
		return Command.ROTATE_FLAG + "";
	}
}
