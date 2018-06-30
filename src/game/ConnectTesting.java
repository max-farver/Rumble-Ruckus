package game;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This Class is for testing purposes only. It will make connects to the server so I can test different players.
 * @author Clayton Nida
 *
 */
public class ConnectTesting
{
	private String serverHostName = "proj-309-tg-3.cs.iastate.edu";
	private int serverPortNumber = 4321;

	/**
	 * @throws IOException
	 */
	public ConnectTesting() throws IOException
	{
		try
		{
			Socket serverSocket = new Socket(serverHostName, serverPortNumber);
			
			Socket serverSocket2 = new Socket(serverHostName, 2222);
			System.out.println("Connecting to server...");
			System.out.println("Attempting connection to <" + serverHostName + "> on port " + serverPortNumber);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		ConnectTesting ct = new ConnectTesting();
	}
}
