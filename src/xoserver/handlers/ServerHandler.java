/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import xoserver.handlers.client.ClientHandler;

/**
 *
 * @author Marina
 */
public class ServerHandler implements Runnable {//implements Message{

    private boolean isRunning;
    private ServerSocket serverSocket;
    private final ErrorMessageSender errorMessageSender;
    private int portNumber;

    ServerHandler(int port, ErrorMessageSender errorMessageSender) throws IOException {

        this.errorMessageSender = errorMessageSender;
        this.portNumber = port;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                new ClientHandler(serverSocket.accept(),errorMessageSender);
            } catch (IOException ex) {
                errorMessageSender.sendMessage(ex.getMessage());
                try {
                    disConnect();
                    serverSocket = null;
                } catch (IOException ex1) {
                    errorMessageSender.sendMessage(ex1.getMessage());
                }

            }

        }
    }

    private void connect() throws IOException {
        isRunning = true;
        this.serverSocket = new ServerSocket(portNumber);
        new Thread(this).start();
    }

    private void disConnect() throws IOException {
        isRunning = false;
        serverSocket.close();

    }

}
