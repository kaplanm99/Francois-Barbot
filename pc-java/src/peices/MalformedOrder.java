package peices;


public class MalformedOrder 
{
	private Order baseOrder;
	
	public MalformedOrder(Order ord) 
	{
		baseOrder = ord;
	}
	
	public Order getBaseOrder()
	{
		return baseOrder;
	}

}
