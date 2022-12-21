package data.database;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import xoserver.handlers.game_room.Play;

/**
 *
 * @author fathy
 */
public class DataAccessLayer {

    private static Connection connection;

    public static void connect() throws SQLException {
        connection = TicTacToeDatabase.getInstance().getConnection();
    }

    public static boolean insertPlayer(String username, String password) {
        try {
            return !connection
                    .createStatement()
                    .execute("INSERT INTO PLAYER (NAME, PASSWORD) VALUES ('" + username + "','" + password + "')");
        } catch (SQLException ex) {
            return false;
        }
    }

    public static boolean login(String username, String password) {
        try {
            return connection
                    .createStatement()
                    .executeQuery("SELECT 1 FROM PLAYER WHERE NAME='" + username + "' AND PASSWORD='" + password + "'")
                    .next();
        } catch (SQLException ex) {
            return false;
        }
    }

    //mohamed ibrahim
    public static int insertGame(String player1, String player2, String date, String wonPlayer) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO GAME "
                        + "(PLAYER_1,PLAYER_2,DATE,WON_PLAYER)"
                        + " VALUES(?,?,?,?)");
        preparedStatement.setString(1, player1);
        preparedStatement.setString(2, player2);
        preparedStatement.setString(3, date);
        preparedStatement.setString(4, wonPlayer);
        preparedStatement.execute();
        //
        ResultSet idResultSet = connection.createStatement().executeQuery("SELECT ID FROM GAME ORDER BY ID DESC FETCH FIRST 1 ROWS ONLY");
        idResultSet.next();
        return idResultSet.getInt(1);
    }

    public static void insertPlays(List<Play> plays, int gameId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PLAY "
                + "(POSITION,PLAYER,GAME_ID) "
                + "VALUES(?,?,?)");
        for (Play play : plays) {
            preparedStatement.setInt(1, play.getPosition());
            preparedStatement.setString(2, play.getPlayer());
            preparedStatement.setInt(3, gameId);
            preparedStatement.execute();
        }
    }

    public static int getPlayersCount() throws SQLException {
        ResultSet playersCountResultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM PLAYER");
        playersCountResultSet.next();
        return playersCountResultSet.getInt(1);
    }

    public static String getGames(String name) throws SQLException {
        ResultSet gamesResultSet = connection.createStatement()
                .executeQuery("SELECT * FROM GAME"
                        + " WHERE PLAYER_1='" + name + "' OR PLAYER_2='" + name + "'");
        String gamesString = "";
        while (gamesResultSet.next()) {
            gamesString += gamesResultSet.getInt("ID")
                    + "~"
                    + gamesResultSet.getString("PLAYER_1")
                    + "~"
                    + gamesResultSet.getString("PLAYER_2")
                    + "~"
                    + gamesResultSet.getString("WON_PLAYER")
                    + "~"
                    + gamesResultSet.getString("DATE")
                    + "~"
                    + gamesResultSet.getString("RECORDED")
                    + "~~";
        }
        return gamesString;
    }

    public static String getGamePlays(int gameId) throws SQLException {
        ResultSet playesResultSet = connection.createStatement()
                .executeQuery("SELECT * FROM PLAY"
                        + " WHERE GAME_ID='" + gameId + "'");
        String gamesString = "";
        while (playesResultSet.next()) {
            gamesString += playesResultSet.getInt("ID")
                    + "~"
                    + playesResultSet.getString("PLAYER")
                    + "~"
                    + playesResultSet.getString("POSITION")
                    + "~~";
        }
        return gamesString;
    }

    public static int playersCount() throws SQLException {
        ResultSet playersCountResultSet = connection.createStatement().executeQuery("SELECT * FROM PLAYER");
        playersCountResultSet.next();
        return playersCountResultSet.getInt(1);
    }

    public static void disconnect() throws SQLException {
        connection.close();
    }

}
