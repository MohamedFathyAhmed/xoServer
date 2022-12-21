/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers.game_room;

import data.database.DataAccessLayer;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import xoserver.handlers.RequestHandler;
import xoserver.handlers.RequestType;
import xoserver.handlers.client.ClientHandler;

/**
 *
 * @author mohamed
 */
public class GameRoomHandler extends RequestHandler {

    private final List<Play> plays;
    private char[] boardChars = {'-', '-', '-', '-', '-', '-', '-', '-', '-'};

    private boolean isRecording;

    private final GameShapes player1Shape;
    private final GameShapes player2Shape;

    private final ClientHandler player1;
    private final ClientHandler player2;

    public GameRoomHandler(String sender, String receiver) throws IOException {
        this.player1Shape = GameShapes.X;
        this.player2Shape = GameShapes.O;

        isRecording = false;
        plays = new ArrayList();

        player1 = ClientHandler.getOnlineClientHandlerByName(sender);
        player1.getInGame(this::handle1);
        player2 = ClientHandler.getOnlineClientHandlerByName(receiver);
        player2.getInGame(this::handle2);
    }

    private String handle1(String name, String request) throws IOException, SQLException {
        String[] splitedRequest = request.split(RequestType.MESSAGE_SPLITER);
        switch (splitedRequest[0]) {
            case RequestType.PLAY:
                GameState gameState = updatePlayer1GameState(splitedRequest[1]);
                switch (gameState) {
                    case ONGOING:
                        player2.request(createPlayResponse(splitedRequest[1], true));
                        return createPlayResponse(splitedRequest[1], false);

                    default:
                        sendLastGameResponse(splitedRequest[1]);
                        insertGameToDatabase(gameState);
                        return gameDoneResponse1(gameState);
                }

             case RequestType.PLAY_AGAIN_ANSWER:
                resetGame();
                player2.request(createPlayAgainResponse());
                return createPlayAgainResponse();

            case RequestType.PLAY_AGAIN:
                player2.request(createPlayAgainResponse());
                return createPlayAgainResponse();

            case RequestType.RECORD:
                isRecording = !isRecording;
                player2.request(createRecordingResponse(isRecording));
                return createRecordingResponse(isRecording);

            case RequestType.LEAVE:
                leave();
                player2.request(createLeaveResponse());
                return createLeaveResponse();
        }
        return "";
    }

    private String handle2(String name, String request) throws IOException, SQLException {
        String[] splitedRequest = request.split(RequestType.MESSAGE_SPLITER);
        switch (splitedRequest[0]) {
            case RequestType.PLAY:
                GameState gameState = updatePlayer2GameState(splitedRequest[1]);
                switch (gameState) {
                    case ONGOING:
                        player1.request(createPlayResponse(splitedRequest[1], true));
                        return createPlayResponse(splitedRequest[1], false);

                    case PLAYER_TWO_WON:
                    case PLAYER_ONE_WON:
                    case DRAW:
                        sendLastGameResponse(splitedRequest[1]);
                        insertGameToDatabase(gameState);
                        return gameDoneResponse2(gameState);

                }

            case RequestType.PLAY_AGAIN_ANSWER:
                resetGame();
                player1.request(createPlayAgainResponse());
                return createPlayAgainResponse();

            case RequestType.PLAY_AGAIN:
                player1.request(createPlayAgainResponse());
                return createPlayAgainResponse();

            case RequestType.RECORD:
                isRecording = !isRecording;
                player2.request(createRecordingResponse(isRecording));
                return createRecordingResponse(isRecording);

            case RequestType.LEAVE:
                leave();
                player1.request(createLeaveResponse());
                return createLeaveResponse();

        }
        return "";
    }

    private void resetBoardChars() {
        for (char c : boardChars) {
            c = '-';
        }
    }

    private void resetGame() {
        plays.clear();
        resetBoardChars();
        isRecording = false;
    }

    private void leave() {
        player1.getOutOfGame();
        player2.getOutOfGame();
    }

    private String createPlayResponse(String position, boolean myTurn) {
        return createResponse(RequestType.PLAY,
                position,
                myTurn + ""
        );
    }

    private String createPlayAgainResponse() {
        return createResponse(
                RequestType.PLAY_AGAIN
        );
    }

    private String gameDoneResponse1(GameState gameState) throws SQLException, IOException {
        String response = createResponse(RequestType.GAME_DONE, gameState.name());
        player2.request(response);
        return response;
    }

    private String gameDoneResponse2(GameState gameState) throws SQLException, IOException {
        String response = createResponse(RequestType.GAME_DONE, gameState.name());
        player1.request(response);
        return response;
    }

    private void sendLastGameResponse(String position) throws IOException {
        player1.request(createPlayResponse(position, false));
        player2.request(createPlayResponse(position, false));
    }

    private void insertGameToDatabase(GameState gameState) throws SQLException {
        String wonPlayer = null;
        switch (gameState) {
            case PLAYER_ONE_WON:
                wonPlayer = player1.getName();
                break;
            case PLAYER_TWO_WON:
                wonPlayer = player2.getName();
        }
        int gameId = DataAccessLayer.insertGame(player1.getName(),
                player2.getName(),
                getCurrentDate(),
                wonPlayer);
        if (isRecording) {
            DataAccessLayer.insertPlays(plays, gameId);
        }
    }

    private String getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        return localDate.getYear()
                + "-"
                + localDate.getMonthValue()
                + "-"
                + localDate.getDayOfMonth();
    }

    private void addPlayer2playOnBoard(int position) {
        boardChars = GameHandler.updateBoard(position, GameShapes.O.name(), boardChars);
    }

    private void addPlayer1playOnBoard(int position) {
        boardChars = GameHandler.updateBoard(position, GameShapes.X.name(), boardChars);
    }

    private void addPlayer1play(int position) {
        plays.add(new Play(position, player1.getName()));
    }

    private void addPlayer2play(int position) {
        plays.add(new Play(position, player2.getName()));
    }

    private GameState updatePlayer1GameState(String positionString) {
        int position = Integer.parseInt(positionString);
        addPlayer1playOnBoard(position);
        addPlayer1play(position);
        if (plays.size() > 4) {
            return GameHandler.updateGameState(new String(boardChars));
        } else {
            return GameState.ONGOING;
        }
    }

    private GameState updatePlayer2GameState(String positionString) {
        int position = Integer.parseInt(positionString);
        addPlayer2playOnBoard(position);
        addPlayer2play(position);
        if (plays.size() > 4) {
            return GameHandler.updateGameState(new String(boardChars));
        } else {
            return GameState.ONGOING;
        }
    }

    private String createPlayAgainAnswerResponse(String success) {
        return createResponse(RequestType.PLAY_AGAIN_ANSWER, success);
    }

    private String createLeaveResponse() {
        return createResponse(RequestType.LEAVE);
    }

    private String createRecordingResponse(boolean recording) {
        return createResponse(RequestType.LEAVE, recording + "");
    }

}
