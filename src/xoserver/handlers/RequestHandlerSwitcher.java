/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.handlers;

/**
 *
 * @author mohamed
 */
public class RequestHandlerSwitcher {

    private static RequestHandlerSwitcher instance;
    private final GameRequestHandler gameRequestHandler;

    private RequestHandlerSwitcher() {
        gameRequestHandler = new GameRequestHandler();
    }

    public static RequestHandlerSwitcher getInstance() {
        if (instance == null) {
            instance = new RequestHandlerSwitcher();
        }
        return instance;
    }

    void registerAuthHandler(ResponseReceiver responseReceiver) {

    }

    ResponseReceiver registerGameHandler() {

    }

    void registerDetailsHandler(ResponseReceiver responseReceiver) {

    }
}
