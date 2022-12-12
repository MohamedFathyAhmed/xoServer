package DB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Players (name , password , score) VALUES (?  , ? , ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getPassword());
            stmt.setInt(3, player.getScore());
            stmt.executeUpdate();
            res = "true";

        } catch (SQLException e) {
            res = "false;;please try again";
        } finally {
            return res;
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

                ////////////// update status
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
