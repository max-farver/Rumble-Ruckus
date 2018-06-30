package login;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;

import lobby.LeaderBoardObj;

/**
 * The Class DBTools.
 */
public class DBTools {
	/**
	 * The Enum TestTableColumns.
	 */
	enum TestTableColumns {
		/** The id. */
		id,
		/** The username. */
		username,
		/** The deaths. */
		deaths,
		/** The kills. */
		kills,
		/** The email. */
		email,
		/** The count. */
		COUNT,
		/** The session key. */
		sessionKey;
	}

	/** The jdbc driver str. */
	private final String jdbcDriverStr;
	/** The jdbc URL. */
	private final String jdbcURL;
	/** The return val. */
	private String returnVal;
	/** The count. */
	private int count;
	/** The connection. */
	private Connection connection;
	/** The statement. */
	private Statement statement;
	/** The result set. */
	private ResultSet resultSet;
	/** The prepared statement. */
	private PreparedStatement preparedStatement;

	/**
	 * Instantiates a new DB tools.
	 *
	 * @param jdbcDriverStr
	 * @param jdbcURL
	 */
	public DBTools(String jdbcDriverStr, String jdbcURL) {
		this.jdbcDriverStr = jdbcDriverStr;
		this.jdbcURL = jdbcURL;
	}

	/**
	 * Read data.
	 *
	 * @param nameOfUser
	 * @param type
	 * @param value
	 * @return a string
	 * @throws Exception
	 */
	public String readData(String nameOfUser, String type, String value) throws Exception {
		try {
			connection = DriverManager.getConnection(jdbcURL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT " + value + " FROM db309tg3.users where " + type + " = \"" + nameOfUser + "\";");
			getResultSet(resultSet, value);
		} finally {
			close();
		}
		return returnVal;
	}

	/**
	 * Leaderboard reader.
	 *
	 * @param idNumber
	 * @param value
	 * @return a string
	 * @throws Exception
	 */
	public String leaderboardReader(String idNumber, String value) throws Exception {
		try {
			connection = DriverManager.getConnection(jdbcURL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT " + value + " FROM db309tg3.users = \"" + idNumber + "\";");
			getResultSet(resultSet, value);
		} finally {
			close();
		}
		return returnVal;
	}

	/**
	 * Write data to database.
	 *
	 * @param nameOfUser
	 * @param value
	 * @param replaceWith
	 * @return a string
	 * @throws Exception
	 */
	public String writeData(String nameOfUser, String value, String replaceWith) throws Exception {
		try {
			connection = DriverManager.getConnection(jdbcURL);
			preparedStatement = connection.prepareStatement("UPDATE db309tg3.cur_users SET " + value + " = "
					+ replaceWith + " where username = \"" + nameOfUser + "\";");
			preparedStatement.executeUpdate();
		} finally {
			close();
		}
		return returnVal;
	}

