package handler;



public class Player {

    private String name;
    private String password;
    private int score;
    private boolean status ;
private boolean IsPlaying;

    //used in retrive in signin
    public Player(String name, String password) {
        this.name = name;
        this.password = password;
   
    }

    public Player(String name, String password, int score) {
        this.name = name;
        this.password = password;
        this.score = score;
    }

    public Player(String name, int score, boolean status) {
   
        this.name = name;
        this.score = score;
        this.status = status;
    }

    //used to retrive data from db to leaderboard
    public Player( String name, int score) {
        
        this.name = name;
      
        this.score = score;
    }

    public Player(String name, boolean IsPlaying) {
        this.name = name;
        this.IsPlaying = IsPlaying;
    }


    Player() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
