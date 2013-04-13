package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

import peices.Ingredient;
import peices.Order;

public class WebTalker extends Thread
{
	public static final String DATA_PULL_URL = "http://kaplanexperiments.com/francois/drinksOrdered.php";
	public static final String DRINK_DELIM = ";";
	public static final String PARAM_DELIM = ",";
	public static final int POLL_INTERVAL = 1000; //in millis
	
	private Status stat;
	private int indexOfNextDrinkToParse;
	
	public WebTalker(Status stat)
	{
		this.stat = stat;
		this.indexOfNextDrinkToParse = 0;
	}
	
	public void run()
	{
		while (true)
		{
			try 
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(DATA_PULL_URL).openStream()));
		        String line = reader.readLine();
		        if (line == null)
		        	continue;
		        reader.close();
		        
		        String[] drinks = line.split(DRINK_DELIM);
		        System.out.println(indexOfNextDrinkToParse);
		        System.out.println(drinks.length);
		        for (int i = indexOfNextDrinkToParse; i < drinks.length; i++)
		        {
		        	String[] params = drinks[i].split(PARAM_DELIM);
		        	String name = params[2];
		        	TreeMap<Ingredient, Integer> recipe = new TreeMap<Ingredient, Integer>();
		        	recipe.put(Status.DEFAULT_INGREDIENTS[0], stringToInt(params[3]));
		        	recipe.put(Status.DEFAULT_INGREDIENTS[1], stringToInt(params[4]));
		        	recipe.put(Status.DEFAULT_INGREDIENTS[2], stringToInt(params[5]));
		        	recipe.put(Status.DEFAULT_INGREDIENTS[3], stringToInt(params[6]));
		        	boolean stir = params[7].equals("1") ? true : false;
		        	boolean ice = params[8].equals("1") ? true : false;
		        	Order ord = new Order(recipe, stir, ice, name);
		        	System.out.println("registering order: " + ord.getStringRep());
		        	stat.addOrder(ord);
		        }
		        indexOfNextDrinkToParse = drinks.length;
			
			} catch (MalformedURLException e) 
			{
				
			} catch (IOException e) 
			{
				
			}
			
			try 
			{
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) 
			{
				
			}
		}
		
	}
	
	private Integer stringToInt(String string) 
	{
		int rtn = 0;
		for (int i = 0; i < string.length(); i++)
			rtn = (rtn * 10) + ((int) string.charAt(i)) - 48;
		return rtn;
	}
}
