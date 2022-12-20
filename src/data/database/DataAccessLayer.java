package data.database;

import data.database.models.Game;
import xoserver.handlers.Player;
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
    private static ResultSet record;

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

    ////////////////////////////////////////////////////////////////marina
    public static Game[] getGames() throws SQLException {
        return getGamesFromResultSet(connection.createStatement()
                .executeQuery("SELECT * FROM GAME"));

    }

    private static Game getGame(ResultSet gamesResultSet) throws SQLException {
        Game game = new Game(gamesResultSet.getString("player_1"),
                gamesResultSet.getString("player_2"),
                gamesResultSet.getString("date"),
                gamesResultSet.getString("won_player"),
                gamesResultSet.getString("player1Shape"),
                gamesResultSet.getString("player1Shape"));
        return game;
    }

    private static int getGameHistoryCount() throws SQLException {
        ResultSet gameCount = connection.createStatement().executeQuery("select count(*) count from GAME");
        return gameCount.getInt("count");

    }

    public static Game[] getGamesFromResultSet(ResultSet gamesResultSet) throws SQLException {
        Game[] gamesArray = new Game[getGameHistoryCount()];
        int i = 0;
        while (gamesResultSet.next()) {
            gamesArray[i++] = getGame(gamesResultSet);
        }
        return gamesArray;
    }

    private static void setGame(Game game) throws SQLException {//id

        connection.createStatement().execute("INSET INTO GAME (PLAYER_1,PLAYER_2,DATE,WON_PLAYER)VALUES('"
                + game.getPlayer1()
                + "','"
                + game.getPlayer2()
                + "','"
                + game.getDate()
                + "','"
                + game.getWonPLayer()
                + "');");

    }
///////////////////////////////////////////////
}
