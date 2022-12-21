/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

/**
 *
 * @author Marina
 */
public interface RequestType {

    public static final String MESSAGE_SPLITER = ";;";
    public static final String ARRAY_SPLITER = "~~";
    public static final String OBJECT_SPLITER = "~";
    public static final String SIGNIN = "signin";
    public static final String CONNECTED = "connected";
    public static final String SIGNUP = "signup";
    public static final String RECORD = "record";
    public static final String RECORDED_GAMES_LIST = "recorded_games_list";
    public static final String LOGOUT = "logout";
    public static final String REQUEST_GAME = "requst_game";
    public static final String PLAY_AGAIN = "play_again";
    public static final String HISTORY = "history";
    public static final String LEAVE = "leave";
    public static final String ONLINE_PLAYERS = "online_players";
    public static final String SHAPE = "shape";
    public static final String RECORDED_GAME = "recorded_game";
    public static final String SENDER_REQUEST_GAME_ANSWER ="sender_request_game_answer";
    public static final String RECEIVER_REQUEST_GAME_ANSWER ="receiver_request_game_answer";
    public static final String REQUEST_GAME_ANSWER ="request_game_answer";
    public static final String PLAY ="play";
    public static final String PLAY_AGAIN_ANSWER ="play_again_answer";
    public static final String GAME_DONE = "game_done";
    public static final String REDIRECT_PLAY="redirect_play";
    public static final String INFO="info";
}
