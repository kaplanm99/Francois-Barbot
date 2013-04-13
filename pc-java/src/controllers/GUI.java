package controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import peices.Cup;

public class GUI extends Thread
{
	public static final Font LABEL_FONT = new Font("Serif", Font.BOLD, 14);
	
	private JFrame frame;
	private JButton[] resetButtons = new JButton[Status.NUMBER_OF_CUPS];
	private JLabel[] statusLabel = new JLabel[Status.NUMBER_OF_CUPS];
	
	private final Status stat;
	
	private static final int frameHeight = 650;
	private static final int frameWidth = 650;
	private static final int bigRadius = 175;
	private static final int smallRadius = 40;
	
	public GUI (final Status stat)
	{
		this.stat = stat;
		
		frame = new JFrame("Francios Local Control");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(null);
		masterPanel.setBackground(Color.BLACK);
		
		for (int i = 0; i < Status.NUMBER_OF_CUPS; i++)
		{
			int centerX = (int) (frameWidth/2 + (bigRadius * Math.cos((0.75 + (((double) i) / Status.NUMBER_OF_CUPS)) * 2 * Math.PI)));
			int centerY = (int) (frameHeight/2 + (bigRadius * Math.sin((0.75 + (((double) i) / Status.NUMBER_OF_CUPS)) * 2 * Math.PI)));
			
			resetButtons[i] = new JButton("Reload Cup");
			resetButtons[i].setBounds(centerX - 100, centerY - 15, 200, 30);
			final int tmp = i;
			resetButtons[i].addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					stat.resetCup(tmp);
				}
	        });
			masterPanel.add(resetButtons[i]);
			
			statusLabel[i] = new JLabel("Awaiting Order", JLabel.CENTER);
			statusLabel[i].setBounds(centerX - 100, centerY + 15, 200, 30);
			statusLabel[i].setOpaque(true);
			statusLabel[i].setFont(LABEL_FONT);
			masterPanel.add(statusLabel[i]);
		}
		
		JButton resetAllCupsButton = new JButton("Reload All Cups");
		resetAllCupsButton.setBounds(frameWidth/2 - 100,  frameHeight/2 - 30, 200, 30);
		resetAllCupsButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				stat.resetAllCups();
			}
        });
		masterPanel.add(resetAllCupsButton);
		
		final JButton pauseSystemButton = new JButton("Pause");
		pauseSystemButton.setBounds(frameWidth/2 - 100,  frameHeight/2, 200, 30);
		pauseSystemButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean isPaused = stat.changePauseStat();
				if (isPaused)
					pauseSystemButton.setText("UN-Pause");
				else
					pauseSystemButton.setText("Pause");
			}
        });
		masterPanel.add(pauseSystemButton);
		
		JButton RotateTrayButton = new JButton("Rotate");
		RotateTrayButton.setBounds(frameWidth/2 - 100,  frameHeight/2 + 30, 200, 30);
		RotateTrayButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				stat.forceARotation();
			}
        });
		masterPanel.add(RotateTrayButton);
	
		
		frame.add(masterPanel);
		frame.setSize(frameWidth,frameHeight);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	
	
	
	public void run()
	{
		while (true)
		{
			Cup[] cups = stat.getStatusOfCups();
			for (int i = 0; i < Status.NUMBER_OF_CUPS; i++)
			{
				if (cups[i].hasNoOrder())
				{
					statusLabel[i].setText("Awaiting Order");
					statusLabel[i].setBackground(Color.WHITE);
					statusLabel[i].setForeground(Color.BLACK);
					resetButtons[i].setText("Reload Cup " + cups[i].getIndetifier());
					resetButtons[i].setEnabled(false);
				}
				else if (cups[i].cupFinished())
				{
					statusLabel[i].setText(cups[i].getOrder().getName() + " Completed");
					statusLabel[i].setBackground(Color.WHITE);
					statusLabel[i].setForeground(Color.GREEN);
					resetButtons[i].setText("Reload Cup " + cups[i].getIndetifier());
					resetButtons[i].setEnabled(true);
				}
				else
				{
					if (cups[i].shouldPour())
						statusLabel[i].setText(cups[i].getOrder().getName() + " (pour in progress)");
					else if (cups[i].shouldStir())
						statusLabel[i].setText(cups[i].getOrder().getName() + " (stir in progress)");
					else
						statusLabel[i].setText(cups[i].getOrder().getName() + " (ice in progress)");
					statusLabel[i].setBackground(Color.WHITE);
					statusLabel[i].setForeground(Color.RED);
					resetButtons[i].setText("Reload Cup " + cups[i].getIndetifier());
					resetButtons[i].setEnabled(false);
				}
			}
		}
	}

}
