package peices;


public class Bottle 
{
	private Ingredient contents;
	
	public Bottle(Ingredient contents) 
	{
		this.contents = contents;
	}

	public Bottle clone()
	{
		return new Bottle(contents);
	}

	public Ingredient getIngredient() 
	{
		return contents;
	}

}
