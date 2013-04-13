package controllers;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.Semaphore;

import messages.Command;
import messages.Message;
import messages.Ping;
import messages.RotateCommand;
import messages.StationaryCommand;


public class ArduinoTalker extends Thread implements SerialPortEventListener
{	
	public static final String PORT_NAME = "COM10";
	public static final int TIME_OUT = 2000; //in millis
	public static final int DATA_RATE = 9600;
	public static final int PING_RATE = 100; // in millis
	
	private SerialPort serialPort;
	private InputStream input;
	private OutputStream output;
	private Status status;
	
	private Command commandInProcess;
	private long millisStampOfLastMessageReceived;
	private long millisStampOfLastMessageSent;
	private Semaphore modding;
	
	public ArduinoTalker(Status status)
	{
		this.status = status;
		boolean finishedConstruction = false;
		
		commandInProcess = null;
		millisStampOfLastMessageReceived = 0;
		millisStampOfLastMessageSent = 0;
		modding = new Semaphore(1);
		
		while (!finishedConstruction)
		{
			CommPortIdentifier portId = null;
			@SuppressWarnings("rawtypes")
			Enumeration ports = CommPortIdentifier.getPortIdentifiers();
	
			while (ports.hasMoreElements()) 
			{

				CommPortIdentifier currPortId = (CommPortIdentifier) ports.nextElement();
				System.out.println(currPortId.getName());
				if (currPortId.getName().equals(PORT_NAME))
				{
					portId = currPortId;
					break;
				}	
			}
	
			try 
			{
				serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
				serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	
				input = serialPort.getInputStream();
				output = serialPort.getOutputStream();
	
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
				finishedConstruction = true;
			} catch (Exception e) 
			{
				if (serialPort != null) 
				{
					serialPort.removeEventListener();
					serialPort.close();
				}
			}
		}
	}

	public synchronized void closeAndReboot() 
	{
		status.setArduinoConnectionTimedOut(true);
		if (serialPort != null) 
			
		{
			serialPort.removeEventListener();
			serialPort.close();
		}
		new ArduinoTalker(status).start();
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) 
	{
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try 
			{
				status.setArduinoConnectionTimedOut(false);
				lock();
				millisStampOfLastMessageReceived = new Date().getTime();
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);
				String flags = new String(chunk);
				for (int i = 0; i < flags.length(); i++)
				{
					char flag = new String(chunk).charAt(i);
					if (flag == Message.OPERATION_COMPLETE_FLAG)
					{
						System.out.println("received command confirmation");
						status.commandHandled(commandInProcess);
						commandInProcess = null;
					}
					else if (flag != Message.PING_RESPONSE_FLAG)
						System.out.println("received flag:" + flag + ".");
				}
				unlock();
			} catch (Exception e) 
			{
				closeAndReboot();
			}
		}
	}
	
	public void run()
	{
		boolean disconnected = false;
		while (!disconnected)
		{
			try 
			{
				//output.write(new RotateCommand().getStringRepresentation().getBytes());
				Command com = null;
				lock();
				if (commandInProcess == null)
					com = status.getNextLogicalCommand();
				if (com != null)
				{
					output.write(com.getStringRepresentation().getBytes());
					System.out.println("sending command: " + com.getStringRepresentation());
					commandInProcess = com;
				}
				else
				{
					output.write(new Ping().getStringRepresentation().getBytes());
				}
				this.millisStampOfLastMessageSent = new Date().getTime();
				unlock();
				Thread.sleep(PING_RATE);
			} catch (Exception e) 
			{
				System.out.println("WHY!!");
				closeAndReboot();
				disconnected = true;
			}
		}
	}
	
	public void lock()
	{
		try 
		{
			modding.acquire();
		} catch (InterruptedException e) 
		{
			System.err.println("Error handling semaphore");
			System.exit(-1);
		}
	}

	public void unlock()
	{
		modding.release();
	}
	

}
