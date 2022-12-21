/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import data.database.DataAccessLayer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import xoserver.handlers.client.ClientHandler;
import xoserver.handlers.game_room.GameRoomHandler;

/**
 *
 * @author mohamed
 */
public class DataRequestHandler extends RequestHandler {

    public ResponseReceiver newResponseReceiver() {
        return this::handle;
    }

    private String handle(String name, String request) throws IOException, NullPointerException, SQLException {
        String[] splitedRequest = request.split(RequestType.MESSAGE_SPLITER);
        boolean success;
        switch (splitedRequest[0]) {

            case RequestType.ONLINE_PLAYERS:
                return createOnlinePLayersResponse(name);

            case RequestType.REQUEST_GAME:
                success = sendToPlayer(splitedRequest[1], createRequestGame(name));
                return createRequestGameResponse(success);

            case RequestType.REQUEST_GAME_ANSWER:
                success = Boolean.valueOf(splitedRequest[2]);
                if (success) {
                    success = sendToPlayer(splitedRequest[1], createRequestGameAnswer(success));
                    getPlayersIntoGame(name, splitedRequest[1]);
                }
                return createRequestGameAnswer(success);

            case RequestType.HISTORY:
                return createResponse(
                        RequestType.HISTORY,
                        DataAccessLayer.getGames(name)
                );

            case RequestType.RECORDED_GAME:
                return createResponse(
                        RequestType.RECORDED_GAME,
                        DataAccessLayer.getGamePlays(Integer.parseInt(splitedRequest[1]))
                );

            case RequestType.LOGOUT:
                getPlayerByName(name).loggedOut();
                return "";

        }
        return "";
    }

    private String createOnlinePLayersResponse(String name) {
        return createResponse(
                RequestType.ONLINE_PLAYERS,
                getOnlinePlayersAsString(name));

    }

    private String getOnlinePlayersAsString(String name) {
        List<ClientHandler> onlineClients = ClientHandler.getOnlineClientHandlers();
        String clientsString = "";
        String clientName;

        for (ClientHandler client : onlineClients) {
            clientName = client.getName();
            if (!clientName.equals(name)) {
                clientsString += clientName + RequestType.ARRAY_SPLITER;
            }
        }
        return clientsString;
    }

    private boolean sendToPlayer(String reciver, String response) {
        try {
            getPlayerByName(reciver).request(response);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private String createRequestGame(String sender) {
        return createResponse(RequestType.REQUEST_GAME, sender);
    }

    private String createRequestGameResponse(boolean success) {
        return createResponse(RequestType.SENDER_REQUEST_GAME_ANSWER, "" + success);
    }

    private void getPlayersIntoGame(String receiver, String sender) throws IOException {
        new GameRoomHandler(sender, receiver);
    }

    private String createRequestGameAnswer(boolean success) {
        return createResponse(RequestType.REQUEST_GAME_ANSWER, "" + success);
    }

    private ClientHandler getPlayerByName(String name) {
        List<ClientHandler> onlineClients = ClientHandler.getOnlineClientHandlers();
        String clientName;
        for (ClientHandler client : onlineClients) {
            clientName = client.getName();
            if (clientName.equals(name)) {
                return client;
            }
        }
        return null;
    }

}
