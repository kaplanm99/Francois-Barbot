package controllers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Semaphore;

import messages.Command;
import messages.RotateCommand;
import messages.StationaryCommand;
import peices.Bottle;
import peices.Cup;
import peices.Ingredient;
import peices.MalformedOrder;
import peices.Order;


public class Status 
{
	//Position of pouring device is position zero
	public static final int NUMBER_OF_CUPS = 6;
	public static final int NUMBER_OF_BOTTLES = 4;
	public static final int POSITION_OF_STIRRING_DEVICE = 3;
	public static final int POSITION_OF_ICE_DEVICE = 4;
	
	public static final Ingredient[] DEFAULT_INGREDIENTS = 
		{Ingredient.LEMON, Ingredient.KIWI_STAWBERRY, Ingredient.RED_BERRY, Ingredient.BLUE_RASPBEERY};
	public static final String[] CUP_IDENTIFIERS = 
		{"Alpha", "Foxtrot", "Echo", "Delta", "Charlie", "Bravo"};
	public static final boolean DEFAULT_IS_STIR_ONLINE = true;
	public static final boolean DEFAULT_IS_ICE_ONLINE = true;
	private static final String DIRECTORY_OF_OPS = "/home/michael/francios";
	
	private boolean arduinoConnectionTimedOut;
	
	private Cup[] cups;
	private Bottle[] bottles;
	private boolean iceOnline;
	private boolean stirOnline;
	private LinkedList<Order> queuedOrders;
	private LinkedList<MalformedOrder> malformedOrders;
	private int currentPosition;
	private boolean paused;
	private int forceRotate;
	
	private Semaphore moddingStatus; 
	//cuz I don't like having Java do my synchronization for me
	//I took CS 4820 for a reason
	
	public Status()
	{
		cups = new Cup[NUMBER_OF_CUPS];
		bottles = new Bottle[NUMBER_OF_BOTTLES];
		iceOnline = DEFAULT_IS_ICE_ONLINE;
		stirOnline = DEFAULT_IS_STIR_ONLINE;
		queuedOrders = new LinkedList<Order>();
		malformedOrders = new LinkedList<MalformedOrder>();
		currentPosition = 0;
		moddingStatus = new Semaphore(1);
		arduinoConnectionTimedOut = true;
		paused = false;
		forceRotate = 0;
		
		for (int i = 0; i < NUMBER_OF_CUPS; i++)
			cups[i] = new Cup(CUP_IDENTIFIERS[i]);
		
		for (int i = 0; i < NUMBER_OF_BOTTLES; i++)
			bottles[i] = new Bottle(DEFAULT_INGREDIENTS[i]);
	}
	
	public void setArduinoConnectionTimedOut(boolean b)
	{
		lock();
		arduinoConnectionTimedOut = true;
		unlock();
	}
	
	public boolean getArduinoConnectionTimedOut()
	{
		lock();
		boolean rtn = arduinoConnectionTimedOut;
		unlock();
		return rtn;
	}
	
	public void setBottle(int pos, Ingredient contents)
	{
		lock();
		bottles[pos] = new Bottle(contents);
		unlock();
	}
	
	private boolean isOrderMakeable(Order ord)
	{
		Set<Ingredient> ls = ord.getIngredientsNeeded();
		boolean canDoAll = true;
		for (Ingredient ing: ls)
		{
			boolean canDoThisOne = false;
			for (int i = 0; i < NUMBER_OF_BOTTLES; i++)
				if (ing == bottles[i].getIngredient())
					canDoThisOne = true;
			canDoAll = canDoAll && canDoThisOne;
		}
		if (ord.needsIce() && !iceOnline)
			canDoAll = false;
		if (ord.needsStir() && !stirOnline)
			canDoAll = false;
		return canDoAll;
	}
	
	public Cup[] getStatusOfCups()
	{
		lock();
		Cup[] rtn = new Cup[NUMBER_OF_CUPS];
		for (int i = 0; i < NUMBER_OF_CUPS; i++)
			rtn[i] = cups[(NUMBER_OF_CUPS + i - currentPosition) % NUMBER_OF_CUPS].clone();
		unlock();
		return rtn;
	}
	
	public Bottle getStatusOfBottle(int pos)
	{
		lock();
		Bottle rtn = bottles[pos % NUMBER_OF_BOTTLES].clone();
		unlock();
		return rtn;
	}
	
	public void addOrder(Order ord)
	{
		lock();
		if (isOrderMakeable(ord))
		{
			boolean handled = false;
			for (int i = 0; i < NUMBER_OF_CUPS; i++)
				if (cups[(NUMBER_OF_CUPS + NUMBER_OF_CUPS - i - currentPosition) % NUMBER_OF_CUPS].hasNoOrder() && (!handled))
				{
					cups[(NUMBER_OF_CUPS + NUMBER_OF_CUPS - i - currentPosition) % NUMBER_OF_CUPS].setOrder(ord);
					handled = true;
				}
			if (!handled)
				queuedOrders.add(ord);
		}
		else
		{
			malformedOrders.add(new MalformedOrder(ord));
		}
		unlock();
	}

