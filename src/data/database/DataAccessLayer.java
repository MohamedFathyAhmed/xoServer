package data.database;



import xoserver.handlers.Player;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

}
