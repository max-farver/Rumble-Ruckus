
package lobby;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Lobby server that goes into the server for Lobby class to connect to.
 * 
 * @author Hexiang Zhang
 */
public class LobbyServer {
	static ArrayList<ListClientHandler> lobbyClientList = new ArrayList<ListClientHandler>();
	static int messageNumber = 0;
	private static int userNum = 0;
	private static int gameNum = 0;
	private static int stock = 5;
	private static int seconds = 300;
	private static String map = "winter";
	private static String p1 = "na";
	private static String p2 = "na";
	private static String p3 = "na";
	private static String p4 = "na";
	private static String spec1 = "na";
	private static String spec2 = "na";
	private static String char1 = "na";
	private static String char2 = "na";
	private static String char3 = "na";
	private static String char4 = "na";
	private static boolean playable = true;

	/**
	 * Main method that calls a runnable class that receives commands from each
	 * client and sends it out to every client.
	 * 
	 * @param args
	 *            Not used
	 * @throws IOException
	 *             Sends exception when connection with ports has an error.
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket serverLobbySocket = null;
		try {
			serverLobbySocket = new ServerSocket(10884);
			System.out.println(serverLobbySocket);
		} catch (IOException e) {
			System.out.println("Could not listen on ports");
			System.exit(-1);
		}
		while (true) {
			Socket clientLobbySocket = null;
			try {
				clientLobbySocket = serverLobbySocket.accept();
				System.out.println("New User Online");
				// Come back to this!!!
				ListClientHandler x = new ListClientHandler(clientLobbySocket);
				Thread t = new Thread(x);
				lobbyClientList.add(x);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed");
				System.exit(-1);
			}
		}
	}

	/**
	 * Constructor is not used for our game. We use the runnable List Client Handler
	 * and Main for our game.
	 */
	LobbyServer() {

	}

	/**
	 * Static class that tracks when a new user connects to this server class
	 * 
	 * @author Hexiang Zhang
	 */
	static class ListClientHandler implements Runnable {
		Socket sock;
		int num;

		ListClientHandler(Socket sock) {
			this.sock = sock;
		}

		/**
		 * Runnable that listens to commands from clients
		 */
		public void run() {
			Scanner in;
			PrintWriter out;
			try {
				in = new Scanner(new BufferedInputStream(sock.getInputStream()));
				out = new PrintWriter(new BufferedOutputStream(sock.getOutputStream()));
				while (true) {
					if (in.hasNext()) {
						String cmd = in.next();
						String str = in.nextLine();
						System.out.println(cmd + " " + str);
						handleRequest(cmd, str);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Method to parse the command clients send
		 * 
		 * @param cmd
		 *            Command key
		 * @param s
		 *            String to be parsed
		 * @throws IOException
		 *             Error occurs when sending outputs to all clients has an issue
		 */
		void handleRequest(String cmd, String s) throws IOException {
			PrintWriter out;
			if (cmd.equals("play")) {
				if (playable) {
					for (ListClientHandler x : lobbyClientList) {
						out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
						out.println("play");
						out.flush();
					}
				}
				playable = false;
			} else if (cmd.equals("spec")) {
				Scanner scan = new Scanner(s);
				int i;
				for (i = 0; i < 2; i++) {
					String str = scan.next();
					if (i == 0) {
						spec1 = str;
					} else if (i == 1) {
						spec2 = str;
					}
				}

				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println("spec" + " " + spec1 + " " + spec2);
					out.flush();
				}
			} else if (cmd.equals("force")) {
				playable = true;
				userNum = 0;
				gameNum = 0;
				stock = 5;
				seconds = 300;
				map = "winter";
				p1 = "na";
				p2 = "na";
				p3 = "na";
				p4 = "na";
				spec1 = "na";
				spec2 = "na";
				char1 = "na";
				char2 = "na";
				char3 = "na";
				char4 = "na";
				spec1 = "na";
				spec2 = "na";

				String setup = "force" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1 + " " + p2
						+ " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2 + " " + char3
						+ " " + char4;
				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println(setup);
					out.flush();
				}

			} else if (cmd.equals("new")) {
				System.out.println("Received New");
				int i;
				Scanner scan = new Scanner(s);
				if (gameNum == 0) {
					gameNum++;
				}
				for (i = 0; i < 4; i++) {
					String str = scan.next();
					if (i == 0) {
						p1 = str;
					} else if (i == 1) {
						p2 = str;
					} else if (i == 2) {
						p3 = str;
					} else {
						p4 = str;
					}
				}
				String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1 + " " + p2
						+ " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2 + " " + char3
						+ " " + char4;
				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println(setup);
					out.flush();
				}
			} else if (cmd.equals("get")) {
				String setup = "info" + " " + gameNum + " " + p1 + " " + p2 + " " + " " + p3 + " " + p4 + " " + spec1
						+ " " + spec2;
				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println(setup);
					out.flush();
				}
			} else if (cmd.equals("message")) {
				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println("message" + s);
					out.flush();
				}
			} else if (cmd.equals("specmessage")) {
				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println("specmessage" + s);
					out.flush();
				}
			} else if (cmd.equals("setup")) {
				int i;
				Scanner scan = new Scanner(s);
				for (i = 0; i < 14; i++) {
					String str = scan.next();
					switch (i) {
					case 0:
						gameNum = Integer.parseInt(str);
						break;
					case 1:
						map = str;
						break;
					case 2:
						stock = Integer.parseInt(str);
						break;
					case 3:
						seconds = Integer.parseInt(str);
						break;
					case 4:
						p1 = str;
						break;
					case 5:
						p2 = str;
						break;
					case 6:
						p3 = str;
						break;
					case 7:
						p4 = str;
						break;
					case 8:
						spec1 = str;
						break;
					case 9:
						spec2 = str;
						break;
					case 10:
						char1 = str;
						break;
					case 11:
						char2 = str;
						break;
					case 12:
						char3 = str;
						break;
					case 13:
						char4 = str;
						break;
					default:
						break;
					}
				}
				String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1 + " " + p2
						+ " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2 + " " + char3
						+ " " + char4;
				for (ListClientHandler x : lobbyClientList) {
					out = new PrintWriter(new BufferedOutputStream(x.sock.getOutputStream()));
					out.println(setup);
					out.flush();
				}
			}
		}
	}
}
