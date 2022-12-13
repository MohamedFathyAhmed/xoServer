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
                        sendInvetationTo(msgSplit[1], msgSplit[2]);
                        break;
                    case "InvitaionResponse":
                        //sender + rcever + anwser
                        sendResponseTo(msgSplit[1], msgSplit[2], msgSplit[3]);
                        break;
                    case "updateGame":
                       // sender + position 
                        updateGame(msgSplit[1], msgSplit[2]);
                        break;
                    case "startGame":
                        //sender +rceiver
                        startGame(msgSplit[1], msgSplit[2]);
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
                        //sender+bool
                        changeIsPlaying(senderName, Boolean.parseBoolean(msgSplit[1]));
                        break;
                         case "playAgainReq":
                        //sender+bool
                        playAgainReq(senderName);
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

    public void startGame(String senderName, String receverName) {
   

    }

    public void updateGame(String senderName,String index) {
        
  
    }


    public void sendInvetationTo( String senderName, String receverName) {
 
        System.out.println("Invetation;;" + senderName + ";;" + receverName);
     
    }

    public void sendResponseTo(String senderName, String receverName ,String response) {
        
        
        System.out.println("Response;;" + response + ";;" + receverName);
      
        if (response.equals("yes")) {
          //yes  
        }
        //no
      
    }

    public void handleSaveGameForLater() {
      
        }

    private void playAgainReq(String senderName) {
       
    }


 
   
}


