package peices;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


public class Order 
{
	private TreeMap<Ingredient, Integer> recipe;
	private boolean needsStir;
	private boolean needsIce;
	private String name;
	
	
	public Order (TreeMap<Ingredient, Integer> ingredients, boolean needsToBeStirred, boolean needsToBeIced, String name)
	{
		this.recipe = ingredients;
		this.needsIce = needsToBeIced;
		this.needsStir = needsToBeStirred;
		this.name = name;
	}
	
	public Order clone()
	{
		return new Order(recipe, needsStir, needsIce, name);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getStringRep()
	{
		String rtn = name + ":";
		for (Entry<Ingredient, Integer> pair: recipe.entrySet())
		{
			rtn +=  " " + pair.getValue().intValue() + " ounces of " + pair.getKey().getName() + ",";
		}
		if (this.needsIce)
			rtn += " Iced";
		if (this.needsStir)
			rtn += " Stirred";
		return rtn;
	}
	
	public Set<Ingredient> getIngredientsNeeded() 
	{
		return recipe.keySet();
	}

	public boolean needsIce() 
	{
		return needsIce;
	}

	public boolean needsStir() 
	{
		return needsStir;
	}

	public int getOuncesNeededOfIngredient(Ingredient ingredient) 
	{
		Integer ounces = recipe.get(ingredient);
		if (ounces == null)
			return 0;
		return ounces.intValue();
	}

}