	/**
	 * Counts total number of entries in the users tables.
	 *
	 * @return count * @throws Exception
	 */
	public int userCount() throws Exception {
		try {
			connection = DriverManager.getConnection(jdbcURL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT COUNT(id) FROM db309tg3.users;");
			getCountResultSet(resultSet);
		} finally {
			close();
		}
		return count;
	}

	/**
	 * Gets the result set.
	 *
	 * @param the
	 *            result set
	 * @param value
	 * @return the usable result set
	 * @throws Exception
	 */
	private void getResultSet(ResultSet resultSet, String value) throws Exception {
		while (resultSet.next()) {
			if (value == "username") {
				returnVal = resultSet.getString(TestTableColumns.username.toString());
			} else if (value == "id") {
				returnVal = resultSet.getString(TestTableColumns.id.toString());
			} else if (value == "kills") {
				returnVal = resultSet.getString(TestTableColumns.kills.toString());
			} else if (value == "deaths") {
				returnVal = resultSet.getString(TestTableColumns.deaths.toString());
			} else if (value == "email") {
				returnVal = resultSet.getString(TestTableColumns.email.toString());
			} else if (value == "sessionKey") {
				returnVal = resultSet.getString(TestTableColumns.username.toString());
			}
		}
	}

	/**
	 * Gets the total number of entries in the result set.
	 *
	 * @param resultSet
	 *            the result set
	 * @return the count result set
	 * @throws Exception
	 */
	private void getCountResultSet(ResultSet resultSet) throws Exception {
		while (resultSet.next()) {
			count = resultSet.getInt(1);
		}
	}

	/**
	 * Finds a non-zero sessionKey and returns the corresponding username.
	 *
	 * @return the name from key
	 * @throws Exception
	 */
	public String getNameFromKey(int sessionKey) throws Exception
	{
		try
		{
			connection = DriverManager.getConnection(jdbcURL);
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT username FROM db309tg3.cur_users where sessionKey = '" + sessionKey + "';"); // "
																														// //"
																														// //!=
																														// \"0\";");
			getResultSet(resultSet, "sessionKey");
		}
		finally
		{
			close();
		}
		return returnVal;
	}

	/**
	 * Write image to database.
	 *
	 * @param userId
	 * @param filename
	 * @throws Exception
	 */
	public void writeBlob(String userId, String filename) throws Exception {
		String updateSQL = "UPDATE db309tg3.profile_images " + "SET image = ? " + "WHERE username=?";
		try (Connection conn = DriverManager.getConnection(jdbcURL);
				PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
			// read the file
			File file = new File("img/" + filename);
			FileInputStream input = new FileInputStream(file);
			// set parameters
			pstmt.setBinaryStream(1, input);
			pstmt.setString(2, userId);
			// store the blob in database
			pstmt.executeUpdate();
			conn.close();
		}
	}

	/**
	 * 
	 * 
	 * @param userId
	 * @param filename
	 * @throws Exception
	 */
	public void writeFirstBlob(String userId, String filename) throws Exception {
		String updateSQL = "INSERT INTO db309tg3.profile_images (username, image)" + "VALUES (?, ?)";
		try (Connection conn = DriverManager.getConnection(jdbcURL);
				PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
			// read the file
			File file = new File("img/" + filename);
			FileInputStream input = new FileInputStream(file);
			// set parameters
			pstmt.setString(1, userId);
			pstmt.setBinaryStream(2, input);
			// store the blob in database
			pstmt.executeUpdate();
			conn.close();
		}
	}

	/**
	 * Read images from database.
	 *
	 * @param userId
	 * @return the image
	 * @throws Exception
	 */
	public Image readBlob(String userId) throws Exception {
		if (this.checkIfMade(userId) == 0) {
			this.writeFirstBlob(userId, "spongebob.png");
		}
		String selectSQL = "SELECT image FROM db309tg3.profile_images WHERE username=?";
		ResultSet rs = null;
		Image img = null;
		try (Connection conn = DriverManager.getConnection(jdbcURL);
				PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
			// set parameter;
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			// write binary stream into file
			File file = new File("userPic.png");
			FileOutputStream output = new FileOutputStream(file);
			while (rs.next()) {
				InputStream input = rs.getBinaryStream("image");
				byte[] buffer = new byte[1024];
				while (input.read(buffer) > 0) {
					output.write(buffer);
				}
			}
			ImageIcon helper = new ImageIcon("userPic.png");
			img = helper.getImage();
			conn.close();
		}
		if (img == null) {
			img = new ImageIcon(this.getClass().getResource("/ninja.png")).getImage();
		}
		return img;
	}

	/**
	 * Read sorted list of entries.
	 *
	 * @param sortBy
	 * @return the leaderboardobj[]
	 * @throws Exception
	 */
	public LeaderBoardObj[] readEntrySorted(String sortBy) throws Exception {
		String user, kills, deaths, wins;
		user = kills = deaths = wins = "";
		LeaderBoardObj leaderObj[] = new LeaderBoardObj[5];
		try {
			connection = DriverManager.getConnection(jdbcURL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT username, kills, deaths, wins " + "FROM db309tg3.users ORDER BY " + sortBy + " DESC;");
			for (int i = 0; i < 5; i++) {
				if (resultSet.next()) {
					user = resultSet.getString("username");
					kills = resultSet.getString("kills");
					deaths = resultSet.getString("deaths");
					wins = resultSet.getString("wins");
					LeaderBoardObj temp = new LeaderBoardObj(user, kills, deaths, wins);
					leaderObj[i] = temp;
				}
			}
		} finally {
			close();
		}
		return leaderObj;
	}

	/**
	 * Read sorted list of entries.
	 *
	 * @param nameOfUser
	 * @return the leaderboardobj[]
	 * @throws Exception
	 */
	public int checkIfMade(String nameOfUser) throws Exception {
		try {
			connection = DriverManager.getConnection(jdbcURL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT COUNT(*) FROM db309tg3.profile_images WHERE username = \"" + nameOfUser + "\";");
			getCountResultSet(resultSet);
		} finally {
			close();
		}
		return count;
	}

	public void increment(String nameOfUser, String value, int amount) throws SQLException {
		try {
			connection = DriverManager.getConnection(jdbcURL);
			preparedStatement = connection.prepareStatement("UPDATE db309tg3.users SET " + value + " = " + value + " + "
					+ amount + " WHERE username = \"" + nameOfUser + "\";");
			preparedStatement.executeUpdate();
		} finally {
			close();
		}
	}

	/**
	 * Close the connection.
	 */
	private void close() {
		try {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception e) {
		}
	}
}
