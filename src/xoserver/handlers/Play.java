/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

/**
 *
 * @author mohamed
 */
public class Play {
    private final int position;
    private final String player;

    public Play(int position, String player) {
        this.position = position;
        this.player = player;
    }

    public int getPosition() {
        return position;
    }

    public String getPlayer() {
        return player;
    }
}
