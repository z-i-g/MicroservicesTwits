package microservicesTwit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class microservicesTwit {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, TwitterException, IOException {
		try {
			runTest();
		} catch (SQLException ex) {
			for (Throwable t : ex)
				t.printStackTrace();

		}

	}

	public static void runTest() throws SQLException, IOException, TwitterException {

		Twitter tw = new TwitterFactory().getInstance();
		tw.setOAuthConsumer("bC5819QlyV4juKqx0dhvenRcA", "HEr2lDHGi18tbcHhIq4VX0S8teJvXocemtZL6U9011LoIpdTrf");
		AccessToken accessToken = new AccessToken("995926589232869376-BzgMvNMg90uYgqybOqpNKwQYejkQRXu",
				"LjP0TJ6FgpdDoYoGJkHSy9f0OLqLQkTufpRWZRdrtfOF0");
		tw.setOAuthAccessToken(accessToken);
		Query query = new Query("microservices");

		QueryResult qr = tw.search(query);

		List<Status> tweets = qr.getTweets();

		String sql = "CREATE TABLE Messages (Message CHAR(225))";
		String sql1 = "INSERT INTO Messages VALUES (?)";
		String sql2 = "SELECT * FROM Messages";

		try (Connection conn = getConnection(); PreparedStatement stat = conn.prepareStatement(sql)) {

			
			System.out.println("Table is created");
			PreparedStatement stat1 = conn.prepareStatement(sql1);
			Statement st = conn.createStatement();
			
			int i = 0;

			for (Status tweet : tweets) {

				String twitText = tweet.getText();
				System.out.println(twitText);

				stat1.setString(1, twitText);
				System.out.println("the twits is added");

			}
			String out = "";
			ResultSet res = stat.executeQuery();
			while (res.next()) {
				out = res.getString("Message");
				System.out.println(out);
			}
			
			System.out.println("not data");
//			st.executeUpdate("DROP TABLE Messages");

			// System.out.println("text tweet: " + tweet.getText() + "href tweet: " +
			// tweet.getURLEntities() + "date produced: " + tweet.getCreatedAt() + "user ID:
			// " + tweet.getUser() + "retweet point: " + tweet.getRetweetCount() +
			// tweet.getFavoriteCount());
		}

	}

	public static Connection getConnection() throws IOException, SQLException {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
			props.load(in);
		}
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");

		System.out.println("JDBC URL is readed");
		System.out.println(props.getProperty("jdbc.url"));
		String username = props.getProperty("jdbc.username");
		System.out.println("Username is readed");
		String password = props.getProperty("jdbc.password");
		System.out.println("Password is readed");

		return DriverManager.getConnection(url, username, password);

	}

}
