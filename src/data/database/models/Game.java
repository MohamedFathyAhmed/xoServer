/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.database.models;

import java.util.Date;

/**
 *
 * @author mohamed
 */
//true  message 
public class Game {

    private final String player1;
    private final String player2;
    private final String date;
    private final String wonPLayer;
    private final String player1Shape;
    private final String player2Shape;

    public Game(
            String player1,
            String player2,
            String date,
            String wonPLayer,
            String player1Shape,
            String player2Shape) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
        this.wonPLayer = wonPLayer;
        this.player1Shape = player1Shape;
        this.player2Shape = player2Shape;
    }

    public String getPlayer1Shape() {
        return player1Shape;
    }

    public String getPlayer2Shape() {
        return player2Shape;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getDate() {
        return date;
    }

    public String getWonPLayer() {
        return wonPLayer;
    }
}


                // "INSERT INTO single_mode_game(game_id, user_name, player_type, difficulty, player_case, game_record) VALUES(?, ?, ?, ?, ?, ?)"); 
    /* public static void addSingleModeGameRecord(String gameID, String userName, String playerType,
            DIFFICULTY difficulty, String playerCase, String gameRecord) {
        try {
            PreparedStatement pst = sqlServerConnection.prepareStatement(
                    "INSERT INTO single_mode_game(game_id, user_name, player_type, difficulty, player_case, game_record) VALUES(?, ?, ?, ?, ?, ?)");
            pst.setString(1, gameID);
            pst.setString(2, userName);
            pst.setString(3, playerType);
            pst.setString(4, MappingFunctions.mapDifficulty(difficulty));
            pst.setString(5, playerCase);
            pst.setString(6, gameRecord);
            pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/