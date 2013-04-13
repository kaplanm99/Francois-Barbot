package peices;

public enum Ingredient 
{
	//TODO: flesh this out
	LEMON ("Lemon Juice"),
	KIWI_STAWBERRY ("Kiwi-Strawberry Juice"),
	CHERRY ("Cherry Juice"),
	BLUE_RASPBEERY ("Blue-Raspberry Juice"),
	RED_BERRY ("Red Berry");
	
	private final String name;
	
	Ingredient(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}