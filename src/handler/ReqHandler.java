package handler;



import DB.DataAccessLayer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReqHandler {
public String msg;


    public String senderName ;
    public String receverName ;
    boolean isPlaying = false;

    public ReqHandler(String msg) {
      this.msg=msg;
    

                System.out.println("message from client to server" + msg);
                String[] msgSplit = msg.split(";;");
                switch (msgSplit[0]) {
                    case "signUp":
                        //sender+pass
                        signUp(msgSplit);
                        break;
                    case "signIn":
                        //sender + pass
                        signIn(msgSplit);
                        break;               
                    case "Logout":
                        //sender
                        logOut(msgSplit[1]);
                        break;
                    case "InvitaionTo":
                        //sender + rcever
                        sendinvetationTo(msgSplit[1], msgSplit[2]);
                        break;
                    case "InvitaionResponse":
                        //sender + rcever + anwser
                        sendResponseTo(msgSplit[1], msgSplit[2], msgSplit[3]);
                        break;
                    case "updateGame":
                       // sender + position
                        updateGame(msgSplit[1]);
                        break;
                    case "startGame":
                        //sender +rceiver
                        startGame();
                        break;
                    case "quitFromGame":
                        handlePlayerWantToQuit();
                        break;
                         case "playAgain":
                             //sender +rcever
                        handlePlayerWantToQuit();
                        break;
                    case "saveGameForLater":
                        handleSaveGameForLater();
                        break;
                    case "ChangeIsPlaying":
                        //sender
                        changeIsPlaying(senderName, Boolean.parseBoolean(msgSplit[1]));
                        break;
                     case "Record":
                       
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
              if(isPlaying)
        {
            handlePlayerWantToQuit();
            handleExitPlayer(senderName);
        }

     
    }

    public void handleExitPlayer(String senderName) {
        
 
    }

    public void handlePlayerWantToQuit() {
     
 
    }
   
    public void changeIsPlaying(String senderName, boolean isplaying) {
     this.isPlaying=isplaying;
    }

    public void startGame() {
        
   

    }

    public void updateGame(String index) {
        
    }


    public void sendinvetationTo( String senderName, String receverName) {
 
        System.out.println("InvetationFrom;;" + senderName + ";;" + receverName);
     
    }

    public void sendResponseTo(String senderName, String receverName ,String resp) {
        
        
        System.out.println("ResponsetoInvetation;;" + resp + ";;" + receverName);
      
        if (resp.equals("yes")) {
          //yes  
        }
        //no
      
    }

 

    public void handleSaveGameForLater() {
      
        }


 
   
}


