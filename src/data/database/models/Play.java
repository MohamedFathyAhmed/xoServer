/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.database.models;

/**
 *
 * @author mohamed
 */
public class Play {

    private final String position;
    private final String player;
    private final String time;
    private final String gameId;

    public Play(String position, String player, String time, String gameId) {
        this.position = position;
        this.player = player;
        this.time = time;
        this.gameId = gameId;
    }

    public String getPosition() {
        return position;
    }

    public String getPlayer() {
        return player;
    }

    public String getTime() {
        return time;
    }

}
