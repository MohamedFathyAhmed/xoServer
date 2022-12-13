package DB;

import handler.Player;
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

    public static String CreatePlayer(Player player) throws SQLException {
        String res = "";
        try {
            PreparedStatement con = connection.prepareStatement("INSERT INTO Players (name , password , score) VALUES (?  , ? , ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            con.setString(1, player.getName());
            con.setString(2, player.getPassword());
            con.setInt(3, player.getScore());
            con.executeUpdate();
            res = "true";

        } catch (SQLException e) {
            res = "false;;please try again";
        } finally {
            return res;
        }
    }

        public boolean changeStatus(String name,Boolean status,Boolean IsPlaying) {
        try {
            PreparedStatement con = connection.prepareStatement("UPDATE players set status=?, IsPlaying=? WHERE NAME=?");
            con.setBoolean(1,status );
            con.setBoolean(2,IsPlaying );
            con.setString(3,name );
            int isupdated = con.executeUpdate();
            if (isupdated > 0) {
                return true;
            }
        } catch (SQLException e) {
        }
        return true;
 }
        
    public void changeScore(String name, int newScore) {
        try {
            PreparedStatement con = connection.prepareStatement("UPDATE players set score=score+? WHERE id=?");
            con.setInt(1, newScore);
            con.setString(3,name );

            int updataNumber = con.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static String getPlayer(String name, String password) {
        String res = "";
        try {
            PreparedStatement con = connection.prepareStatement("SELECT * FROM players WHERE name=? AND password=?");
            con.setString(1, name);
            con.setString(2, password);
            ResultSet rs = con.executeQuery();
            boolean isExist = rs.next();

            if (isExist) {
                res = "true" + ";;" + rs.getString("name") + ";;" + rs.getInt("score");
                PreparedStatement update = connection.prepareStatement("UPDATE players set status=TRUE WHERE name=? ");
                update.setString(1, name);
                int updataNumber = update.executeUpdate();
                System.out.println(updataNumber);

                Player player = new Player(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("score"));

            } else {
                // if user don't exist
                res = "false___notExist";
            }
        } catch (SQLException ex) {
            res = "false___error";
            System.out.println(ex);
        } finally {
            return res;
        }

    }


}
