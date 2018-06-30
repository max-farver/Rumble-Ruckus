package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.ivan.xinput.exceptions.XInputNotLoadedException;

import lobby.Lobby;
import login.DBTools;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = -4584388369897487885L;
	private Thread thread;
	private Handler handler;
	private GameServerListener sl;
	private GameServerListenerLocation sll;
	private PrintWriter out;
	private PrintWriter out2;
	private Socket serverSocket;
	private Socket serverSocket2;
	private volatile BufferedImage bgIMG = null;
	private volatile BufferedImage loadScreen[];
	private volatile BufferedImage platform[];
	private volatile BufferedImage floor[];
	private volatile BufferedImage playerIcons[];
	public static Rectangle ground;
	public static Rectangle platLeft;
	public static Rectangle platRight;
	public static Rectangle platMid;
	private String serverHostName = "proj-309-tg-3.cs.iastate.edu";
	public static String username = "Loading";
	public String p1Username = "Loading";
	public String p2Username = "Loading";
	public String p3Username = "Loading";
	public String p4Username = "Loading";
	public String p1Character = "Loading";
	public String p2Character = "Loading";
	public String p3Character = "Loading";
	public String p4Character = "Loading";
	private String level = "winter";
	private String ping = "0";
	private String fps;
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String MYSQL_URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309tg3?"
			+ "user=dbu309tg3&password=CvZE1!5e";
	private static int interval = 0;
	public static int width, height;
	public int playerNum;
	private int frames = 0;
	private long ms = 0;
	private boolean running = false;
	private volatile boolean connected = false;
	public volatile static boolean loading = true;
	private volatile boolean victory = false;
	public volatile boolean ready = false;
	public volatile static boolean allPlayersReady = false;
	public volatile boolean[] readyArray = { false, false, false, false };
	private String loadingString = "LOADING";
	private Animation loadingAnimation;
	private int stock;
	private int seconds;
	public int request = 0;
	public Font timerFontEnd;
	public float fontSizeExpand = 22f;
	private static int oldInterval;
	public volatile boolean readLine = false;
	public int numOfPlayers = 0;
	private boolean runningLeft;
	private boolean runningRight;
	private boolean dataUpdated = false;

	public Game(String level, int stock, int seconds, String p1Username, String p2Username, String p3Username,
			String p4Username, String p1Character, String p2Character, String p3Character, String p4Character,
			String nameOfUser) throws XInputNotLoadedException
	{
		handler = new Handler();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = screenSize.height;
		width = screenSize.width;
		loadScreen = new BufferedImage[4];
		for (int i = 0; i < 4; i++)
		{
			loadScreen[i] = Sprite.loadSprite("img/loading" + i + ".png", "screen");
		}
		loadingAnimation = new Animation(loadScreen, 1);
		loadingAnimation.start();
		this.level = level;
		this.stock = stock;
		this.seconds = seconds;
		this.p1Username = p1Username;
		this.p2Username = p2Username;
		this.p3Username = p3Username;
		this.p4Username = p4Username;
		this.p1Character = p1Character;
		this.p2Character = p2Character;
		this.p3Character = p3Character;
		this.p4Character = p4Character;
		System.out.println("Detected Resolution: " + width + "x" + height);
		fps = "Fps: " + frames;
		System.out.println("Loading level: " + level);
		loadLevel(level);
		DBTools dao = new DBTools(MYSQL_DRIVER, MYSQL_URL);
		try
		{
			username = dao.readData(nameOfUser, "username", "username");
		}
		catch (Exception e1)
		{
			System.out.println("Could not load username");
		}
		new Window(width, height, "Rumble Ruckus", this);
		try
		{
			serverSocket = new Socket(serverHostName, 4321);
			serverSocket2 = new Socket(serverHostName, 2222);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		sl = new GameServerListener(serverSocket);
		sll = new GameServerListenerLocation(serverSocket2);
		new Thread(sl).start();
		new Thread(sll).start();
		new Thread(new latencyChecker()).start();
		try
		{
			out = new PrintWriter(new BufferedOutputStream(serverSocket.getOutputStream()));
			out2 = new PrintWriter(new BufferedOutputStream(serverSocket2.getOutputStream()));
			while (!connected)
			{
				Thread.sleep(1000);
			}
			try
			{
				timerFontEnd = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/RacingSansOne-Regular.ttf"));
			}
			catch (IOException | FontFormatException e)
			{
				e.printStackTrace();
			}
			// if (!spec1.equals("na"))
			// {
			// handler.addObject(new Player(0, 0, -1, "", spec1, 0, handler));
			// }
			// if (!spec2.equals("na"))
			// {
			// handler.addObject(new Player(0, 0, -2, "", spec2, 0, handler));
			// }
			if (checkUsernameValid(p1Username))
			{
				System.out.println(p1Username);
				System.out.println("Placing player one...");
				handler.addObject(new Player(((width / 2) - scale(640)) + scale(50), scale(height) - scale(100), 1,
						p1Character, p1Username, stock, handler));
				System.out.println(">Player one placed.");
				numOfPlayers++;
			}
			if (checkUsernameValid(p2Username))
			{
				System.out.println(p2Username);
				System.out.println("Placing player two...");
				handler.addObject(new Player(((width / 2) + scale(640)) - scale(170), scale(height) - scale(100), 2,
						p2Character, p2Username, stock, handler));
				System.out.println(">Player two placed.");
				numOfPlayers++;
			}
			if (checkUsernameValid(p3Username))
			{
				System.out.println(p3Username);
				System.out.println("Placing player three...");
				handler.addObject(new Player(((width / 2) - scale(420)) + scale(74) - scale(94),
						scale(height) - scale(350) - scale(100), 3, p3Character, p3Username, stock, handler));
				System.out.println(">Player three placed.");
				numOfPlayers++;
			}
			if (checkUsernameValid(p4Username))
			{
				System.out.println(p4Username);
				System.out.println("Placing player four...");
				handler.addObject(new Player(((width / 2) - scale(420)) - scale(74) + scale(94),
						scale(height) - scale(350) - scale(100), 4, p4Character, p4Username, stock, handler));
				System.out.println(">Player four placed.");
				numOfPlayers++;
			}
			System.out.println("Connected!");
			ready = true;
			sendReadySignal();
			if (numOfPlayers >= 1)
			{
				while (readyArray[0] == false)
				{
				}
			}
			if (numOfPlayers >= 2)
			{
				while (readyArray[1] == false)
				{
				}
			}
			if (numOfPlayers >= 3)
			{
				while (readyArray[2] == false)
				{
				}
			}
			if (numOfPlayers == 4)
			{
				while (readyArray[3] == false)
				{
				}
			}
			if (username.equals(p1Username))
			{
				playerNum = 1;
			}
			else if (username.equals(p2Username))
			{
				playerNum = 2;
			}
			else if (username.equals(p3Username))
			{
				playerNum = 3;
			}
			else if (username.equals(p4Username))
			{
				playerNum = 4;
			}
			else
			{
				playerNum = 5;
				playerNum++;
			}
			allPlayersReady = true;
			this.timer(5);
			while (interval != 0)
			{
				Thread.sleep(10);
			}
			loading = false;
			this.timer(seconds);
			this.addKeyListener(new KeyInput(out, playerNum, handler));
			System.out.println("Game Running");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private boolean checkUsernameValid(String username)
	{
		if (username.length() >= 5)
		{
			return true;
		}
		return false;
	}

	private void loadLevel(String level)
	{
		platform = new BufferedImage[3];
		playerIcons = new BufferedImage[4];
		floor = new BufferedImage[3];
		playerIcons[0] = Sprite.loadSprite("img/playerIcon1.png", "playerIcon");
		playerIcons[1] = Sprite.loadSprite("img/playerIcon2.png", "playerIcon");
		playerIcons[2] = Sprite.loadSprite("img/playerIcon3.png", "playerIcon");
		playerIcons[3] = Sprite.loadSprite("img/playerIcon4.png", "playerIcon");
		int num;
		for (int i = 0; i < 3; i++)
		{
			if (level.equals("forest"))
			{
				num = 13 + i;
				bgIMG = Sprite.loadSprite("forest/BG/BG.png", "BG");
				platform[i] = Sprite.loadSprite("forest/Tiles/" + num + ".png", "platform");
				int floorNum = 1 + i;
				floor[i] = Sprite.loadSprite("forest/Tiles/" + floorNum + ".png", "floor");
			}
			if (level.equals("winter"))
			{
				num = 14 + i;
				bgIMG = Sprite.loadSprite("winter/png/BG/BG.png", "BG");
				platform[i] = Sprite.loadSprite("winter/png/Tiles/" + num + ".png", "platform");
				int floorNum = 1 + i;
				floor[i] = Sprite.loadSprite("winter/png/Tiles/" + floorNum + ".png", "floor");
			}
			if (level.equals("desert"))
			{
				num = 14 + i;
				bgIMG = Sprite.loadSprite("desert/BG.png", "BG");
				platform[i] = Sprite.loadSprite("desert/Tile/" + num + ".png", "platform");
				int floorNum = 1 + i;
				floor[i] = Sprite.loadSprite("desert/Tile/" + floorNum + ".png", "floor");
			}
			if (level.equals("graveyard"))
			{
				num = 14 + i;
				bgIMG = Sprite.loadSprite("graveyard/BG.png", "BG");
				platform[i] = Sprite.loadSprite("graveyard/Tiles/" + num + ".png", "platform");
				int floorNum = 1 + i;
				floor[i] = Sprite.loadSprite("graveyard/Tiles/" + floorNum + ".png", "floor");
			}
		}
		ground = new Rectangle();
		ground.setBounds(((width / 2) - Game.scale(640)), height - Game.scale(100),
				Game.scale(floor[0].getWidth()) * scale(20), Game.scale(floor[0].getHeight()));
		platLeft = new Rectangle();
		platLeft.setBounds(((width / 2) - Game.scale(640)) + Game.scale(74), height - Game.scale(350),
				Game.scale(platform[0].getWidth()) * scale(10), Game.scale(platform[0].getHeight() / 4));
		platRight = new Rectangle();
		platRight.setBounds(((width / 2) + Game.scale(640) - Game.scale(384)) - Game.scale(74),
				height - Game.scale(350), Game.scale(platform[0].getWidth()) * scale(10),
				Game.scale(platform[0].getHeight() / 4));
		platMid = new Rectangle();
		platMid.setBounds((((width / 2) - Game.scale(384)) + Game.scale(192)), height - Game.scale(600),
				Game.scale(platform[0].getWidth()) * scale(10), Game.scale(platform[0].getHeight() / 4));
	}

	public static int scale(int x)
	{
		return (x * height) / 1080;
	}

	public static int deScale(int x)
	{
		return (x * 1080) / height;
	}

	private void sync()
	{
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getID() == playerNum && !loading && !tempObject.isDead())
			{
				String keyString = "loc " + playerNum + " " + deScale(tempObject.getX()) + " "
						+ deScale(tempObject.getY());
				out2.println(keyString);
				out2.flush();
				String keyString2 = "damage " + playerNum + " " + tempObject.getDamage();
				out2.println(keyString2);
				out2.flush();
			}
		}
	}

	private void sendReadySignal()
	{
		for (int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if (ready && loading && tempObject.getID() == playerNum)
			{
				String keyString = "ready " + playerNum + " " + "1";
				out2.println(keyString);
				out2.flush();
				ready = false;
			}
		}
	}

	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public class GameServerListener implements Runnable
	{
		Scanner in;

		public GameServerListener(Socket s)
		{
			try
			{
				in = new Scanner(new BufferedInputStream(s.getInputStream()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void run()
		{
			while (true)
			{
				if (in.hasNextLine())
				{
					String cmd = in.next();
					String s = in.nextLine();
					handleMoves(cmd, s);
				}
			}
		}
	}

	public class latencyChecker implements Runnable
	{
		public latencyChecker()
		{
		}

		@Override
		public void run()
		{
			while (true)
			{
				long lastTime = System.nanoTime();
				double amountOfTicks = 60.0;
				double ns = 1000000000 / amountOfTicks;
				double delta = 0;
				long timer = System.currentTimeMillis();
				while (true)
				{
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
					while (delta >= 1)
					{
						delta--;
					}
					if (System.currentTimeMillis() - timer > 1000)
					{
						try
						{
							InetAddress inet = InetAddress.getByName(serverHostName);
							long finish = 0;
							long start = new GregorianCalendar().getTimeInMillis();
							if (inet.isReachable(5000))
							{
								finish = new GregorianCalendar().getTimeInMillis();
								ms = (finish - start);
							}
						}
						catch (Exception e)
						{
							System.out.println("Exception:" + e.getMessage());
						}
					}
				}
			}
		}
	}

	public class GameServerListenerLocation implements Runnable
	{
		Scanner in;

		public GameServerListenerLocation(Socket s)
		{
			try
			{
				in = new Scanner(new BufferedInputStream(s.getInputStream()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void run()
		{
			while (true)
			{
				readLine = false;
				if (in.hasNextLine())
				{
					readLine = true;
					String cmd = in.next();
					String s = in.nextLine();
					handleLoc(cmd, s);
				}
			}
		}
	}

	public void handleLoc(String cmd, String s)
	{
		switch (cmd)
		{
		case "loc":
			String[] location = s.split(" ");
			int playerLoc = Integer.parseInt(location[1]);
			int locX = Integer.parseInt(location[2]);
			int locY = Integer.parseInt(location[3]);
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getID() == playerLoc && playerLoc != playerNum && !tempObject.isDead())
				{
					if (!tempObject.checkXandY(scale(locX), scale(locY)))
					{
						tempObject.setX(tempObject.getOldX());
						tempObject.setY(tempObject.getOldY());
					}
					tempObject.storeXandY(scale(locX), scale(locY));
				}
			}
			break;
		case "damage":
			String[] damage = s.split(" ");
			int playerID = Integer.parseInt(damage[1]);
			int playerDamage = Integer.parseInt(damage[2]);
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getID() == playerID && playerID != playerNum)
				{
					if (tempObject.getDamage() != playerDamage)
					{
						tempObject.setDamage(playerDamage);
					}
				}
			}
			break;
		case "ready":
			String[] readyString = s.split(" ");
			int playerStatus = Integer.parseInt(readyString[1]) - 1;
			int status = Integer.parseInt(readyString[2]);
			System.out.println("Player " + playerStatus + " is ready.");
			readyArray[playerStatus] = (boolean) (status == 1);
			break;
		case "exit":
			System.exit(-1);
			break;
		case "assign":
			String[] nums = s.split(" ");
			playerNum = Integer.parseInt(nums[1]);
			connected = true;
			break;
		default:
			System.out.println("client side: unknown command received LOCCC:" + cmd + s);
		}
	}

	public void handleMoves(String cmd, String s)
	{
		int moveNum;
		switch (cmd)
		{
		case "move":
			String[] move = s.split(" ");
			boolean pressed = false;
			if (move[2].equals("pressed"))
			{
				pressed = true;
			}
			moveNum = Integer.parseInt(move[1]);
			int player = Integer.parseInt(move[3]);
			for (int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getID() == player && player != playerNum && !tempObject.isDead())
				{
					if (pressed == true && tempObject.getInAction() == false)
					{
						while (readLine == false)
						{
						}
						if (moveNum == KeyEvent.VK_W)
						{
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
							tempObject.setAttacking(true);
							tempObject.setInAction(true);
							tempObject.setShooting(false);
							tempObject.setRunning(false);
							tempObject.setSpeedX(0);
							try
							{
								AudioInputStream audioInputStream = AudioSystem
										.getAudioInputStream(new File("sounds/slash.wav"));
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
							tempObject.setInAction(true);
							tempObject.setShooting(true);
							tempObject.setSpeedX(0);
							tempObject.setJumping(false);
							tempObject.setAttacking(false);
							tempObject.setRunning(false);
						}
						if (moveNum == KeyEvent.VK_Q)
						{
							tempObject.setShooting(false);
							tempObject.setSpeedX(0);
							tempObject.setAttacking(false);
							tempObject.setRunning(false);
							tempObject.setShielding(true);
							tempObject.setInAction(true);
						}
					}
					else
					{
						if (tempObject.getInAction() == false)
						{
							if (moveNum == KeyEvent.VK_A)
							{
								if (runningLeft == true)
								{
									tempObject.setSpeedX(0);
									tempObject.setRunning(false);
								}
							}
							if (moveNum == KeyEvent.VK_D)
							{
								if (runningRight == true)
								{
									tempObject.setSpeedX(0);
									tempObject.setRunning(false);
								}
							}
						}
						if (moveNum == KeyEvent.VK_Q && tempObject.getShielding())
						{
							tempObject.setShielding(false);
							tempObject.setInAction(false);
						}
					}
				}
			}
			break;
		case "exit":
			System.exit(-1);
			break;
		case "assign":
			String[] nums = s.split(" ");
			playerNum = Integer.parseInt(nums[1]);
			connected = true;
			break;
		default:
			System.out.println("client side: unknown command received:" + cmd);
		}
	}

	public void run()
	{
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		long hsTimer = System.currentTimeMillis();
		boolean canSync = false;
		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1)
			{
				tick();
				delta--;
			}
			if (running)
			{
				render();
				canSync = true;
			}
			frames++;
			if (canSync)
			{
				sync();
			}
			if (System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				fps = "Fps: " + frames;
				ping = "Ping: " + ms + " ms";
				frames = 0;
				Player.playOnce = 0;
				if (loading == true && allPlayersReady == false)
				{
					loadingString = loadingString + ".";
				}
			}
			if (System.currentTimeMillis() - hsTimer > 50)
			{

				hsTimer += 50;
				if (loading == true && allPlayersReady == false)
				{
					loadingAnimation.start();
					loadingAnimation.update();
				}
			}
		}
		try
		{
			Thread.sleep(10);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		if (victory)
		{
			victoryRender();
			victoryRender();
		}
		try
		{
			Thread.sleep(15000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("victory ended");
		stop();
	}

	private void tick()
	{
		handler.tick();
	}

	private void victoryRender()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Player[] players = checkVictory();
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(Sprite.loadSprite("img/Victory_Image.jpg", "screen"), 0, 0, this);
		int rectangleWidth = scale(420);
		int divider = scale(40);
		Rectangle playerStatsPlaceholder = new Rectangle(rectangleWidth, scale(350));
		Color rectangleBackground;
		int x = scale(60);
		int y = scale(60);
		for (int j = 0; j < 4; j++)
		{
			int redrawLoopLength = 4 - j;
			if (numOfPlayers < redrawLoopLength)
			{
				redrawLoopLength = numOfPlayers;
			}
			x = scale(60);
			for (int i = 0; i < redrawLoopLength; i++)
			{
				playerStatsPlaceholder.setLocation(x, y);
				rectangleBackground = new Color(232, 23, 23, 50);
				g2.setColor(rectangleBackground);
				g2.draw(playerStatsPlaceholder);
				g2.fill(playerStatsPlaceholder);
				rectangleBackground = new Color(0, 0, 0, 255);
				g2.setColor(rectangleBackground);
				g2.drawLine(x + scale(10), y + scale(100), x + rectangleWidth - scale(10), y + scale(100));
				g.setFont(timerFontEnd.deriveFont(100f));
				int lengthNumber = g.getFontMetrics().stringWidth(i + 1 + "");
				g.setFont(timerFontEnd.deriveFont(65f));
				String numberEnding = "";
				int length;
				if (i == 0)
				{
					numberEnding = "st";
				}
				else if (i == 1)
				{
					numberEnding = "nd";
				}
				else if (i == 2)
				{
					numberEnding = "rd";
				}
				else
				{
					numberEnding = "th";
				}
				length = lengthNumber + g.getFontMetrics().stringWidth(numberEnding);
				g.setFont(timerFontEnd.deriveFont(100f));
				g.drawString(i + 1 + "", x + rectangleWidth / 2 - length / 2, y + scale(85));
				g.setFont(timerFontEnd.deriveFont(65f));
				g.drawString(numberEnding, x + rectangleWidth / 2 - length / 2 + lengthNumber, y + scale(85));
				String playerName = "";
				if (i < players.length && players[i] != null)
				{
					playerName = players[i].getName();
				}
				try
				{
					g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Audiowide-Regular.ttf"))
							.deriveFont(40f));
				}
				catch (IOException | FontFormatException e)
				{
					e.printStackTrace();
				}
				length = g.getFontMetrics().stringWidth(playerName);
				g.drawString(playerName, x + rectangleWidth / 2 - length / 2, y + scale(150));
				x = x + rectangleWidth + divider;
				if (playerName.equals(username) && !dataUpdated)
				{
					DBTools gameEnd = new DBTools(MYSQL_DRIVER, MYSQL_URL);
					int deaths = this.stock - players[i].getStock();
					try
					{
						gameEnd.increment(username, "deaths", deaths);
						gameEnd.increment(username, "kills", players[i].getKills());
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
					if (numberEnding == "st")
					{
						try
						{
							gameEnd.increment(username, "wins", 1);
						}
						catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
					dataUpdated = true;
				}
			}
		}
		g.dispose();
		bs.show();
	}

	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (victory == false)
			g2.drawImage(bgIMG, 0, 0, this);
		int xPlacement;
		if (p1Username.length() < 5)
		{
		}
		else if (p1Character.equals("ninja"))
		{
			g2.drawImage(playerIcons[0], (width - Game.scale(230)) - Game.scale(400), -10, this);
		}
		else if (p1Character.equals("ninjaGirl"))
		{
			g2.drawImage(playerIcons[1], (width - Game.scale(230)) - Game.scale(400), -10, this);
		}
		else if (p1Character.equals("robot"))
		{
			g2.drawImage(playerIcons[2], (width - Game.scale(230)) - Game.scale(400), -10, this);
		}
		else
		{
			g2.drawImage(playerIcons[3], (width - Game.scale(230)) - Game.scale(400), -10, this);
		}
		if (p2Username.length() < 5)
		{
		}
		else if (p2Character.equals("ninja"))
		{
			g2.drawImage(playerIcons[0], (width - Game.scale(230)) - Game.scale(200), -10, this);
		}
		else if (p2Character.equals("ninjaGirl"))
		{
			g2.drawImage(playerIcons[1], (width - Game.scale(230)) - Game.scale(200), -10, this);
		}
		else if (p2Character.equals("robot"))
		{
			g2.drawImage(playerIcons[2], (width - Game.scale(230)) - Game.scale(200), -10, this);
		}
		else
		{
			g2.drawImage(playerIcons[3], (width - Game.scale(230)) - Game.scale(200), -10, this);
		}

		xPlacement = Game.scale(430);
		if (p3Username.length() < 5)
		{
		}
		else if (p3Character.equals("ninja"))
		{
			g2.drawImage(playerIcons[0], xPlacement, -10, this);
		}
		else if (p3Character.equals("ninjaGirl"))
		{
			g2.drawImage(playerIcons[1], xPlacement, -10, this);
		}
		else if (p3Character.equals("robot"))
		{
			g2.drawImage(playerIcons[2], xPlacement, -10, this);
		}
		else
		{
			g2.drawImage(playerIcons[3], xPlacement, -10, this);
		}

		xPlacement = xPlacement - Game.scale(200);
		if (p4Username.length() < 5)
		{
		}
		else if (p4Character.equals("ninja"))
		{
			g2.drawImage(playerIcons[0], xPlacement, -10, this);
		}
		else if (p4Character.equals("ninjaGirl"))
		{
			g2.drawImage(playerIcons[1], xPlacement, -10, this);
		}
		else if (p4Character.equals("robot"))
		{
			g2.drawImage(playerIcons[2], xPlacement, -10, this);
		}
		else
		{
			g2.drawImage(playerIcons[3], xPlacement, -10, this);
		}

		// left platform
		int x = ((width / 2) - Game.scale(640)) - Game.scale(128) + Game.scale(74);
		for (int i = 0; i < 3; i++)
		{
			x = x + Game.scale(128);
			g2.drawImage(platform[i], x, height - Game.scale(350), null);
		}
		// right platform
		x = ((width / 2) + Game.scale(640) - Game.scale(384)) - Game.scale(128) - Game.scale(74);
		for (int i = 0; i < 3; i++)
		{
			x = x + Game.scale(128);
			g2.drawImage(platform[i], x, height - Game.scale(350), null);
		}
		// mid platform
		x = (((width / 2) - Game.scale(384)) + Game.scale(192)) - Game.scale(128);
		for (int i = 0; i < 3; i++)
		{
			x = x + Game.scale(128);
			g2.drawImage(platform[i], x, height - Game.scale(600), null);
		}
		// floor
		x = ((width / 2) - Game.scale(640) - Game.scale(128));
		for (int i = 0; i < 10; i++)
		{
			x = x + Game.scale(128);
			if (i == 0)
			{
				g2.drawImage(floor[0], x, height - Game.scale(100), null);
			}
			if (i == 9)
			{
				g2.drawImage(floor[2], x, height - Game.scale(100), null);
			}
			else if (i > 0 && i < 9)
			{
				g2.drawImage(floor[1], x, height - Game.scale(100), null);
			}
		}
		g.setFont(new Font("Arial", Font.PLAIN, Game.scale(22)));
		g2.drawString(fps, 0, Game.scale(25));
		g2.drawString(ping + "", 0, Game.scale(50));
		if (p1Username.length() >= 5)
			g2.drawString(p1Username, (width - Game.scale(185)) - Game.scale(385), Game.scale(148));
		if (p2Username.length() >= 5)
			g2.drawString(p2Username, (width - Game.scale(110)) - Game.scale(260), Game.scale(148));
		if (p3Username.length() >= 5)
			g2.drawString(p3Username, Game.scale(500), Game.scale(148));
		if (p4Username.length() >= 5)
			g2.drawString(p4Username, Game.scale(300), Game.scale(148));
		g.setFont(new Font("Arial", Font.PLAIN, Game.scale(50)));
		if (interval != 0)
		{
			String drawTimer;
			if (interval <= 5)
			{
				if (oldInterval != interval)
				{
					fontSizeExpand = 40f;
				}
				fontSizeExpand *= 1.02f;
				if (fontSizeExpand > 110f)
				{
					fontSizeExpand = 110f;
				}
				oldInterval = interval;
				g.setFont(timerFontEnd.deriveFont(fontSizeExpand));
				drawTimer = "" + interval;
			}
			else if (interval % 60 < 10)
			{
				drawTimer = interval / 60 + ":0" + interval % 60;
			}
			else
			{
				drawTimer = interval / 60 + ":" + interval % 60;
			}
			int timeWidth = g.getFontMetrics().stringWidth(interval + "");
			g2.drawString(drawTimer, (width / 2) - timeWidth / 2,
					(g.getFont().getFontName().equals("Arial")) ? Game.scale(50) : Game.scale(540));
		}
		if (loading == true && allPlayersReady == false)
		{
			int loadingWidth = g.getFontMetrics().stringWidth(loadingString);
			g2.setColor(new Color(255, 255, 255));
			g2.drawImage(loadingAnimation.getSprite(), 0, 0, this);
			g2.drawString(loadingString, (width / 2) - loadingWidth / 2, Game.scale(50));
		}
		if (victory == true)
		{
			g2.drawImage(Sprite.loadSprite("img/Victory_Image.jpg", "screen"), 0, 0, this);
			running = false;
		}
		handler.render(g);
		g.dispose();
		bs.show();
	}

	public int timer(int seconds)
	{
		Timer timer;
		String secs = seconds + "";
		int delay = 1000;
		int period = 1000;
		timer = new Timer();
		interval = Integer.parseInt(secs);
		System.out.print(secs);
		timer.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{
				setInterval(timer);
			}
		}, delay, period);
		return 0;
	}

	private Player[] checkVictory()
	{
		Player[] players = new Player[numOfPlayers];
		if (loading == false)
		{
			if (handler.size() == 1)
			{
				victory = true;
				players[0] = (Player) handler.object.get(0);
			}
			else
			{
				// int[] places = new int[numOfPlayers];
				for (int i = 0; i < numOfPlayers; i++)
				{
					players[i] = (Player) handler.object.get(i);
				}
				for (int i = 1; i < numOfPlayers; i++)
				{
					int j = i - 1;
					Player temp = players[i];
					while (j >= 0 && players[j].getStock() < temp.getStock())
					{
						players[j + 1] = players[j];
						players[j] = temp;
						j--;
					}
				}
				if (players[0].getStock() == players[1].getStock() && interval == 0)
				{
				}
				else if (players[1].getStock() == 0)
				{
					victory = true;
				}
				else if (interval == 0 && (players[1].getStock() < players[0].getStock()))
				{
					victory = true;
				}
				else
				{
					if (players[1].getStock() > players[0].getStock())
					{
						System.out.print(players.toString() + "!ERROR!");
					}
				}
			}
		}
		return players;
	}

	private int setInterval(Timer timer)
	{
		if (interval == 1)
		{
			timer.cancel();
		}
		--interval;
		checkVictory();
		return interval;
	}

	public static int clamp(int var, int min, int max)
	{
		if (var >= max)
		{
			return var = max;
		}
		else if (var <= min)
		{
			return var = min;
		}
		else
		{
			return var;
		}
	}

	public static void main(String[] args) throws XInputNotLoadedException, IOException, URISyntaxException
	{
		Random random = new Random();
		int sessionKey = random.nextInt(9998) + 1;
		Login login = new Login(sessionKey);
		while (login.isBrowserOpen() == false)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		while (login.isBrowserClosed() == false)
		{
		}
		if (login.isGameLaunched())
		{
		}
		else
		{
			System.exit(1);
		}
		String name = "";
		DBTools counter = new DBTools(MYSQL_DRIVER, MYSQL_URL);
		try
		{
			name = counter.getNameFromKey(sessionKey);
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
		try
		{
			counter.writeData(name, "sessionKey", "0");
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		try
		{
			username = name;
			System.out.println(name);
			Lobby frame = new Lobby(name);
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);
			while (!(frame.isShowing()))
			{
			}
			login.closeLogin();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}