	public void resetCup(int pos)
	{
		lock();
		cups[(NUMBER_OF_CUPS + pos - currentPosition) % NUMBER_OF_CUPS].reset();
		if (queuedOrders.size() > 0)
			cups[(NUMBER_OF_CUPS + pos - currentPosition) % NUMBER_OF_CUPS].setOrder(queuedOrders.removeFirst());
		unlock();
	}
	
	public void resetAllCups()
	{
		lock();
		for (int i = 0; i < NUMBER_OF_CUPS; i++)
		{
			if (cups[i].cupFinished())
			{
				cups[i].reset();
				if (queuedOrders.size() > 0)
					cups[i].setOrder(queuedOrders.removeFirst());
			}
		}
		unlock();
	}
	
	public void forceARotation()
	{
		lock();
		forceRotate++;
		unlock();
	}
	
	public Command getNextLogicalCommand() 
	{
		lock();
		Command msg = null;
		if (forceRotate > 0)
			msg = new RotateCommand();
		else if (paused)
			msg = null;
		else if (cups[(NUMBER_OF_CUPS - currentPosition) % NUMBER_OF_CUPS].shouldPour() || cups[(NUMBER_OF_CUPS - currentPosition + POSITION_OF_STIRRING_DEVICE) % NUMBER_OF_CUPS].shouldStir() || cups[(NUMBER_OF_CUPS - currentPosition + POSITION_OF_ICE_DEVICE) % NUMBER_OF_CUPS].shouldIce())
		{
			int[] ounces = new int[NUMBER_OF_BOTTLES];
			for (int i = 0; i < NUMBER_OF_BOTTLES; i++)
				if (cups[(NUMBER_OF_CUPS - currentPosition) % NUMBER_OF_CUPS].shouldPour())
					ounces[i] = cups[(NUMBER_OF_CUPS - currentPosition) % NUMBER_OF_CUPS].getOuncesNeededOfIngredient(bottles[i].getIngredient());
				else
					ounces[i] = 0;
			msg = new StationaryCommand(ounces, cups[(NUMBER_OF_CUPS - currentPosition) % NUMBER_OF_CUPS].shouldPour(), cups[(NUMBER_OF_CUPS - currentPosition + POSITION_OF_ICE_DEVICE) % NUMBER_OF_CUPS].shouldIce(), cups[(NUMBER_OF_CUPS - currentPosition + POSITION_OF_STIRRING_DEVICE) % NUMBER_OF_CUPS].shouldStir());
		}
		else
		{
			boolean shouldRotate = false;
			for (int i = 0; i < NUMBER_OF_CUPS; i++)
				if (cups[i].shouldPour() || cups[i].shouldIce() || cups[i].shouldStir())
					shouldRotate = true;
			if (shouldRotate)
				msg = new RotateCommand();
		}
		unlock();
		return msg;
	}
	
	private void lock()
	{
		try {
			moddingStatus.acquire();
		} catch (InterruptedException e) 
		{
			System.err.println("Error handling semaphore");
			System.exit(-1);
		}
	}
	
	private void unlock()
	{
		moddingStatus.release();
	}

	public void commandHandled(Command commandInProcess) 
	{
		lock();
		if (commandInProcess instanceof RotateCommand)
		{
			if (forceRotate > 0)
				forceRotate--;
			currentPosition = (1 + currentPosition) % NUMBER_OF_CUPS;
		}
		else if (commandInProcess instanceof StationaryCommand)
		{
			if (((StationaryCommand) commandInProcess).isPour())
			{
				cups[(NUMBER_OF_CUPS - currentPosition) % NUMBER_OF_CUPS].setHasBeenPoured(true);
			}
			if (((StationaryCommand) commandInProcess).isIce())
			{
				cups[(NUMBER_OF_CUPS + POSITION_OF_ICE_DEVICE - currentPosition) % NUMBER_OF_CUPS].setHasBeenIced(true);
			}
			if (((StationaryCommand) commandInProcess).isStir())
			{
				cups[(NUMBER_OF_CUPS + POSITION_OF_STIRRING_DEVICE - currentPosition) % NUMBER_OF_CUPS].setHasBeenStirred(true);
			}
		}
		unlock();
	}
	
	public boolean changePauseStat()
	{
		lock();
		paused = !paused;
		boolean temp = paused;
		unlock();
		return temp;
	}

	public static void readText(String toRead) throws IOException, InterruptedException
	{
		if (new File(DIRECTORY_OF_OPS + "/script.sh").exists())
			new File(DIRECTORY_OF_OPS + "/script.sh").delete();
		File myFile = new File(DIRECTORY_OF_OPS + "/script.sh");
		myFile.createNewFile();
		myFile.setExecutable(true);
		FileWriter fstream = new FileWriter(myFile);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("#!/bin/bash\n\necho " + toRead + " | festival --tts ");
		out.close();
		Process pro = Runtime.getRuntime().exec(DIRECTORY_OF_OPS + "/script.sh");
		pro.waitFor();
	}
	
	
}
