/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import data.database.DataAccessLayer;
import xoserver.handlers.client.ClientHandler;

/**
 *
 * @author fathy
 */
public class AuthRequestHandler extends RequestHandler {

    public AuthRequestHandler() {

    }

    public ResponseReceiver newResponseReceiver() {
        return this::handle;
    }

    public synchronized String handle(String name, String request) throws NullPointerException {
        String[] splitedRequest = request.split(RequestType.MESSAGE_SPLITER);
        boolean success = false;
        
        switch (splitedRequest[0]) {
        
            case RequestType.SIGNIN:
                success = DataAccessLayer.login(splitedRequest[1], splitedRequest[2]);
                if (success) {
                    ClientHandler.getGuestClientHandlerByName(name).loggedIn(splitedRequest[1]);
                }
                return createSigninResponse(success);
          
            case RequestType.SIGNUP:
                success = DataAccessLayer.insertPlayer(splitedRequest[1], splitedRequest[2]);
                if (success) {
                    ClientHandler.getGuestClientHandlerByName(name).loggedIn(splitedRequest[1]);
                }
                return createSignupResponse(success);
         
//            case RequestType.LOGOUT:
//                ClientHandler.getOnlineClientHandlerByName(splitedRequest[1]).loggedOut();
//                return createLogoutResponse(true);
        }
        return "";
    }

    private String createSignupResponse(boolean success) {
        return createResponse(
                success,
                RequestType.SIGNUP,
                "signup succefully",
                "username already exist");
    }

    private String createLogoutResponse(boolean success) {
        return createResponse(
                success,
                RequestType.LOGOUT,
                "logout succefully",
                "failed to logout");
    }

    private String createSigninResponse(boolean success) {
        return createResponse(
                success,
                RequestType.SIGNIN,
                "signed in successfully",
                "username or password incorrect"
        );
    }

}
