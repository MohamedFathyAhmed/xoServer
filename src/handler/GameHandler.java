package handler;

import java.util.function.Consumer;

public class GameHandler {

    private char[] boardChars = {'-', '-', '-', '-', '-', '-', '-', '-', '-'};
    private boolean continueToPlay = true;

    public String playerName1;
    public String playerName2;
    public int scorePlayer1;
    public int scorePlayer2;
    private int index;
    private GameState statusGame;

    public GameHandler(String playerName1, String playerName2) {

        this.playerName1 = playerName1;
        this.playerName2 = playerName2;

        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;

    }

    public void insertMove(int index, char shape) {
     boardChars[index] = shape;
     String shapee  = Character.toString(shape);
         msgToTwoPlayer("move;;" + shape + ";;" + index, "move;;" + shape + ";;" + index);
        if (shapee.equals("x")){
                msgToTwoPlayer("Turn;;2", "Turn;;2");
        }
        else{
                msgToTwoPlayer("Turn;;1", "Turn;;1");
                }

         statusGame = checkIfGameIsOver();
        if (statusGame.equals("PLAYER_ONE_WON")) {
            msgToTwoPlayer("woow;;youwin", "woow;;youlose");
         
           //lsa mhndlt4 el score

        } else if (statusGame.equals("PLAYER_TWO_WON")) {
            msgToTwoPlayer("woow;;youlose", "woow;;youwin");
            
             //lsa mhndlt4 el score
             
        } else if (statusGame.equals("DRAW")) {
   
            msgToTwoPlayer("woow;;draw", "woow;;draw");
        }
    }

    public void resetGame() {
        for (char boardChar : boardChars) {
            boardChar = '-';
        }

    }

    private GameState checkIfGameIsOver() {

        final String xPattern = "(^(X..X..X..)|(.X..X..X.)|(..X..X..X)|(XXX......)|(...XXX...)|(......XXX)|(..X.X.X..)|(X...X...X)$)";
        final String oPattern;
        final char[] boardChars = {'-', '-', '-', '-', '-', '-', '-', '-', '-'};
        final Consumer<GameState> gameStateUpdater;
        oPattern = xPattern.replaceAll("X", "O");

        String board = new String(boardChars);

        if (board.matches(xPattern)) {
            return GameState.PLAYER_ONE_WON;
        } else if (board.matches(oPattern)) {
            return GameState.PLAYER_TWO_WON;
        } else if (board.contains("-")) {
            return GameState.ONGOING;
        } else {
            return GameState.DRAW;

        }

    }

    public char[] getBoard() {
        return boardChars;
    }

    public void setCell(char[] board) {
        boardChars = board;
    }
      public void msgToTwoPlayer(String msg1, String msg2) {
       //hna hb3t el msg ll player
    }

}
