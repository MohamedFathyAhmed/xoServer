package data.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.prefs.Preferences;
import org.apache.derby.jdbc.EmbeddedDriver;

public class TicTacToeDatabase {

    private final String gameTable = " create table GAME "
            + " (ID INTEGER not null primary key, "
            + " PLAYER_1 VARCHAR(50) not null, "
            + " PLAYER_2 VARCHAR(50) not null, "
            + " DATE DATE not null, "
            + " WON_PLAYER VARCHAR(50), "
            + " FOREIGN KEY (PLAYER_1) REFERENCES PLAYER(NAME), "
            + " FOREIGN KEY (PLAYER_2) REFERENCES PLAYER(NAME), "
            + " FOREIGN KEY (WON_PLAYER) REFERENCES PLAYER(NAME)"
            + " ) ";

    private final String playTable = " create table PLAY"
            + " ( "
            + " ID INTEGER not null primary key, "
            + " POSITION SMALLINT not null, "
            + " PLAYER VARCHAR(50) not null, "
            + " GAME_ID INTEGER not null, "
            + " FOREIGN KEY (PLAYER) REFERENCES PLAYER(NAME),"
            + " FOREIGN KEY (GAME_ID) REFERENCES GAME(ID) "
            + " ) ";

    private final String playerTable = " create table PLAYER"
            + " ( "
            + " NAME VARCHAR(50) not null primary key, "
            + " PASSWORD VARCHAR(50) not null"
            + " ) ";

    private final String gameShapeTable = " create table GAME_SHAPE"
            + " ( "
            + "	ID INTEGER not null primary key, "
            + " GAME_ID INTEGER not null, "
            + " PLAYER VARCHAR(50) not null, "
            + " SHAPE_ID INTEGER not null, "
            + " FOREIGN KEY (PLAYER) REFERENCES PLAYER(NAME), "
            + " FOREIGN KEY (GAME_ID) REFERENCES GAME(ID), "
            + " FOREIGN KEY (SHAPE_ID) REFERENCES SHAPE(ID) "
            + " ) ";

    private final String shapeTable = " create table SHAPE "
            + " ( "
            + " ID INTEGER not null primary key, "
            + " NAME VARCHAR(10) not null "
            + " ) ";

    private final String pausedGamesTable = " create table PAUSED_GAMES "
            + " ( "
            + " ID INTEGER not null primary key, "
            + " GAME_ID INTEGER not null,"
            + " NEXT_PLAYER VARCHAR(50) not null, "
            + " TIME INTEGER,"
            + " FOREIGN KEY (NEXT_PLAYER) REFERENCES PLAYER(NAME), "
            + " FOREIGN KEY (GAME_ID) REFERENCES GAME(ID) "
            + " ) ";

    private Connection connection = null;

    private static TicTacToeDatabase instance = null;

    private TicTacToeDatabase() throws SQLException {
        Driver derbyDatabase = new EmbeddedDriver();
        DriverManager.registerDriver(derbyDatabase);
        connection = DriverManager.getConnection("jdbc:derby:tic_tac_toe;create=true");

//        setIsDatabaseCreated(false);
        if (getIsDatabaseCreated()) {
            return;
        }

        connection.createStatement().execute(playerTable);
        connection.createStatement().execute(gameTable);
        connection.createStatement().execute(shapeTable);
        connection.createStatement().execute(playTable);
        connection.createStatement().execute(gameShapeTable);
        connection.createStatement().execute(pausedGamesTable);

        setIsDatabaseCreated(true);
    }

    public static TicTacToeDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new TicTacToeDatabase();
        }
        return instance;
    }

    private boolean getIsDatabaseCreated() {
        Preferences prefs = Preferences.userNodeForPackage(TicTacToeDatabase.class);
        return prefs.getBoolean("isDatabaseCreated", false);
    }

    private void setIsDatabaseCreated(boolean isDatabaseCreated) {
        Preferences prefs = Preferences.userNodeForPackage(TicTacToeDatabase.class);
        prefs.putBoolean("isDatabaseCreated", isDatabaseCreated);
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() throws SQLException {
        connection.close();

    }

}
