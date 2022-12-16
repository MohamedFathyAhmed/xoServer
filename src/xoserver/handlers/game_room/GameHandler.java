/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xoserver.handlers.game_room;

import java.util.function.Consumer;

/**
 *
 * @author mohamed
 */
public class GameHandler {

    private static final String X_PATTERN = "(^(X..X..X..)|(.X..X..X.)|(..X..X..X)|(XXX......)|(...XXX...)|(......XXX)|(..X.X.X..)|(X...X...X)$)";
    private static final String O_PATTERN = "(^(O..O..O..)|(.O..O..O.)|(..O..O..O)|(OOO......)|(...OOO...)|(......OOO)|(..O.O.O..)|(O...O...O)$)";

    public static synchronized char[] updateBoard(int position, String shape, char[] boardChars) {
        boardChars[position] = shape.charAt(0);
        return boardChars;
    }

    public static synchronized GameState updateGameState(String board) {
        if (board.matches(X_PATTERN)) {
            return GameState.PLAYER_ONE_WON;
        } else if (board.matches(O_PATTERN)) {
            return GameState.PLAYER_TWO_WON;
        } else if (board.contains("-")) {
            return GameState.ONGOING;
        } else {
            return GameState.DRAW;
        }
    }

}
