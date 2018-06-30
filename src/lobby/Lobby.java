package lobby;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.ivan.xinput.exceptions.XInputNotLoadedException;

import game.Game;
import game.Login;
import login.DBTools;

public class Lobby extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelHome;
	private JPanel panelLobby;
	private JPanel panelLeaderboard;
	private JPanel panelPreLobby;
	private JPanel panelProfile;
	private JPanel panelFriends;
	private JPanel panelAdmin;
	private CardLayout cardLayout;

	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String MYSQL_URL = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309tg3?"
			+ "user=dbu309tg3&password=CvZE1!5e";
	private String user;

	private LobbyServerListener lobbyServerListener;
	private PrintWriter outLobby;
	private PrintWriter outGame;
	private PrintWriter outFriend;
	private Thread thread;
	private Socket serverLobbySocket;
	private String serverHostName = "proj-309-tg-3.cs.iastate.edu";

	private int lobbyServerPortNumber = 10887;

	private String testString = "1 winter 5 300 hexzha canida2 rmhilby na mfarver na ninja ninjaGirl robot na";

	private int gameNum = 0;
	private int returning = 0;
	private boolean joined = false;

	public Image img = new ImageIcon(this.getClass().getResource("/LobbyBG.png")).getImage();

	private int stock = 3;
	private int seconds = 1800;
	private int playerNum = 0;

	private String map = "winter";
	private String p1 = "na";
	private String p2 = "na";
	private String p3 = "na";
	private String p4 = "na";
	private String spec1 = "na";
	private String spec2 = "na";
	private String char1 = "na";
	private String char2 = "na";
	private String char3 = "na";
	private String char4 = "na";
	private boolean playable = true;

	private boolean initLeaderboard = false;
	private boolean gameRunning = false;
	private int players = 0;
	private int spectators = 0;
	private String me = "";

	LeaderBoardObj leaderObj[] = new LeaderBoardObj[5];

	int width = 1920;
	int height = 1080;
	LobbyServerListener LSL;
	FriendServerListener FSL;
	/**
	 * Start of Friends list variables
	 */
	private Socket serverFriendSocket;
	ArrayList<String> friends = new ArrayList<String>();
	/**
	 * End Friends list variables
	 */
	private int join = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lobby frame = new Lobby("admin");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection connection = null;
	private JTextField player1;
	private JTextField player2;
	private JTextField player3;
	private JTextField player4;
	private JTextField txtCharacterSelect;
	private JTextPane chatTextPane;
	private JLabel charP1;
	private JLabel charP2;
	private JLabel charP3;
	private JLabel charP4;

	JButton btnPrePlay;
	JButton btnMap;
	JButton btnStock;
	private JTextField txtPlayers;
	private JTextField txtPlayer1;
	private JTextField txtPlayer2;
	private JTextField txtPlayer3;
	private JTextField txtPlayer4;
	private JButton btnAdminBack;
	private JButton btnStop;
	private JButton btnSpectate;
	private JTextField txtSpectators;
	private JTextField txtSpec1;
	private JTextField txtSpec2;

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Lobby(String nameOfUser) throws Exception {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = screenSize.height;
		width = screenSize.width;
		try {
			serverLobbySocket = new Socket(serverHostName, 10884);
			serverFriendSocket = new Socket(serverHostName, 10886);
			System.out.println("Connecting to server...");
			System.out.println("Attempting connection to <" + serverHostName + "> on port " + lobbyServerPortNumber);
			System.out.println("Connecting to server...");
			System.out.println("Attempting connection to <" + serverHostName + "> on port " + 10887);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LSL = new LobbyServerListener(serverLobbySocket);
		FSL = new FriendServerListener(serverFriendSocket);
		new Thread(LSL).start();
		new Thread(FSL).start();
		try {
			outLobby = new PrintWriter(new BufferedOutputStream(serverLobbySocket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			outFriend = new PrintWriter(new BufferedOutputStream(serverFriendSocket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		user = nameOfUser;
		setTitle("Rumble Ruckus: Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(lobbyScale(100), lobbyScale(100), lobbyScale(804), lobbyScale(461));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		cardLayout = (CardLayout) contentPane.getLayout();
		initHome();
		initLobby();
		initLeaderboard();
		initPreLobby();
		initProfile();
		initFriends();
		initAdmin();

		contentPane.add(panelAdmin, "panelAdmin");
		contentPane.add(panelHome, "panelHome");
		contentPane.add(panelLeaderboard, "panelLeaderBoard");
		contentPane.add(panelLobby, "panelLobby");
		contentPane.add(panelPreLobby, "panelPreLobby");
		contentPane.add(panelProfile, "panelProfile");
		contentPane.add(panelFriends, "panelFriends");
		cardLayout.show(contentPane, "panelHome");
		panelHome.requestFocus();
		addToFriendsServer(user);
		outFriend.flush();
	}

	private void initAdmin() {
		panelAdmin = new BackPanel();

		panelAdmin.setLayout(null);

		btnAdminBack = new JButton("Back");
		btnAdminBack.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnAdminBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAdminBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelHome");
			}
		});
		btnAdminBack.setBounds(lobbyScale(269), lobbyScale(90), lobbyScale(157), lobbyScale(53));
		panelAdmin.add(btnAdminBack);

		btnStop = new JButton("Force Stop Games");
		btnStop.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					outLobby.println("force stop");
					outLobby.flush();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStop.setBounds(lobbyScale(269), lobbyScale(154), lobbyScale(157), lobbyScale(53));
		panelAdmin.add(btnStop);
	}

	private void initPreLobby() {

		panelPreLobby = new BackPanel();
		panelPreLobby.setLayout(null);

		btnSpectate = new JButton("Spectate");
		btnSpectate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				me = "spec";
				if (spec1.equals("na")) {
					try {
						outLobby.println("spec" + " " + user + " " + spec2);
						outLobby.flush();
						cardLayout.show(contentPane, "panelLobby");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if (spec2.equals("na")) {
					try {
						outLobby.println("spec" + " " + spec1 + " " + user);
						outLobby.flush();
						cardLayout.show(contentPane, "panelLobby");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnSpectate.setFont(new Font("Tahoma", Font.BOLD, lobbyScale(13)));
		btnSpectate.setBounds(lobbyScale(345), lobbyScale(89), lobbyScale(130), lobbyScale(40));
		panelPreLobby.add(btnSpectate);

		txtSpectators = new JTextField();
		txtSpectators.setText("Spectators:");
		txtSpectators.setHorizontalAlignment(SwingConstants.CENTER);
		txtSpectators.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtSpectators.setEditable(false);
		txtSpectators.setColumns(10);
		txtSpectators.setBounds(lobbyScale(345), lobbyScale(156), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtSpectators);

		txtSpec1 = new JTextField();
		txtSpec1.setText("Spectator 1:");
		txtSpec1.setHorizontalAlignment(SwingConstants.CENTER);
		txtSpec1.setEditable(false);
		txtSpec1.setColumns(10);
		txtSpec1.setBounds(lobbyScale(345), lobbyScale(187), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtSpec1);

		txtSpec2 = new JTextField();
		txtSpec2.setText("Spectator 2:");
		txtSpec2.setHorizontalAlignment(SwingConstants.CENTER);
		txtSpec2.setEditable(false);
		txtSpec2.setColumns(10);
		txtSpec2.setBounds(lobbyScale(345), lobbyScale(219), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtSpec2);

		btnPrePlay = new JButton("");
		if (gameRunning == false) {
			btnPrePlay.setText("Play");
		} else {
			btnPrePlay.setText("Join");
		}
		btnPrePlay.setFont(new Font("Tahoma", Font.BOLD, lobbyScale(13)));
		btnPrePlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!gameRunning) {

					try {
						playerNum = 0;
						outLobby.println("new" + " " + user + " " + p2 + " " + p3 + " " + p4);
						outLobby.flush();

					} catch (Exception e) {
						e.printStackTrace();
					}

					gameRunning = true;

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}

					cardLayout.show(contentPane, "panelLobby");

				} else if (gameRunning) {
					if (p2.equals("na")) {
						try {
							playerNum = 1;
							outLobby.println("new" + " " + p1 + " " + user + " " + p3 + " " + p4);
							outLobby.flush();

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (p3.equals("na")) {
						try {
							playerNum = 2;
							outLobby.println("new" + " " + p1 + " " + p2 + " " + user + " " + p4);
							outLobby.flush();

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (p4.equals("na")) {
						try {
							playerNum = 3;
							outLobby.println("new" + " " + p1 + " " + p2 + " " + p3 + " " + user);
							outLobby.flush();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}

					cardLayout.show(contentPane, "panelLobby");
				}
				joined = true;
			}
		});
		btnPrePlay.setBounds(lobbyScale(198), lobbyScale(89), lobbyScale(130), lobbyScale(40));
		panelPreLobby.add(btnPrePlay);

		txtPlayers = new JTextField();
		txtPlayers.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPlayers.setEditable(false);
		txtPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayers.setText("Players:");
		txtPlayers.setBounds(lobbyScale(198), lobbyScale(156), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtPlayers);
		txtPlayers.setColumns(10);

		txtPlayer1 = new JTextField();
		txtPlayer1.setText("Player 1:");
		txtPlayer1.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayer1.setEditable(false);
		txtPlayer1.setColumns(10);
		txtPlayer1.setBounds(lobbyScale(198), lobbyScale(187), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtPlayer1);

		txtPlayer2 = new JTextField();
		txtPlayer2.setText("Player 2:");
		txtPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayer2.setEditable(false);
		txtPlayer2.setColumns(10);
		txtPlayer2.setBounds(lobbyScale(198), lobbyScale(219), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtPlayer2);

		txtPlayer3 = new JTextField();
		txtPlayer3.setText("Player 3:");
		txtPlayer3.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayer3.setEditable(false);
		txtPlayer3.setColumns(10);
		txtPlayer3.setBounds(lobbyScale(198), lobbyScale(251), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtPlayer3);

		txtPlayer4 = new JTextField();
		txtPlayer4.setText("Player 4:");
		txtPlayer4.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayer4.setEditable(false);
		txtPlayer4.setColumns(10);
		txtPlayer4.setBounds(lobbyScale(198), lobbyScale(282), lobbyScale(130), lobbyScale(20));
		panelPreLobby.add(txtPlayer4);

		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelHome");
			}
		});
		btnBack.setBounds(lobbyScale(10), lobbyScale(11), lobbyScale(130), lobbyScale(40));
		panelPreLobby.add(btnBack);

	}

	public void initProfile() {
		DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
		panelProfile = new BackPanel();

		panelProfile.setLayout(null);
		JLabel lblRRImage = new JLabel("");
		Image img = null;
		try {
			img = dbCall.readBlob(user);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		Image frontImg = img.getScaledInstance(lobbyScale(150), lobbyScale(150), Image.SCALE_SMOOTH);
		lblRRImage.setBounds(lobbyScale(305), lobbyScale(44), lobbyScale(150), lobbyScale(150));
		lblRRImage.setIcon((new ImageIcon(frontImg)));
		panelProfile.add(lblRRImage);
		JLabel textProfileInstruc = new JLabel(
				"<html>How to set profile image:<br/>1) Click on a picture.<br/>" + "2) Already finished!</html>",
				SwingConstants.LEFT);
		textProfileInstruc.setFont(new Font("Segoe UI Symbol", Font.PLAIN, lobbyScale(15)));
		textProfileInstruc.setForeground(Color.white);
		textProfileInstruc.setBounds(lobbyScale(85), lobbyScale(80), lobbyScale(192), lobbyScale(100));
		panelProfile.add(textProfileInstruc);
		JButton btnPicture1 = new JButton();
		Image imgStart = null;
		imgStart = new ImageIcon(this.getClass().getResource("/spongebob.png")).getImage();
		Image newImage = imgStart.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
		btnPicture1.setIcon(new ImageIcon(newImage));
		btnPicture1.setBounds(lobbyScale(30), lobbyScale(260), lobbyScale(100), lobbyScale(100));
		btnPicture1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				try {
					dbCall.writeBlob(user, "spongebob.png");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Image img1 = null;
				img1 = new ImageIcon(this.getClass().getResource("/spongebob.png")).getImage();
				Image tempImage = img1.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
				btnPicture1.setIcon(new ImageIcon(tempImage));
				tempImage = tempImage.getScaledInstance(lobbyScale(150), lobbyScale(150), Image.SCALE_SMOOTH);
				lblRRImage.setIcon(new ImageIcon(tempImage));
				cardLayout.show(contentPane, "panelProfile");
			}
		});
		panelProfile.add(btnPicture1);
		imgStart = null;
		JButton btnPicture2 = new JButton();
		imgStart = new ImageIcon(this.getClass().getResource("/patrick.png")).getImage();
		newImage = imgStart.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
		btnPicture2.setIcon(new ImageIcon(newImage));
		btnPicture2.setBounds(lobbyScale(180), lobbyScale(260), lobbyScale(100), lobbyScale(100));
		btnPicture2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				try {
					dbCall.writeBlob(user, "patrick.png");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Image img1 = null;
				img1 = new ImageIcon(this.getClass().getResource("/patrick.png")).getImage();
				Image tempImage = img1.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
				btnPicture2.setIcon(new ImageIcon(tempImage));
				tempImage = tempImage.getScaledInstance(lobbyScale(150), lobbyScale(150), Image.SCALE_SMOOTH);
				lblRRImage.setIcon(new ImageIcon(tempImage));
				cardLayout.show(contentPane, "panelProfile");
			}
		});
		panelProfile.add(btnPicture2);
		imgStart = null;
		JButton btnPicture3 = new JButton();
		imgStart = new ImageIcon(this.getClass().getResource("/squidward.png")).getImage();
		newImage = imgStart.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
		btnPicture3.setIcon(new ImageIcon(newImage));
		btnPicture3.setBounds(lobbyScale(330), lobbyScale(260), lobbyScale(100), lobbyScale(100));
		btnPicture3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				try {
					dbCall.writeBlob(user, "squidward.png");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Image img1 = null;
				img1 = new ImageIcon(this.getClass().getResource("/squidward.png")).getImage();
				Image tempImage = img1.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
				btnPicture3.setIcon(new ImageIcon(tempImage));
				tempImage = tempImage.getScaledInstance(lobbyScale(150), lobbyScale(150), Image.SCALE_SMOOTH);
				lblRRImage.setIcon(new ImageIcon(tempImage));
				cardLayout.show(contentPane, "panelProfile");
			}
		});
		panelProfile.add(btnPicture3);
		imgStart = null;
		JButton btnPicture4 = new JButton();
		imgStart = new ImageIcon(this.getClass().getResource("/mindy.png")).getImage();
		newImage = imgStart.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
		btnPicture4.setIcon(new ImageIcon(newImage));
		btnPicture4.setBounds(lobbyScale(480), lobbyScale(260), lobbyScale(100), lobbyScale(100));
		btnPicture4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				try {
					dbCall.writeBlob(user, "mindy.png");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Image img1 = null;
				img1 = new ImageIcon(this.getClass().getResource("/mindy.png")).getImage();
				Image tempImage = img1.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
				btnPicture4.setIcon(new ImageIcon(tempImage));
				tempImage = tempImage.getScaledInstance(lobbyScale(150), lobbyScale(150), Image.SCALE_SMOOTH);
				lblRRImage.setIcon(new ImageIcon(tempImage));
				cardLayout.show(contentPane, "panelProfile");
			}
		});
		panelProfile.add(btnPicture4);
		imgStart = null;
		JButton btnPicture5 = new JButton();
		imgStart = new ImageIcon(this.getClass().getResource("/dennis.png")).getImage();
		newImage = imgStart.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
		btnPicture5.setIcon(new ImageIcon(newImage));
		btnPicture5.setBounds(lobbyScale(630), lobbyScale(260), lobbyScale(100), lobbyScale(100));
		btnPicture5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				try {
					dbCall.writeBlob(user, "dennis.png");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Image img1 = null;
				img1 = new ImageIcon(this.getClass().getResource("/dennis.png")).getImage();
				Image tempImage = img1.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_DEFAULT);
				btnPicture5.setIcon(new ImageIcon(tempImage));
				tempImage = tempImage.getScaledInstance(lobbyScale(150), lobbyScale(150), Image.SCALE_SMOOTH);
				lblRRImage.setIcon(new ImageIcon(tempImage));
				cardLayout.show(contentPane, "panelProfile");
			}
		});
		panelProfile.add(btnPicture5);
		JButton btnProfileBack = new JButton("Back");
		btnProfileBack.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnProfileBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelHome");
				panelHome.requestFocus();
			}
		});
		btnProfileBack.setBounds(lobbyScale(10), lobbyScale(11), lobbyScale(89), lobbyScale(23));
		panelProfile.add(btnProfileBack);
	}

	public void initFriends() throws Exception {
		panelFriends = new BackPanel();
		panelFriends.setLayout(null);
		JTextField txtRumbleRuckusFriends;
		JTable tableFriends;
		JButton btnLeaderBoardBack = new JButton("Back");
		btnLeaderBoardBack.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnLeaderBoardBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelHome");
			}
		});
		btnLeaderBoardBack.setBounds(lobbyScale(10), lobbyScale(11), lobbyScale(89), lobbyScale(23));
		panelFriends.add(btnLeaderBoardBack);
		txtRumbleRuckusFriends = new JTextField();
		txtRumbleRuckusFriends.setFont(new Font("Arial", Font.PLAIN, lobbyScale(30)));
		txtRumbleRuckusFriends.setBounds(lobbyScale(140), lobbyScale(11), lobbyScale(500), lobbyScale(35));
		txtRumbleRuckusFriends.setText("List of Players Online");
		txtRumbleRuckusFriends.setHorizontalAlignment(SwingConstants.CENTER);
		txtRumbleRuckusFriends.setEditable(false);
		panelFriends.add(txtRumbleRuckusFriends);
		txtRumbleRuckusFriends.setColumns(10);
		tableFriends = new JTable();
		tableFriends.setRowHeight(lobbyScale(40));
		tableFriends.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		tableFriends.setEnabled(false);
		Object[][] data = { { null, null }, { null, null }, { null, null }, { null, null }, { null, null } };
		tableFriends.setModel(new DefaultTableModel(data, new String[] { null, "Name" }) {
			/**
			 * Get column data type for table
			 */
			private static final long serialVersionUID = 8116864206034995656L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return ImageIcon.class;
				case 1:
					return String.class;
				default:
					return Object.class;
				}
			}
		});
		TableColumnModel columnModel = tableFriends.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(150);
		tableFriends.setBounds(lobbyScale(10), lobbyScale(69), lobbyScale(200), lobbyScale(200));
		panelFriends.add(tableFriends);
		JButton btnFriendRefresh = new JButton("Refresh");
		btnFriendRefresh.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnFriendRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < 5; i++) {
					tableFriends.setValueAt(null, i, 0);
					tableFriends.setValueAt(null, i, 1);
				}
				requestFriendUpdate();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				DBTools imageCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				int k = 0;
				for (int i = 0; i < 5; i++) {
					if (i < friends.size()) {
						if (!friends.get(i).equals(user)) {
							if (friends.get(i) != null) {
								tableFriends.setValueAt(friends.get(i), k, 1);
								try {
									Image playerImg = imageCall.readBlob(friends.get(i));
									playerImg = playerImg.getScaledInstance(lobbyScale(40), lobbyScale(40),
											Image.SCALE_SMOOTH);
									ImageIcon finalPlayerImg = new ImageIcon(playerImg);
									tableFriends.setValueAt(finalPlayerImg, k, 0);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								k++;
							}
						}
					}
				}
				outFriend.flush();
				cardLayout.show(contentPane, "panelFriends");
				panelFriends.requestFocus();
			}
		});
		btnFriendRefresh.setBounds(lobbyScale(10), lobbyScale(300), lobbyScale(89), lobbyScale(23));
		panelFriends.add(btnFriendRefresh);
	}

	public void initHome() throws IOException {
		panelHome = new BackPanel();
		panelHome.setLayout(null);
		JButton btnProfile = new JButton("Profile");
		btnProfile.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnProfile.setBounds(lobbyScale(47), lobbyScale(260), lobbyScale(135), lobbyScale(38));
		btnProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelProfile");
				panelProfile.requestFocus();
			}
		});
		panelHome.add(btnProfile);

		if (user.equals("admin")) {
			JButton btnAdmin = new JButton("Admin");
			btnAdmin.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
			btnAdmin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btnAdmin.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					cardLayout.show(contentPane, "panelAdmin");
				}
			});

			btnAdmin.setBounds(lobbyScale(50), lobbyScale(20), lobbyScale(135), lobbyScale(38));
			panelHome.add(btnAdmin);
		}

		JButton btnLobbyBack = new JButton("Back");
		btnLobbyBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnLobbyBack.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnLobbyBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				setVisible(false);

				Random random = new Random();
				int sessionKey = random.nextInt(9998) + 1;
				Login login = new Login(sessionKey);
				while (login.isBrowserOpen() == false) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while (login.isBrowserClosed() == false) {
				}
				if (login.isGameLaunched()) {
				}
				String name = "";
				DBTools counter = new DBTools(MYSQL_DRIVER, MYSQL_URL);
				try {
					name = counter.getNameFromKey(sessionKey);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					counter.writeData(name, "sessionKey", "0");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					login.closeLogin();
				} catch (Exception e) {
					e.printStackTrace();
				}

				setVisible(true);

			}
		});

		btnLobbyBack.setBounds(lobbyScale(50), lobbyScale(80), lobbyScale(135), lobbyScale(38));
		panelHome.add(btnLobbyBack);

		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPlay.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (joined == false) {
					try {
						outLobby.println("get info");
						outLobby.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}

					if (!p1.equals("na")) {
						gameRunning = true;
					}
					cardLayout.show(contentPane, "panelPreLobby");
				} else {
					cardLayout.show(contentPane, "panelLobby");
				}
			}
		});
		btnPlay.setBounds(lobbyScale(50), lobbyScale(320), lobbyScale(135), lobbyScale(38));
		panelHome.add(btnPlay);

		JButton btnLeaderboard = new JButton("Leaderboard");
		btnLeaderboard.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnLeaderboard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelLeaderBoard");
				panelLeaderboard.requestFocus();
			}
		});
		btnLeaderboard.setBounds(lobbyScale(600), lobbyScale(320), lobbyScale(135), lobbyScale(38));
		panelHome.add(btnLeaderboard);
		JLabel lblChooseAdventure = new JLabel("<html>Choose your Character...<br/>Choose your Fate!</html>",
				SwingConstants.LEFT);
		lblChooseAdventure.setForeground(Color.white);
		lblChooseAdventure.setFont(new Font("Segoe UI Symbol", Font.PLAIN, lobbyScale(15)));
		lblChooseAdventure.setBounds(lobbyScale(430), lobbyScale(280), lobbyScale(192), lobbyScale(38));
		panelHome.add(lblChooseAdventure);
		JLabel lblRRImage = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/rumbleRuckusIcon.png")).getImage();
		Image frontImg = img.getScaledInstance(lobbyScale(240), lobbyScale(111), Image.SCALE_SMOOTH);
		lblRRImage.setBounds(lobbyScale(273), lobbyScale(110), lobbyScale(240), lobbyScale(111));
		lblRRImage.setIcon((new ImageIcon(frontImg)));
		panelHome.add(lblRRImage);
		JButton btnFriends = new JButton("Friends");
		btnFriends.setFont(new Font("Arial", Font.PLAIN, lobbyScale(15)));
		btnFriends.setBounds(lobbyScale(50), lobbyScale(200), lobbyScale(135), lobbyScale(38));
		btnFriends.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelFriends");
				panelFriends.requestFocus();
			}
		});
		panelHome.add(btnFriends);
	}

	public void initLeaderboard() {
		JTextField txtRumbleRuckusLeader;
		JTable tableLeaderBoard;
		panelLeaderboard = new BackPanel();
		panelLeaderboard.setLayout(null);
		JButton btnLeaderBoardBack = new JButton("Back");
		btnLeaderBoardBack.setFont(new Font("Arial", Font.PLAIN, lobbyScale(14)));
		btnLeaderBoardBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelHome");
				panelHome.requestFocus();
			}
		});
		btnLeaderBoardBack.setBounds(lobbyScale(10), lobbyScale(11), lobbyScale(89), lobbyScale(23));
		panelLeaderboard.add(btnLeaderBoardBack);
		txtRumbleRuckusLeader = new JTextField();
		txtRumbleRuckusLeader.setFont(new Font("Arial", Font.PLAIN, lobbyScale(30)));
		txtRumbleRuckusLeader.setText("Rumble Ruckus Leader Board");
		txtRumbleRuckusLeader.setHorizontalAlignment(SwingConstants.CENTER);
		txtRumbleRuckusLeader.setEditable(false);
		txtRumbleRuckusLeader.setBounds(lobbyScale(140), lobbyScale(11), lobbyScale(500), lobbyScale(35));
		panelLeaderboard.add(txtRumbleRuckusLeader);
		txtRumbleRuckusLeader.setColumns(10);
		String type = "kills";
		String user = "";
		String kills = "";
		String deaths = "";
		String wins = "";
		DBTools dbCall = new DBTools(MYSQL_DRIVER, MYSQL_URL);
		for (int i = 1; i < 6; i++) {
			try {
				user = dbCall.readData("" + i + "", "id", "username");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				kills = dbCall.readData("" + i + "", "id", "kills");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				deaths = dbCall.readData("" + i + "", "id", "deaths");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				wins = dbCall.readData("" + i + "", "id", "wins");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			leaderObj[i - 1] = new LeaderBoardObj(user, kills, deaths, wins);
		}
		tableLeaderBoard = new JTable();
		tableLeaderBoard.setRowHeight(lobbyScale(40));
		tableLeaderBoard.setFont(new Font("Arial", Font.PLAIN, lobbyScale(20)));
		tableLeaderBoard.setEnabled(false);
		tableLeaderBoard.setModel(new DefaultTableModel(
				new Object[][] { { "1", null, null, null, null }, { "2", null, null, null, null },
						{ "3", null, null, null, null }, { "4", null, null, null, null }, { "5", null, null, null }, },
				new String[] { "Rank", "Name", "Wins", "Kills", "Deaths" }));
		tableLeaderBoard.setBounds(lobbyScale(10), lobbyScale(69), lobbyScale(760), lobbyScale(200));
		panelLeaderboard.add(tableLeaderBoard);

		JButton btnKills = new JButton("Kills");
		btnKills.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnKills.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					leaderObj = dbCall.readEntrySorted("Kills");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				tableLeaderBoard.setValueAt(leaderObj[0].getUserName(), 0, 1);
				tableLeaderBoard.setValueAt(leaderObj[1].getUserName(), 1, 1);
				tableLeaderBoard.setValueAt(leaderObj[2].getUserName(), 2, 1);
				tableLeaderBoard.setValueAt(leaderObj[3].getUserName(), 3, 1);
				tableLeaderBoard.setValueAt(leaderObj[4].getUserName(), 4, 1);

				tableLeaderBoard.setValueAt(leaderObj[0].getWins(), 0, 2);
				tableLeaderBoard.setValueAt(leaderObj[1].getWins(), 1, 2);
				tableLeaderBoard.setValueAt(leaderObj[2].getWins(), 2, 2);
				tableLeaderBoard.setValueAt(leaderObj[3].getWins(), 3, 2);
				tableLeaderBoard.setValueAt(leaderObj[4].getWins(), 4, 2);

				tableLeaderBoard.setValueAt(leaderObj[0].getKills(), 0, 3);
				tableLeaderBoard.setValueAt(leaderObj[1].getKills(), 1, 3);
				tableLeaderBoard.setValueAt(leaderObj[2].getKills(), 2, 3);
				tableLeaderBoard.setValueAt(leaderObj[3].getKills(), 3, 3);
				tableLeaderBoard.setValueAt(leaderObj[4].getKills(), 4, 3);

				tableLeaderBoard.setValueAt(leaderObj[0].getDeaths(), 0, 4);
				tableLeaderBoard.setValueAt(leaderObj[1].getDeaths(), 1, 4);
				tableLeaderBoard.setValueAt(leaderObj[2].getDeaths(), 2, 4);
				tableLeaderBoard.setValueAt(leaderObj[3].getDeaths(), 3, 4);
				tableLeaderBoard.setValueAt(leaderObj[4].getDeaths(), 4, 4);
			}
		});
		btnKills.setBounds(lobbyScale(466), lobbyScale(46), lobbyScale(152), lobbyScale(23));
		panelLeaderboard.add(btnKills);

		JButton btnWins = new JButton("Wins");
		btnWins.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnWins.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					leaderObj = dbCall.readEntrySorted("wins");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				tableLeaderBoard.setValueAt(leaderObj[0].getUserName(), 0, 1);
				tableLeaderBoard.setValueAt(leaderObj[1].getUserName(), 1, 1);
				tableLeaderBoard.setValueAt(leaderObj[2].getUserName(), 2, 1);
				tableLeaderBoard.setValueAt(leaderObj[3].getUserName(), 3, 1);
				tableLeaderBoard.setValueAt(leaderObj[4].getUserName(), 4, 1);

				tableLeaderBoard.setValueAt(leaderObj[0].getWins(), 0, 2);
				tableLeaderBoard.setValueAt(leaderObj[1].getWins(), 1, 2);
				tableLeaderBoard.setValueAt(leaderObj[2].getWins(), 2, 2);
				tableLeaderBoard.setValueAt(leaderObj[3].getWins(), 3, 2);
				tableLeaderBoard.setValueAt(leaderObj[4].getWins(), 4, 2);

				tableLeaderBoard.setValueAt(leaderObj[0].getKills(), 0, 3);
				tableLeaderBoard.setValueAt(leaderObj[1].getKills(), 1, 3);
				tableLeaderBoard.setValueAt(leaderObj[2].getKills(), 2, 3);
				tableLeaderBoard.setValueAt(leaderObj[3].getKills(), 3, 3);
				tableLeaderBoard.setValueAt(leaderObj[4].getKills(), 4, 3);

				tableLeaderBoard.setValueAt(leaderObj[0].getDeaths(), 0, 4);
				tableLeaderBoard.setValueAt(leaderObj[1].getDeaths(), 1, 4);
				tableLeaderBoard.setValueAt(leaderObj[2].getDeaths(), 2, 4);
				tableLeaderBoard.setValueAt(leaderObj[3].getDeaths(), 3, 4);
				tableLeaderBoard.setValueAt(leaderObj[4].getDeaths(), 4, 4);
			}
		});
		btnWins.setBounds(lobbyScale(314), lobbyScale(46), lobbyScale(152), lobbyScale(23));
		panelLeaderboard.add(btnWins);
		JButton btnName = new JButton("Name");
		btnName.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					leaderObj = dbCall.readEntrySorted("username");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				tableLeaderBoard.setValueAt(leaderObj[0].getUserName(), 0, 1);
				tableLeaderBoard.setValueAt(leaderObj[1].getUserName(), 1, 1);
				tableLeaderBoard.setValueAt(leaderObj[2].getUserName(), 2, 1);
				tableLeaderBoard.setValueAt(leaderObj[3].getUserName(), 3, 1);
				tableLeaderBoard.setValueAt(leaderObj[4].getUserName(), 4, 1);

				tableLeaderBoard.setValueAt(leaderObj[0].getWins(), 0, 2);
				tableLeaderBoard.setValueAt(leaderObj[1].getWins(), 1, 2);
				tableLeaderBoard.setValueAt(leaderObj[2].getWins(), 2, 2);
				tableLeaderBoard.setValueAt(leaderObj[3].getWins(), 3, 2);
				tableLeaderBoard.setValueAt(leaderObj[4].getWins(), 4, 2);

				tableLeaderBoard.setValueAt(leaderObj[0].getKills(), 0, 3);
				tableLeaderBoard.setValueAt(leaderObj[1].getKills(), 1, 3);
				tableLeaderBoard.setValueAt(leaderObj[2].getKills(), 2, 3);
				tableLeaderBoard.setValueAt(leaderObj[3].getKills(), 3, 3);
				tableLeaderBoard.setValueAt(leaderObj[4].getKills(), 4, 3);

				tableLeaderBoard.setValueAt(leaderObj[0].getDeaths(), 0, 4);
				tableLeaderBoard.setValueAt(leaderObj[1].getDeaths(), 1, 4);
				tableLeaderBoard.setValueAt(leaderObj[2].getDeaths(), 2, 4);
				tableLeaderBoard.setValueAt(leaderObj[3].getDeaths(), 3, 4);
				tableLeaderBoard.setValueAt(leaderObj[4].getDeaths(), 4, 4);
			}
		});
		btnName.setBounds(lobbyScale(162), lobbyScale(46), lobbyScale(152), lobbyScale(23));
		panelLeaderboard.add(btnName);
		JButton btnDeaths = new JButton("Deaths");
		btnDeaths.setFont(new Font("Arial", Font.PLAIN, lobbyScale(12)));
		btnDeaths.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					leaderObj = dbCall.readEntrySorted("deaths");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				tableLeaderBoard.setValueAt(leaderObj[0].getUserName(), 0, 1);
				tableLeaderBoard.setValueAt(leaderObj[1].getUserName(), 1, 1);
				tableLeaderBoard.setValueAt(leaderObj[2].getUserName(), 2, 1);
				tableLeaderBoard.setValueAt(leaderObj[3].getUserName(), 3, 1);
				tableLeaderBoard.setValueAt(leaderObj[4].getUserName(), 4, 1);

				tableLeaderBoard.setValueAt(leaderObj[0].getWins(), 0, 2);
				tableLeaderBoard.setValueAt(leaderObj[1].getWins(), 1, 2);
				tableLeaderBoard.setValueAt(leaderObj[2].getWins(), 2, 2);
				tableLeaderBoard.setValueAt(leaderObj[3].getWins(), 3, 2);
				tableLeaderBoard.setValueAt(leaderObj[4].getWins(), 4, 2);

				tableLeaderBoard.setValueAt(leaderObj[0].getKills(), 0, 3);
				tableLeaderBoard.setValueAt(leaderObj[1].getKills(), 1, 3);
				tableLeaderBoard.setValueAt(leaderObj[2].getKills(), 2, 3);
				tableLeaderBoard.setValueAt(leaderObj[3].getKills(), 3, 3);
				tableLeaderBoard.setValueAt(leaderObj[4].getKills(), 4, 3);

				tableLeaderBoard.setValueAt(leaderObj[0].getDeaths(), 0, 4);
				tableLeaderBoard.setValueAt(leaderObj[1].getDeaths(), 1, 4);
				tableLeaderBoard.setValueAt(leaderObj[2].getDeaths(), 2, 4);
				tableLeaderBoard.setValueAt(leaderObj[3].getDeaths(), 3, 4);
				tableLeaderBoard.setValueAt(leaderObj[4].getDeaths(), 4, 4);
			}
		});
		btnDeaths.setBounds(lobbyScale(618), lobbyScale(46), lobbyScale(152), lobbyScale(23));
		panelLeaderboard.add(btnDeaths);
	}

	public void initLobby() {
		JTextField chatTextField;
		JTextField txtPlayerPreview;

		panelLobby = new BackPanel();

		panelLobby.setLayout(null);

		charP1 = new JLabel("");
		charP1.setBounds(lobbyScale(25), lobbyScale(170), lobbyScale(100), lobbyScale(100));
		panelLobby.add(charP1);

		charP2 = new JLabel("");
		charP2.setBounds(lobbyScale(195), lobbyScale(170), lobbyScale(100), lobbyScale(100));
		panelLobby.add(charP2);

		charP3 = new JLabel("");
		charP3.setBounds(lobbyScale(365), lobbyScale(170), lobbyScale(100), lobbyScale(100));
		panelLobby.add(charP3);

		charP4 = new JLabel("");
		charP4.setBounds(lobbyScale(535), lobbyScale(170), lobbyScale(100), lobbyScale(100));
		panelLobby.add(charP4);

		player4 = new JTextField();
		player4.setEditable(false);
		player4.setText("P4:" + p4);
		player4.setColumns(10);
		player4.setBounds(lobbyScale(530), lobbyScale(136), lobbyScale(160), lobbyScale(30));
		panelLobby.add(player4);

		player3 = new JTextField();
		player3.setEditable(false);
		player3.setText("P3: " + p3);
		player3.setColumns(10);
		player3.setBounds(lobbyScale(360), lobbyScale(136), lobbyScale(160), lobbyScale(30));
		panelLobby.add(player3);

		player2 = new JTextField();
		player2.setEditable(false);
		player2.setText("P2: " + p2);
		player2.setColumns(10);
		player2.setBounds(lobbyScale(190), lobbyScale(136), lobbyScale(160), lobbyScale(30));
		panelLobby.add(player2);

		player1 = new JTextField();
		player1.setEditable(false);
		player1.setBounds(lobbyScale(20), lobbyScale(136), lobbyScale(160), lobbyScale(30));
		player1.setText("P1: " + p1);
		panelLobby.add(player1);
		player1.setColumns(10);

		JLabel lblNewLabel = new JLabel("Rumble Ruckus Lobby");
		lblNewLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, lobbyScale(16)));
		lblNewLabel.setForeground(Color.white);
		lblNewLabel.setBounds(lobbyScale(350), lobbyScale(11), lobbyScale(422), lobbyScale(75));
		panelLobby.add(lblNewLabel);

		btnMap = new JButton("Map: " + map.toUpperCase());
		btnMap.setFont(new Font("Arial", Font.BOLD, lobbyScale(16)));
		btnMap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!me.equals("spec")) {

					if (btnMap.getText().equals("Map: WINTER")) {
						map = "graveyard";
					} else if (btnMap.getText().equals("Map: GRAVEYARD")) {
						map = "desert";
					} else if (btnMap.getText().equals("Map: DESERT")) {
						map = "forest";
					} else {
						btnMap.setText("Map: WINTER");
						map = "winter";
					}

					try {
						String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
								+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2
								+ " " + char3 + " " + char4;
						outLobby.println(setup);
						outLobby.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnMap.setBounds(lobbyScale(20), lobbyScale(11), lobbyScale(150), lobbyScale(30));
		panelLobby.add(btnMap);

		btnStock = new JButton("Stock: 3");
		btnStock.setFont(new Font("Arial", Font.BOLD, lobbyScale(16)));
		btnStock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!me.equals("spec")) {

					if (stock == 3) {
						stock = 4;
					} else if (stock == 4) {
						stock = 5;
					} else if (stock == 5) {
						stock = 1;
					} else if (stock == 1) {
						stock = 2;
					} else {
						stock = 3;
					}

					try {
						String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
								+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2
								+ " " + char3 + " " + char4;

						outLobby.println(setup);
						outLobby.flush();
						System.out.println("Sending Setup String");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		btnStock.setBounds(lobbyScale(190), lobbyScale(11), lobbyScale(150), lobbyScale(30));
		panelLobby.add(btnStock);

		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPane, "panelHome");
			}
		});
		btnBack.setBounds(lobbyScale(20), lobbyScale(93), lobbyScale(150), lobbyScale(30));
		btnBack.setFont(new Font("Arial", Font.BOLD, lobbyScale(16)));
		panelLobby.add(btnBack);

		JButton btnLobbyPlay = new JButton("Play");
		btnLobbyPlay.setFont(new Font("Arial", Font.BOLD, lobbyScale(16)));
		btnLobbyPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!me.equals("spec")) {
					if (playable) {
						try {
							testString = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
									+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " "
									+ char2 + " " + char3 + " " + char4;
							outLobby.println(testString);
							outLobby.flush();
							outLobby.println("play");
							outLobby.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		btnLobbyPlay.setBounds(lobbyScale(350), lobbyScale(93), lobbyScale(420), lobbyScale(30));
		panelLobby.add(btnLobbyPlay);

		chatTextPane = new JTextPane();
		chatTextPane.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(chatTextPane);
		chatScrollPane.setBounds(lobbyScale(20), lobbyScale(280), lobbyScale(250), lobbyScale(95));
		panelLobby.add(chatScrollPane);

		chatTextField = new JTextField();
		chatTextField.setText("Type to Chat");
		chatTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				chatTextField.setText("");
			}
		});
		chatTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_ENTER) {
					chatTextPane.setEditable(true);
					String message = chatTextField.getText();

					if (!me.equals("spec")) {
						try {
							outLobby.println("message " + user + ": " + message);
							outLobby.flush();
							System.out.println("Sending Message");
						} catch (Exception e) {
							e.printStackTrace();
						}

						chatTextField.setText("");
						chatTextPane.setEditable(false);
					} else {
						try {
							outLobby.println("specmessage " + user + ": " + message);
							outLobby.flush();
							System.out.println("Sending Message");
						} catch (Exception e) {
							e.printStackTrace();
						}

						chatTextField.setText("");
						chatTextPane.setEditable(false);
					}
				}
			}
		});
		chatTextField.setBounds(lobbyScale(20), lobbyScale(381), lobbyScale(250), lobbyScale(20));
		panelLobby.add(chatTextField);
		chatTextField.setColumns(10);

		txtPlayerPreview = new JTextField();
		txtPlayerPreview.setBounds(lobbyScale(20), lobbyScale(165), lobbyScale(750), lobbyScale(110));
		panelLobby.add(txtPlayerPreview);
		txtPlayerPreview.setColumns(10);

		JLabel charNinja = new JLabel("");
		charNinja.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!me.equals("spec")) {

					if (playerNum == 0) {
						char1 = "ninja";
					} else if (playerNum == 1) {
						char2 = "ninja";
					} else if (playerNum == 2) {
						char3 = "ninja";
					} else {
						char4 = "ninja";
					}

					try {
						String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
								+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2
								+ " " + char3 + " " + char4;

						outLobby.println(setup);
						outLobby.flush();
						System.out.println("Sending Setup String");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		charNinja.setBounds(lobbyScale(350), lobbyScale(304), lobbyScale(60), lobbyScale(60));
		Image ninjaImg = new ImageIcon(this.getClass().getResource("/ninjaS.png")).getImage();
		ninjaImg = ninjaImg.getScaledInstance(lobbyScale(60), lobbyScale(60), Image.SCALE_SMOOTH);
		charNinja.setIcon((new ImageIcon(ninjaImg)));
		panelLobby.add(charNinja);

		JLabel charNinjaGirl = new JLabel("");
		charNinjaGirl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!me.equals("spec")) {

					if (playerNum == 0) {
						char1 = "ninjaGirl";
					} else if (playerNum == 1) {
						char2 = "ninjaGirl";
					} else if (playerNum == 2) {
						char3 = "ninjaGirl";
					} else {
						char4 = "ninjaGirl";
					}

					try {
						String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
								+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2
								+ " " + char3 + " " + char4;

						outLobby.println(setup);
						outLobby.flush();
						System.out.println("Sending Setup String");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		charNinjaGirl.setBounds(lobbyScale(420), lobbyScale(304), lobbyScale(60), lobbyScale(60));
		Image ninjaGirlImg = new ImageIcon(this.getClass().getResource("/ninjaGirlS.png")).getImage();
		ninjaGirlImg = ninjaGirlImg.getScaledInstance(lobbyScale(60), lobbyScale(60), Image.SCALE_SMOOTH);
		charNinjaGirl.setIcon((new ImageIcon(ninjaGirlImg)));
		panelLobby.add(charNinjaGirl);

		JLabel charRobot = new JLabel("");
		if (!me.equals("spec")) {
			charRobot.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (playerNum == 0) {
						char1 = "robot";
					} else if (playerNum == 1) {
						char2 = "robot";
					} else if (playerNum == 2) {
						char3 = "robot";
					} else {
						char4 = "robot";
					}

					try {
						String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
								+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2
								+ " " + char3 + " " + char4;

						outLobby.println(setup);
						outLobby.flush();
						System.out.println("Sending Setup String");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		charRobot.setBounds(lobbyScale(490), lobbyScale(304), lobbyScale(60), lobbyScale(60));
		Image robotImg = new ImageIcon(this.getClass().getResource("/robotS.png")).getImage();
		robotImg = robotImg.getScaledInstance(lobbyScale(60), lobbyScale(60), Image.SCALE_SMOOTH);
		charRobot.setIcon((new ImageIcon(robotImg)));
		panelLobby.add(charRobot);

		JLabel charKnight = new JLabel("");
		charKnight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!me.equals("spec")) {
					if (playerNum == 0) {
						char1 = "knight";
					} else if (playerNum == 1) {
						char2 = "knight";
					} else if (playerNum == 2) {
						char3 = "knight";
					} else {
						char4 = "knight";
					}
					try {
						String setup = "setup" + " " + gameNum + " " + map + " " + stock + " " + seconds + " " + p1
								+ " " + p2 + " " + p3 + " " + p4 + " " + spec1 + " " + spec2 + " " + char1 + " " + char2
								+ " " + char3 + " " + char4;
						outLobby.println(setup);
						outLobby.flush();
						System.out.println("Sending Setup String");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		charKnight.setBounds(lobbyScale(560), lobbyScale(304), lobbyScale(60), lobbyScale(60));
		Image knightImg = new ImageIcon(this.getClass().getResource("/knightS.png")).getImage();
		knightImg = knightImg.getScaledInstance(lobbyScale(60), lobbyScale(60), Image.SCALE_SMOOTH);
		charKnight.setIcon((new ImageIcon(knightImg)));
		panelLobby.add(charKnight);

		JLabel charSoon1 = new JLabel("Soon");
		charSoon1.setBounds(lobbyScale(630), lobbyScale(304), lobbyScale(60), lobbyScale(60));
		charSoon1.setForeground(Color.white);
		panelLobby.add(charSoon1);

		JLabel charSoon2 = new JLabel("Soon");
		charSoon2.setBounds(lobbyScale(700), lobbyScale(304), lobbyScale(60), lobbyScale(60));
		charSoon2.setForeground(Color.white);
		panelLobby.add(charSoon2);

		txtCharacterSelect = new JTextField();
		txtCharacterSelect.setHorizontalAlignment(SwingConstants.CENTER);
		txtCharacterSelect.setText("Character Select:");
		txtCharacterSelect.setEditable(false);
		txtCharacterSelect.setBounds(lobbyScale(350), lobbyScale(280), lobbyScale(420), lobbyScale(20));
		panelLobby.add(txtCharacterSelect);
		txtCharacterSelect.setColumns(10);
	}

	public void appendToPane(JTextPane tp, String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		tp.setCharacterAttributes(aset, false);
		tp.replaceSelection(msg);
		tp.repaint();
	}

	public class LobbyServerListener implements Runnable {
		Scanner in;

		public LobbyServerListener(Socket s) {
			try {
				in = new Scanner(new BufferedInputStream(s.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (true) {
				if (in.hasNextLine()) {
					String cmd = in.next();
					String str = in.nextLine();
					handleMoves(cmd, str);
				}
			}
		}
	}

	public void handleMoves(String cmd, String s) {
		if (cmd.equals("play")) {
			try {
				new Game(map, stock, seconds, p1, p2, p3, p4, char1, char2, char3, char4, user);
				playable = false;
			} catch (XInputNotLoadedException e) {
				e.printStackTrace();
			}

			try {
				String setup = "force reset";
				outLobby.println(setup);
				outLobby.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (cmd.equals("info")) {
			System.out.println("Received Info");

			Scanner scan = new Scanner(s);
			gameNum = Integer.parseInt(scan.next());
			p1 = scan.next();
			p2 = scan.next();
			p3 = scan.next();
			p4 = scan.next();
			spec1 = scan.next();
			spec2 = scan.next();

			txtPlayer1.setText("P1: " + p1);
			txtPlayer2.setText("P2: " + p2);
			txtPlayer3.setText("P3: " + p3);
			txtPlayer4.setText("P4: " + p4);

			txtPlayer1.repaint();
			txtPlayer2.repaint();
			txtPlayer3.repaint();
			txtPlayer4.repaint();

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

			txtSpec1.setText(spec1);
			txtSpec2.setText(spec2);

			txtSpec1.repaint();
			txtSpec2.repaint();

		} else if (cmd.equals("message")) {

			System.out.println(s.trim());
			chatTextPane.setEditable(true);
			appendToPane(chatTextPane, s + "\n", Color.BLACK);
			chatTextPane.setEditable(false);

		} else if (cmd.equals("specmessage")) {

			System.out.println(s.trim());
			chatTextPane.setEditable(true);
			appendToPane(chatTextPane, s + "\n", Color.BLUE);
			chatTextPane.setEditable(false);

		} else if (cmd.equals("force")) {
			gameNum = 0;
			returning = 0;
			joined = false;

			stock = 3;
			seconds = 1800;
			playerNum = 0;
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
			gameRunning = false;
			players = 0;
			spectators = 0;
			me = "";
			spec1 = "na";
			spec2 = "na";

			cardLayout.show(contentPane, "panelHome");
		} else if (cmd.equals("setup")) {
			int i;
			System.out.println("Received Setup");
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

			btnStock.setText("Stock: " + stock);
			btnStock.repaint();

			btnMap.setText("Map: " + map.toUpperCase());
			btnMap.repaint();

			player1.setText("P1: " + p1);
			player2.setText("P2: " + p2);
			player3.setText("P3: " + p3);
			player4.setText("P4: " + p4);

			switch (playerNum) {
			case 0:
				player1.setBackground(new Color(0, 153, 204));
				player2.setBackground(new Color(255, 255, 255));
				player3.setBackground(new Color(255, 255, 255));
				player4.setBackground(new Color(255, 255, 255));
				break;
			case 1:
				player1.setBackground(new Color(255, 255, 255));
				player2.setBackground(new Color(0, 153, 204));
				player3.setBackground(new Color(255, 255, 255));
				player4.setBackground(new Color(255, 255, 255));
				break;
			case 2:
				player1.setBackground(new Color(255, 255, 255));
				player2.setBackground(new Color(255, 255, 255));
				player3.setBackground(new Color(0, 153, 204));
				player4.setBackground(new Color(255, 255, 255));
				break;
			default:
				player1.setBackground(new Color(255, 255, 255));
				player2.setBackground(new Color(255, 255, 255));
				player3.setBackground(new Color(255, 255, 255));
				player4.setBackground(new Color(0, 153, 204));
				break;
			}

			player1.repaint();
			player2.repaint();
			player3.repaint();
			player4.repaint();

			System.out.println("painted + " + p1);

			Image ninja = new ImageIcon(this.getClass().getResource("/ninjaM.png")).getImage();
			ninja = ninja.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_SMOOTH);
			Image ninjaGirl = new ImageIcon(this.getClass().getResource("/ninjaGirlM.png")).getImage();
			ninjaGirl = ninjaGirl.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_SMOOTH);
			Image robot = new ImageIcon(this.getClass().getResource("/robotM.png")).getImage();
			robot = robot.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_SMOOTH);
			Image knight = new ImageIcon(this.getClass().getResource("/knightM.png")).getImage();
			knight = knight.getScaledInstance(lobbyScale(100), lobbyScale(100), Image.SCALE_SMOOTH);

			if (!char1.equals("na")) {
				if (char1.equals("ninja")) {
					charP1.setIcon((new ImageIcon(ninja)));
				} else if (char1.equals("ninjaGirl")) {
					charP1.setIcon((new ImageIcon(ninjaGirl)));
				} else if (char1.equals("robot")) {
					charP1.setIcon(new ImageIcon(robot));
				} else if (char1.equals("knight")) {
					charP1.setIcon(new ImageIcon(knight));
				}
				charP1.repaint();
			}
			if (!char2.equals("na")) {
				if (char2.equals("ninja")) {
					charP2.setIcon((new ImageIcon(ninja)));
				} else if (char2.equals("ninjaGirl")) {
					charP2.setIcon((new ImageIcon(ninjaGirl)));
				} else if (char2.equals("robot")) {
					charP2.setIcon(new ImageIcon(robot));
				} else if (char2.equals("knight")) {
					charP2.setIcon(new ImageIcon(knight));
				}
				charP2.repaint();
			}
			if (!char3.equals("na")) {
				if (char3.equals("ninja")) {
					charP3.setIcon((new ImageIcon(ninja)));
				} else if (char3.equals("ninjaGirl")) {
					charP3.setIcon((new ImageIcon(ninjaGirl)));
				} else if (char3.equals("robot")) {
					charP3.setIcon(new ImageIcon(robot));
				} else if (char3.equals("knight")) {
					charP3.setIcon(new ImageIcon(knight));
				}
				charP3.repaint();
			}
			if (!char4.equals("na")) {
				if (char4.equals("ninja")) {
					charP4.setIcon((new ImageIcon(ninja)));
				} else if (char4.equals("ninjaGirl")) {
					charP4.setIcon((new ImageIcon(ninjaGirl)));
				} else if (char4.equals("robot")) {
					charP4.setIcon(new ImageIcon(robot));
				} else if (char4.equals("knight")) {
					charP4.setIcon(new ImageIcon(knight));
				}
				charP4.repaint();
			}
		}
	}

	public class FriendServerListener implements Runnable {
		Scanner in;
		String names = "";

		public FriendServerListener(Socket s) {
			try {
				in = new Scanner(new BufferedInputStream(s.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (true) {
				if (in.hasNextLine()) {
					String names = in.nextLine();
					updateFriendsList(names);
				}
			}
		}
	}

	public void requestFriendUpdate() {
		outFriend.println("update " + "nothing");
	}

	public void removeFromFriendsServer(String username) {
		outFriend.println("remove " + username);
	}

	public void addToFriendsServer(String username) {
		outFriend.println("add " + username);
	}

	public void updateFriendsList(String players) {
		friends.clear();
		Scanner parser = new Scanner(players);
		while (parser.hasNext()) {
			friends.add(parser.next());
		}
		parser.close();
	}

	private int lobbyScale(int x) {
		return (x * height) / 1080;
	}

	public class BackPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);
			g.drawImage(img, 0, 0, lobbyScale(804), lobbyScale(461), 0, 0, 804, 461, null);
		}

	}

}
