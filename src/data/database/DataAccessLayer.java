package data.database;



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

}
