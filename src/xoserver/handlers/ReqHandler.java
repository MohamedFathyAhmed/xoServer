package xoserver.handlers;

import data.database.DataAccessLayer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReqHandler {

    public String msg;

    public String senderName;
    public String receverName;
    boolean isPlaying = false;

    public ReqHandler(String msg) {
        this.msg = msg;

        System.out.println("message from client to server" + msg);
        String[] msgSplit = msg.split(Requesconstants.MESSAGE_SPLITER);
        switch (msgSplit[0]) {
            case Requesconstants.SIGNUP:
                //sender+pass/
                signUp(msgSplit);
                break;
            case Requesconstants.SIGNIN:
                //sender + pass /
                signIn(msgSplit);
                break;
            case Requesconstants.LOGOUT:
                //sender
                logOut(msgSplit[1]);
                break;
            case Requesconstants.REQUEST_GAME:
                //sender + rcever
                sendinvetationTo(msgSplit[1], msgSplit[2]);
                break;
            case Requesconstants.RESPONSE_REQUEST_GAME:
                //sender + rcever + anwser
                sendResponseTo(msgSplit[1], msgSplit[2], msgSplit[3]);
                break;
//                    case "updateGame":
//                       // sender + position 
//                        updateGame(msgSplit[1], msgSplit[2]);
//                        break;
//                    case "startGame":
//                        //sender +rceiver
//                        startGame(msgSplit[1], msgSplit[2]);
//                        break;
            case Requesconstants.LEAVE:
                handlePlayerWantToQuit();
                break;
            case Requesconstants.PLAYAGAIN:
                //sender +rcever
                playAgainReq(senderName);
                break;
            case Requesconstants.RECORD:
                handleSaveGameForLater();
                break;
            case Requesconstants.RECORDED_GAMES_LIST:
                recordGamesList(senderName);
                break;
            case Requesconstants.ONLINE_PLAYERS:
                onlinePlayers();
                break;
            case Requesconstants.SHAPE:
                getShape();
                break;
            case Requesconstants.RECORDED_GAME:
                //sender+bool
                recordedGame();
                break;

        }

    }

    public void signUp(String[] msg) {

        try {
            //name + pass + 0
            Player player = new Player(msg[1], msg[2], 0);
            String res = DataAccessLayer.CreatePlayer(player);//lsa
        } catch (SQLException ex) {
            Logger.getLogger(ReqHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void signIn(String[] msg) {
        //name +pass
        String res = DataAccessLayer.getPlayer(msg[1], msg[2]); //lsa
        String[] resArr = res.split(";;");
        if (Boolean.parseBoolean(resArr[0])) {

            this.senderName = resArr[1];

        }

    }

    public void logOut(String nameId) {
        if (isPlaying) {
            handlePlayerWantToQuit();
            handleExitPlayer(senderName);
        }

    }

    public void handleExitPlayer(String senderName) {

    }

    public void handlePlayerWantToQuit() {

    }

    public void changeIsPlaying(String senderName, boolean isplaying) {
        this.isPlaying = isplaying;
    }

    public void startGame(String senderName, String receverName) {

    }

    public void updateGame(String senderName, String index) {

    }

    public void sendinvetationTo(String senderName, String receverName) {

        System.out.println("InvetationFrom;;" + senderName + ";;" + receverName);

    }

    public void sendResponseTo(String senderName, String receverName, String resp) {

        System.out.println("ResponsetoInvetation;;" + resp + ";;" + receverName);

        if (resp.equals("yes")) {
            //yes  
        }
        //no

    }

    public void handleSaveGameForLater() {

    }

    private void playAgainReq(String senderName) {

    }

    private void recordGamesList(String senderName) {
   }

    private void onlinePlayers() {
   }

    private void getShape() {
   }

    private void recordedGame() {
    }

}
