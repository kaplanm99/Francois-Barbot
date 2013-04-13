package messages;

public class StationaryCommand extends Command
{
	private boolean stir;
	private boolean ice;
	private boolean pour;
	private int[] ounces;
	
	public StationaryCommand(int[] ounces, boolean pour, boolean ice, boolean stir)
	{
		this.ounces = ounces;
		this.ice = ice;
		this.stir = stir;
		this.pour = pour;
	}
	
	
	
	public boolean isStir() {
		return stir;
	}



	public boolean isIce() {
		return ice;
	}



	public boolean isPour() {
		return pour;
	}



	@Override
	public String getStringRepresentation() 
	{
		String rtn = BEGIN_STATIONARY_OPERATION_COMMAND_FLAG + "";
		for (int i = 0 ; i < ounces.length; i++)
		{
			String rep = "";
			int oz = ounces[i];
			if (oz == 0)
				rep = "0";
			else
				while (oz > 0)
				{
					rep = ((char) ((oz  % 10) + 48)) + rep;
					oz /= 10;
				}
			rtn += rep + Command.BOTTLE_DELIMITER;
		}
		if (stir)
			rtn += Command.STIR_FLAG;
		if (ice)
			rtn += Command.ICE_FLAG;
		return rtn + Command.END_STATIONARY_OPERATION_COMMAND_FLAG;
	}

}
