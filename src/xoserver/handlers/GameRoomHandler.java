/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author mohamed
 */
public class GameRoomHandler {

    private static final Vector<GameRoomHandler> gameRooms = new Vector<GameRoomHandler>();

    private final List<Play> plays;

    private final ResponseReceiver gameRequestReceiver;

    private final ResponseReceiver player1ResponseReceiver;
    private final ResponseReceiver player2ResponseReceiver;

    private final String player1Name;
    private final String player2Name;

    private char[] boardChars = {'-', '-', '-', '-', '-', '-', '-', '-', '-'};

    private boolean isRecording;
    private boolean player1Turn;

    private GameShapes player1Shape;
    private GameShapes player2Shape;

    private GameRoomHandler(String sender, String receiver) {
        this.player1Name = sender;
        this.player2Name = receiver;

        this.player1Shape = GameShapes.X;
        this.player1Shape = GameShapes.O;

        isRecording = false;
        plays = new ArrayList();

        gameRequestReceiver = (request) -> {
            return handle(request);
        };

        player1ResponseReceiver = ClientHanlder.getClientHanlderByName(player1Name).getInGame(gameRequestReceiver);
        player2ResponseReceiver = ClientHanlder.getClientHanlderByName(player2Name).getInGame(gameRequestReceiver);

        gameRooms.add(this);
    }

    private String handle(String request) {
        switch (request) {
            case "play":
                //performPlayOnBoard(/*position*/);
                //addPlay(/*position*/);
                //GameHandler.updateGameState(new String (boardChars));
                player1Turn = !player1Turn;
            //return response

            case "replay":
                replay();
            //return response

            case "record":
                isRecording = !isRecording;
            //return response

            case "leave":
                leave();
            //return response

        }
        return "";
    }

    private void resetBoardChars() {
        for (char c : boardChars) {
            c = '-';
        }
    }

    private void replay() {
        resetBoardChars();
        isRecording = false;
    }

    private void leave() {
        ClientHanlder.getClientHanlderByName(player1Name).getOutOfGame(player1ResponseReceiver);
        ClientHanlder.getClientHanlderByName(player2Name).getOutOfGame(player2ResponseReceiver);
        gameRooms.remove(this);
    }

    private void performPlayOnBoard(int position) {
        GameShapes shape = player2Shape;
        if (player1Turn) {
            shape = player1Shape;
        }

        boardChars = GameHandler.updateBoard(position, shape.name(), boardChars);
    }

    private void addPlay(int position) {
        String player = player2Name;
        if (player1Turn) {
            player = player2Name;
        }
        plays.add(new Play(position, player));
    }
}
