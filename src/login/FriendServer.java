package login;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class FriendServer
{
	static ArrayList<Client> clientList = new ArrayList<Client>();
	static ArrayList<String> playerList = new ArrayList<String>();
	static int messageNumber = 0;
	static String names = "";

	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = null;
		try
		{
			serverSocket = new ServerSocket(10884);
			System.out.println(serverSocket);
		}
		catch (IOException e)
		{
			System.out.println("Could not listen on ports");
			System.exit(-1);
		}
		while (true)
		{
			Socket clientSocket = null;
			try
			{
				clientSocket = serverSocket.accept();
				System.out.println("Server got connected to newest client.");
				new EchoThread(clientSocket).start();
			}
			catch (IOException e)
			{
				System.out.println("Accept failed");
				System.exit(-1);
			}
		}
	}

	static class EchoThread extends Thread
	{
		protected Socket socket;

		public EchoThread(Socket clientSocket)
		{
			this.socket = clientSocket;
		}

		public void run()
		{
			InputStream inp = null;
			BufferedReader brinp = null;
			DataOutputStream out = null;
			try
			{
				inp = socket.getInputStream();
				brinp = new BufferedReader(new InputStreamReader(inp));
				out = new DataOutputStream(socket.getOutputStream());
			}
			catch (IOException e)
			{
				return;
			}
			String line;
			while (true)
			{
				try
				{
					line = brinp.readLine();
					String arr[] = line.split(" ", 2);
					System.out.println(line);
					if ((line == null) || line.equalsIgnoreCase("QUIT"))
					{
						socket.close();
						return;
					}
					else if (arr[0].equals("update"))
					{
						names = "";
						for (String x : playerList)
						{
							names += " ";
							names += x;
						}
						out.writeBytes(names + "\n\r");
						out.flush();
					}
					else if (arr[0].equals("add"))
					{
						clientList.add(new Client(this.socket, arr[1]));
						playerList.add(arr[1]);
						names = "";
						for (String x : playerList)
						{
							names += " ";
							names += x;
						}
						out.writeBytes(names + "\n\r");
						out.flush();
					}
					else
					{
						out.writeBytes(line + "\n\r");
						out.flush();
					}
				}
				catch (IOException e)
				{
					System.out.println("Client disconnected");
					for (Client x : clientList)
					{
						System.out.println(x.username);
						if (playerList.contains(x.username))
						{
							playerList.remove(x.username);
						}
					}
					return;
				}
			}
		}

		static String convertStreamToString(java.io.InputStream is)
		{
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	static class Client
	{
		Socket sock = null;
		String username = "";

		Client(Socket sock, String username)
		{
			this.sock = sock;
			this.username = username;
		}
	}
}
