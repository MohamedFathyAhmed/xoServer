package data.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.prefs.Preferences;
import org.apache.derby.jdbc.EmbeddedDriver;


public class TicTacToeDatabase {

    private final String gameTable = " CREATE TABLE GAME "
            + " (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
            + " PLAYER_1 VARCHAR(50) NOT NULL, "
            + " PLAYER_2 VARCHAR(50) NOT NULL, "
            + " PLAYER_1_SHAPE VARCHAR(1) NOT NULL, "
            + " PLAYER_2_SHAPE VARCHAR(1) NOT NULL, "
            + " DATE DATE NOT NULL, "
            + " WON_PLAYER VARCHAR(50) NOT NULL, "
            + " RECORDED BOOLEAN NOT NULL,"
            + " FOREIGN KEY (PLAYER_1) REFERENCES PLAYER(NAME), "
            + " FOREIGN KEY (PLAYER_2) REFERENCES PLAYER(NAME), "
            + " FOREIGN KEY (WON_PLAYER) REFERENCES PLAYER(NAME)"
            + " ) ";

    private final String playTable = " CREATE TABLE PLAY"
            + " ( "
            + " ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
            + " POSITION SMALLINT NOT NULL, "
            + " PLAYER VARCHAR(50) NOT NULL, "
            + " GAME_ID INTEGER NOT NULL, "
            + " FOREIGN KEY (PLAYER) REFERENCES PLAYER(NAME),"
            + " FOREIGN KEY (GAME_ID) REFERENCES GAME(ID) "
            + " ) ";

    private final String playerTable = " CREATE TABLE PLAYER"
            + " ( "
            + " NAME VARCHAR(50) NOT NULL PRIMARY KEY,"
            + " PASSWORD VARCHAR(50) NOT NULL "
            + " ) ";

    private Connection connection = null;

    private static TicTacToeDatabase instance = null;

    private TicTacToeDatabase() throws SQLException {
        Driver derbyDatabase = new EmbeddedDriver();
        DriverManager.registerDriver(derbyDatabase);
        try {
            connection = DriverManager.getConnection("jdbc:derby:server_tic_tac_toe_db;ifexists=true", "root", "root");
        } catch (SQLException e) {
            connection = DriverManager.getConnection("jdbc:derby:server_tic_tac_toe_db;create=true", "root", "root");
            initDababase();
        }

    }

    public static TicTacToeDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new TicTacToeDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() throws SQLException {
        connection.close();

    }

    private void initDababase() throws SQLException {
        connection.createStatement().execute(playerTable);
        connection.createStatement().execute(gameTable);
        connection.createStatement().execute(playTable);
        connection.commit();
        connection.createStatement().execute("INSERT INTO PLAYER (NAME) VALUES ('EASY')");
        connection.commit();
    }

}
