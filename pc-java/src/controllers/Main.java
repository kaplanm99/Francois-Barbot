package controllers;

public class Main 
{
	public static void main(String[] args)
	{
		Status stat = new Status();
		new GUI(stat).start();
		new WebTalker(stat).start();
		new ArduinoTalker(stat).start();
	}
}
