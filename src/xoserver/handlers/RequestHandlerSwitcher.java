package xoserver.handlers;

import java.io.IOException;
import xoserver.handlers.game_room.GameRoomHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package tictactoeserver.handlers;
/**
 *
 * @author marina
 */
public class RequestHandlerSwitcher {

    private static RequestHandlerSwitcher instance;
    private final AuthRequestHandler authRequestHandler;
    private final DataRequestHandler dataRequestHandler;

    private RequestHandlerSwitcher() {
        authRequestHandler = new AuthRequestHandler();
        dataRequestHandler = new DataRequestHandler();
    }

    public static RequestHandlerSwitcher getInstance() {
        if (instance == null) {
            instance = new RequestHandlerSwitcher();
        }
        return instance;
    }

    public void getInRoom(String sender, String receiver) throws IOException {
        new GameRoomHandler(sender, receiver);
    }

    public ResponseReceiver newAuthRequestHandler() {
        return authRequestHandler.newResponseReceiver();
    }

    public ResponseReceiver newDataRequestHandler() {
        return dataRequestHandler.newResponseReceiver();
    }
}
