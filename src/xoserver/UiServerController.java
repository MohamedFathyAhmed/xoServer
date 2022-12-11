/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package xoserver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

/**
 * FXML Controller class
 *
 * @author mo_fathy
 */
public class UiServerController implements Initializable {

    @FXML
    private PieChart piechart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
             ObservableList<PieChart.Data> piechartData =
            FXCollections.observableArrayList(
                     new PieChart. Data ("online",10),
                  
                     new PieChart. Data ("ofline",23));
    
                        piechart.setData(piechartData);
    }    
    
}
