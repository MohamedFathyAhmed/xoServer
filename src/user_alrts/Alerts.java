/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user_alrts;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Marina
 */
public class Alerts extends Alert {

    public static final String USER_NOT_FOUND="This User Not Founded";
    public static final String  RECORD_NOT_FOUNDED= "This Game Not Recorded";
    public static final String WRONG_PORT= "This Port is Wrong";
    public static final String WRONG_IP = "This IP Wrong";
    public static final String  HISTORY_NOT_FOUNDED = "Thir is No History Yet";
    public static final String LEAVING = "You will leave the Game, Are You Sure?";
    public static final String RECORD_GAME= "Start Recording";
    ////////////////////////////////////// worning
    public static final String MISSING_NAME = "Please Enter Your Name";
    public static final String MISSING_PASSWORD = "Please Enter Your Password";
    public static final String MISSING_PLAYER_ONE_NAME = "Please Enter Player One Name";
    public static final String  MISSING_PLAYER_TWO_NAME = "Please Enter Player Two Your Name";

    private  Alerts() {
        super(AlertType.CONFIRMATION);
    }

    public static Alert displayAlert(String message, AlertType type, AlertButtonResult res) {
        Alert alert = new Alert(type);
        alert.setTitle("Tic Tac Toe");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.APPLY
                || result.get() == ButtonType.OK
                || result.get() == ButtonType.YES) {
            res.IfOk();

        }

        if (result.get() == ButtonType.CLOSE
                || result.get() == ButtonType.CANCEL
                || result.get() == ButtonType.FINISH
                || result.get() == ButtonType.NO) {
            res.IfCancel();
        }

        return alert;

    }

}
