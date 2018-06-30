package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * This Class is how we communicate Player's movements to the Server.
 * 
 * @author Clayton Nida
 *
 */
public class KeyInput extends KeyAdapter
{
	private int moveNum;
	private PrintWriter os;
	private int playerNum;
	private boolean pressing = false;
	// private int lastPressed;
	private Handler handler;
	private boolean runningLeft;
	private boolean runningRight;
	private String keyStringPressed;
	private String keyStringReleased;

	/**
	 * This constructs our KeyInput class with a given PrintWriter/Output stream
	 * and a player number.
	 * 
	 * @param os
	 *            The output stream that is used.
	 * @param playerNum
	 *            The number of the player communicating.
	 */
	public KeyInput(PrintWriter os, int playerNum, Handler handler)
	{
		this.handler = handler;
		this.os = os;
		this.playerNum = playerNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e)
	{
		moveNum = e.getKeyCode();
		// if (!pressing || moveNum != lastPressed)
		// {
		// pressing = true;
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getID() == playerNum && tempObject.getInAction() == false && !tempObject.isDead())
			{
				if (moveNum == KeyEvent.VK_W)
				{
					keyStringPressed = moveNum + "" + " pressed " + playerNum;
					os.println(keyStringPressed);
					os.flush();
					if (tempObject.getDoubleJumped() == false)
					{
						if (tempObject.getJumped() < 2)
						{
							tempObject.setJumped(tempObject.getJumped() + 1);
						}
						else
						{
							tempObject.setDoubleJumped(true);
						}
						tempObject.setAttacking(false);
						tempObject.setY(tempObject.getY() - Game.scale(4));
						tempObject.setSpeedY(Game.scale(-20));
						tempObject.setJumping(true);
						tempObject.setRunning(false);
						tempObject.setShooting(false);
					}
				}
				if (moveNum == KeyEvent.VK_SPACE && !tempObject.getJumping())
				{
					keyStringPressed = moveNum + "" + " pressed " + playerNum;
					os.println(keyStringPressed);
					os.flush();
					tempObject.setAttacking(true);
					tempObject.setInAction(true);
					tempObject.setShooting(false);
					tempObject.setRunning(false);
					tempObject.setSpeedX(0);
					try
					{
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/slash.wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
					}
					catch (Exception ee)
					{
						ee.printStackTrace();
					}
				}
				if (moveNum == KeyEvent.VK_A)
				{
					keyStringPressed = moveNum + "" + " pressed " + playerNum;
					os.println(keyStringPressed);
					os.flush();
					tempObject.setSpeedX(Game.scale(-7));
					tempObject.setFacingLeft(true);
					if (!tempObject.getJumping())
					{
						tempObject.setRunning(true);
					}
					tempObject.setShooting(false);
					tempObject.setAttacking(false);
					runningLeft = true;
					runningRight = false;
				}
				if (moveNum == KeyEvent.VK_D)
				{
					keyStringPressed = moveNum + "" + " pressed " + playerNum;
					os.println(keyStringPressed);
					os.flush();
					tempObject.setAttacking(false);
					tempObject.setSpeedX(Game.scale(7));
					tempObject.setFacingLeft(false);
					if (!tempObject.getJumping())
					{
						tempObject.setRunning(true);
					}
					runningRight = true;
					runningLeft = false;
				}
				if (moveNum == KeyEvent.VK_E && tempObject.getJumping() == false)
				{
					keyStringPressed = moveNum + "" + " pressed " + playerNum;
					os.println(keyStringPressed);
					os.flush();
					tempObject.setInAction(true);
					tempObject.setShooting(true);
					tempObject.setSpeedX(0);
					tempObject.setJumping(false);
					tempObject.setAttacking(false);
					tempObject.setRunning(false);
				}
				if (moveNum == KeyEvent.VK_Q && !pressing)
				{
					keyStringPressed = moveNum + "" + " pressed " + playerNum;
					os.println(keyStringPressed);
					os.flush();
					tempObject.setShooting(false);
					tempObject.setSpeedX(0);
					tempObject.setAttacking(false);
					tempObject.setRunning(false);
					tempObject.setShielding(true);
					tempObject.setInAction(true);
					pressing = true;
				}
			}
		}
		// lastPressed = moveNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e)
	{
		moveNum = e.getKeyCode();
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getID() == playerNum && tempObject.getInAction() == false)
			{
				if (moveNum == KeyEvent.VK_A)
				{
					keyStringReleased = moveNum + "" + " released " + playerNum;
					os.println(keyStringReleased);
					os.flush();
					if (runningLeft == true)
					{
						tempObject.setSpeedX(0);
						tempObject.setRunning(false);
					}
				}
				if (moveNum == KeyEvent.VK_D)
				{
					keyStringReleased = moveNum + "" + " released " + playerNum;
					os.println(keyStringReleased);
					os.flush();
					if (runningRight == true)
					{
						tempObject.setSpeedX(0);
						tempObject.setRunning(false);
					}
				}
			}
			if ((tempObject.getID() == playerNum && moveNum == KeyEvent.VK_Q && tempObject.getShielding()))
			{
				keyStringReleased = moveNum + "" + " released " + playerNum;
				os.println(keyStringReleased);
				os.flush();
				tempObject.setShielding(false);
				tempObject.setInAction(false);
				pressing = false;
			}
		}
	}
}
