package peices;


public class Cup 
{
	private Order ord;
	private boolean hasBeenPoured;
	private boolean hasBeenStirred;
	private boolean hasBeenIced;
	private String indetifier;
	
	public Cup(String indetifier)
	{
		this.indetifier = indetifier;
		ord = null;
		hasBeenPoured = false;
		hasBeenStirred = false;
		hasBeenIced = false;
	}
	
	public Cup(Order ord, boolean hasBeenPoured, boolean hasBeenStirred, boolean hasBeenIced, String indetifier)
	{
		this.indetifier = indetifier;
		if (ord != null)
			this.ord = ord.clone();
		else
			this.ord = null;
		this.hasBeenIced = hasBeenIced;
		this.hasBeenPoured = hasBeenPoured;
		this.hasBeenStirred = hasBeenStirred;
	}
	
	public Cup clone()
	{
		return new Cup(ord, hasBeenPoured, hasBeenStirred, hasBeenIced, indetifier);
	}
	
	public String getIndetifier()
	{
		return indetifier;
	}

	public boolean cupFinished()
	{
		return (!hasNoOrder()) && (!shouldPour()) && (!shouldStir()) && (!shouldIce());
	}
	
	public Order getOrder()
	{
		return ord;
	}
	
	public void setHasBeenPoured(boolean b) 
	{
		this.hasBeenPoured = b;	
	}

	public void setHasBeenStirred(boolean b) 
	{
		this.hasBeenStirred = b;	
	}

	public void setHasBeenIced(boolean b) 
	{
		this.hasBeenIced = b;
	}

	public boolean shouldPour()
	{
		return (!hasNoOrder()) && !hasBeenPoured;
	}
	
	public boolean shouldIce() 
	{
		return (!hasNoOrder()) && (!hasBeenIced) && ord.needsIce() && (!shouldStir()) && (!shouldPour()); 
	}

	public boolean shouldStir() 
	{
		return (!hasNoOrder()) && (!hasBeenStirred) && ord.needsStir() && (!shouldPour()); 
	}

	public int getOuncesNeededOfIngredient(Ingredient ingredient) 
	{
		return ord.getOuncesNeededOfIngredient(ingredient);
	}

	public boolean hasNoOrder() 
	{
		return ord == null;
	}

	public void setOrder(Order ord) 
	{
		this.ord = ord;
	}

	public void reset() 
	{
		ord = null;
		this.hasBeenIced = false;
		this.hasBeenPoured = false;
		this.hasBeenStirred = false;
	}
}
