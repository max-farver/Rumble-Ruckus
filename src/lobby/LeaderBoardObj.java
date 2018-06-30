package lobby;

public class LeaderBoardObj {
	private String username;
	private String kills;
	private String deaths;
	private String wins;

	public LeaderBoardObj(String username, String kills, String deaths, String wins) {
		this.username = username;
		this.kills = kills;
		this.deaths = deaths;
		this.wins = wins;
	}

	public String getUserName() {
		return username;
	}

	public String getKills() {
		return kills;
	}

	public int getKillsNum() {
		return Integer.parseInt(kills);
	}

	public String getDeaths() {
		return deaths;
	}

	public int getDeathsNum() {
		return Integer.parseInt(deaths);
	}

	public String getWins() {
		return wins;
	}
}
