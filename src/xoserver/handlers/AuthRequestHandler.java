/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import data.database.DataAccessLayer;
import java.io.IOException;
import java.sql.SQLException;
import xoserver.handlers.client.ClientHandler;

/**
 *
 * @author mohamed
 */
public class AuthRequestHandler extends RequestHandler {

    public ResponseReceiver newResponseReceiver() {
                return this::handle;
    }

    public synchronized String handle(String name, String request) throws NullPointerException {
        String[] splitedRequest = request.split(RequestType.MESSAGE_SPLITER);
        String username = splitedRequest[1];
        String password = splitedRequest[2];
        boolean success = false;
        String message;
        switch (splitedRequest[0]) {

            case RequestType.SIGNIN:
                success = !ClientHandler.isClientLoggedIn(username)&&DataAccessLayer.login(username, password);
                message = "username or password incorrect";
                if (success) {
                    message = "signin successfully";
                    ClientHandler.getGuestClientHandlerByName(name).loggedIn(username);
                }
                return createSigninResponse(success, message);

            case RequestType.SIGNUP:
                success = DataAccessLayer.insertPlayer(username, password);
                message = "username or password incorrect";
                if (success) {
                    message = "signin successfully";

                    ClientHandler.getGuestClientHandlerByName(name).loggedIn(username);
                }
                return createSignupResponse(success, message);

        }
        return "";
    }

    private String createSignupResponse(boolean success, String message) {
        return createResponse(RequestType.SIGNUP, "" + success, message);
    }

    private String createSigninResponse(boolean success, String message) {
        return createResponse(RequestType.SIGNIN, "" + success, message);
    }

}
