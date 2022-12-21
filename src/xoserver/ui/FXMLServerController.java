/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package xoserver.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.CircularArray;
import xoserver.handlers.ErrorMessageSender;
import xoserver.handlers.client.ClientHandler;
import xoserver.handlers.server.ServerHandler;

/**
 * FXML Controller class
 *
 * @author mo_fathy
 */
public class FXMLServerController implements Initializable {

    @FXML
    private PieChart piechart;

    @FXML
    private TextField portTextField;

    @FXML
    private final CircularArray<String> startButtonNames = new CircularArray("Start", "Stop");

    private ServerHandler serverHandler;
    private ErrorMessageSender errorMessageSender;

    private ObservableList<PieChart.Data> piechartData;

    private Consumer<Integer> onlineClientsUpdater;
    private Consumer<Integer> guestClientsUpdater;
    private Consumer<Integer> inGameClientsUpdater;

    public FXMLServerController(Stage stage) {
        setupClientsUpdater();

        stage.setOnCloseRequest((event) -> {
            try {
                serverHandler.disConnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPieChar();
        ClientHandler.updateClientsUpdater();
        setupClientsUpdater();
        setupServer();

    }

    @FXML
    public void startButtonClicked(ActionEvent event) {
        try {
            startButtonNames.next();

            ((Button) event.getSource()).setText(startButtonNames.get());

            serverHandler.toggleConnection();

            piechart.setVisible(!piechart.visibleProperty().get());
        } catch (IOException ex) {
            //catch me
            ex.printStackTrace();
        }
    }

    private void setupClientsUpdater() {
        ClientHandler.setOnlineClientsUpdater((count) -> piechartData.get(0).setPieValue(count));
        ClientHandler.setGuestClientsUpdater((count) -> piechartData.get(1).setPieValue(count));
        ClientHandler.setInGameClientsUpdater((count) -> piechartData.get(2).setPieValue(count));
        ClientHandler.setOfflineClientsUpdater((count) -> piechartData.get(3).setPieValue(count));
    }

    private void setupPieChar() {
        piechartData = FXCollections.observableArrayList(
                new PieChart.Data("Online", 0),
                new PieChart.Data("Guest", 0),
                new PieChart.Data("In Game", 0),
                new PieChart.Data("Offline", 0));

        piechart.setData(piechartData);
    }

    private void setupServer() {
        errorMessageSender = (message) -> {
            //show me

        };
        serverHandler = new ServerHandler(Integer.parseInt(portTextField.getText()), errorMessageSender);
    }

}
