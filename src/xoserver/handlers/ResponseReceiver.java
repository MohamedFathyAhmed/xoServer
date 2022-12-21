/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import java.io.IOException;
import java.sql.SQLException;
import javafx.util.Pair;

/**
 *
 * @author mohamed
 */
@FunctionalInterface
public interface ResponseReceiver {
    String sendData(String name, String request) throws IOException, SQLException;
}
