package xoserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import data.database.DataAccessLayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xoserver.ui.FXMLServerController;

/**
 *
 * @author mohamed
 */
public class XoServer extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        DataAccessLayer.connect();
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/FXMLServer.fxml"));
        loader.setController(new FXMLServerController(stage));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataAccessLayer.disconnect();
    }

}
