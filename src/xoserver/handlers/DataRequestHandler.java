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
import xoserver.handlers.client.ClientHandler;
import xoserver.handlers.game_room.GameRoomHandler;

/**
 *
 * @author fathy
 */
public class DataRequestHandler extends RequestHandler {

    private static DataRequestHandler instance;

    public DataRequestHandler() {
    }

    public ResponseReceiver newResponseReceiver() {
        return this::handle;
    }

    private String handle(String name, String request) throws IOException, NullPointerException, SQLException {
        String[] splitedRequest = request.split(RequestType.MESSAGE_SPLITER);
        boolean success = false;
        switch (splitedRequest[0]) {
            case RequestType.ONLINE_PLAYERS:
                return createOnlinePLayersResponse(name);
            case RequestType.REQUEST_GAME:
                success = sendToPlayer(createRequestGame(name), splitedRequest[1]);
                return sendRequestGameResponse(success);
            case RequestType.REQUEST_GAME_ANSWER:
                success = Boolean.valueOf(splitedRequest[2]);
                if (success) {
                    sendToPlayer(createAnswerGameRsponse(success, splitedRequest[1]), splitedRequest[1]);
                    getPlayerIntoGame(name, splitedRequest[1]);
                }
                return createAnswerGameRsponse(success, name);

            case RequestType.HISTORY:
                return createResponse(
                        RequestType.HISTORY,
                        "true",
                        DataAccessLayer.getGames(name),
                         "sent"
                );

            case RequestType.RECORDED_GAME:
                return createResponse(
                        RequestType.RECORDED_GAME,
                        "true",
                        DataAccessLayer.getGamePlays(Integer.parseInt(splitedRequest[1])),
                        "sent"
                );
                
        }
        return "";
    }

    private String createOnlinePLayersResponse(String name) {

        return createResponse(
                RequestType.ONLINE_PLAYERS,
                "true",
                "success",
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

    private boolean sendToPlayer(String response, String reciver) {
        List<ClientHandler> onlineClients = ClientHandler.getOnlineClientHandlers();
        String clientName;
        for (ClientHandler client : onlineClients) {
            clientName = client.getName();
            if (clientName.equals(reciver)) {
                try {
                    client.request(response);
                } catch (IOException ex) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private String createRequestGame(String sender) {
        return createResponse(RequestType.REQUEST_GAME, "true", sender + " requst to play with u ", sender
        );
    }

    private String sendRequestGameResponse(boolean success) {
        return createResponse(success, RequestType.SENDER_REQUEST_GAME_ANSWER, "request sent successfully", "failed to send request");
    }

    private String sendAnswerGameResponse(boolean success) {
        return createResponse(success, RequestType.RECEIVER_REQUEST_GAME_ANSWER, "answer sent successfully", "failed to send answer");
    }

    private String createAnswerGameRsponse(boolean success, String name) {
        return createResponse(success,
                RequestType.REQUEST_GAME_ANSWER,
                name + " accepted the request",
                name + " refused the request"
        );
    }

    private void getPlayerIntoGame(String receiver, String sender) throws IOException {
        new GameRoomHandler(sender, receiver);
    }

}
