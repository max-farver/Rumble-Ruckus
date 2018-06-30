package game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Used to communicate to all users/clients who are connected to it. It is to be hosted and Compiled on our server. a
 * @author Clayton Nida
 *
 */
public class GameServer
{
	static ArrayList<ListClientHandler> clientList = new ArrayList<ListClientHandler>();
	static ArrayList<ListClientHandlerLoc> clientList2 = new ArrayList<ListClientHandlerLoc>();
	static int messageNumber = 0;

	/**
	 * The main method will handle all connections that are being made to our servers open sockets and add them
	 * to our list of clients.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = null;
		ServerSocket serverSocket2 = null;
		int clientNum = 0;
		String userName = "";
		try
		{
			serverSocket = new ServerSocket(4321);
			serverSocket2 = new ServerSocket(2222);
			System.out.println(serverSocket);
			System.out.println(serverSocket2);
		}
		catch (IOException e)
		{
			System.out.println("Could not listen on ports");
			System.exit(-1);
		}
		while (true)
		{
			Socket clientSocket = null;
			Socket clientSocket2 = null;
			try
			{
				if (clientNum == 5)
				{
					System.out.println("Im sorry, this is the max number of players");
					// throw new java.lang.Error("Im sorry, this is the max
					// number of players");
					throw new IOException();
				}
				else
				{
					System.out.println("Waiting for client " + (clientNum + 1) + " to connect!");
					clientSocket = serverSocket.accept();
					clientSocket2 = serverSocket2.accept();
					System.out.println("Server got connected to client" + ++clientNum);
					ListClientHandler x = new ListClientHandler(clientSocket, clientNum, userName);
					ListClientHandlerLoc x2 = new ListClientHandlerLoc(clientSocket2, clientNum);
					Thread t = new Thread(x);
					Thread t2 = new Thread(x2);
					clientList.add(x);
					clientList2.add(x2);
					t.start();
					t2.start();
				}
			}
			catch (IOException e)
			{
				System.out.println("Accept failed");
				System.exit(-1);
			}
		}
	}

	/**
	 * This will listen for information sent from our connected clients and handle that information accordingly.
	 * @author Clayton Nida
	 *
	 */
	static class ListClientHandler implements Runnable
	{
		Socket sock;
		int num;
		String loginName;

		/**
		 * The constructor for the listener. It will use the given socket and number to communicate with.
		 * @param s The socket that we are connected to.
		 * @param n The number of client that we are.
		 * @param userName The user name of the client.
		 */
		ListClientHandler(Socket s, int n, String userName)
		{
			this.sock = s;
			num = n;
			this.loginName = userName;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			Scanner in;
			PrintWriter out;
			try
			{
				in = new Scanner(new BufferedInputStream(sock.getInputStream()));
				out = new PrintWriter(new BufferedOutputStream(sock.getOutputStream()));
				out.println("assign " + num);
				out.flush();
				while (true)
				{
					if (in.hasNext())
					{
						String s = in.nextLine();
						if (s.equals("exit"))
						{
							out.println("exit");
							out.flush();
						}
						else
						{
							handleRequest(s);
							out.flush();
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * This will take the information sent to the Server by the client, and relay it to all clients.
		 * @param cmd The command given to use by the client.
		 * @throws IOException
		 */
		void handleRequest(String cmd) throws IOException
		{
			PrintWriter out1;
			out1 = new PrintWriter(new BufferedOutputStream(sock.getOutputStream()));
			for (ListClientHandler x : clientList)
			{
				out1 = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
				out1.println("move " + cmd);
				out1.flush();
			}
		}
	}
	/**
	 * This will listen for location information sent from our connected clients and handle that information accordingly.
	 * @author Clayton Nida
	 *
	 */
	static class ListClientHandlerLoc implements Runnable
	{
		Socket sock2;
		int num2;

		/**
		 * The constructor for the listener. It will use the given socket and number to communicate with.
		 * @param s The socket that we are connected to.
		 * @param n The number of client that we are.
		 */
		ListClientHandlerLoc(Socket s, int n)
		{
			this.sock2 = s;
			num2 = n;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			Scanner in2;
			PrintWriter out2;
			try
			{
				in2 = new Scanner(new BufferedInputStream(sock2.getInputStream()));
				out2 = new PrintWriter(new BufferedOutputStream(sock2.getOutputStream()));
				while (true)
				{
					if (in2.hasNext())
					{
						String s = in2.nextLine();
						if (s.equals("exit"))
						{
							out2.println("exit");
							out2.flush();
						}
						else
						{
							handleRequestLoc(s);
							out2.flush();
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		/**
		 * This will take the information sent to the Server by the client, and relay it to all clients.
		 * @param cmd The command given to use by the client.
		 * @throws IOException
		 */
		void handleRequestLoc(String cmd) throws IOException
		{
			PrintWriter out2;
			out2 = new PrintWriter(new BufferedOutputStream(sock2.getOutputStream()));
			for (ListClientHandlerLoc x : clientList2)
			{
				out2 = new PrintWriter(new BufferedOutputStream(x.sock2.getOutputStream()));
				out2.println(cmd);
				out2.flush();
			}
		}
	}
}