/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers.server;

import java.io.IOException;
import java.net.ServerSocket;
import xoserver.handlers.client.ClientHandler;
import xoserver.handlers.ErrorMessageSender;

/**
 *
 * @author Marina
 */
public class ServerHandler implements Runnable {

    private boolean isRunning;
    private ServerSocket serverSocket;
    private final ErrorMessageSender errorMessageSender;
    private int port;

    public ServerHandler(int port, ErrorMessageSender errorMessageSender) {
        this.errorMessageSender = errorMessageSender;
        this.port = port;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                new ClientHandler(serverSocket.accept(), errorMessageSender);
            } catch (IOException ex) {
                errorMessageSender.sendMessage(ex.getMessage());
                try {
                    if (!isRunning) {
                        disConnect();
                        serverSocket = null;
                    }
                } catch (IOException ex1) {
                    errorMessageSender.sendMessage(ex1.getMessage());
                }
            }
        }
    }

    public void connect() throws IOException {
        isRunning = true;
        this.serverSocket = new ServerSocket(port);
        new Thread(this).start();
    }

    public void connect(int port) throws IOException {
        this.port = port;
        connect();
    }

    public void toggleConnection() throws IOException {
        if (isRunning) {
            disConnect();
        } else {
            connect();
        }
    }

    public void disConnect() throws IOException {
        if (isRunning) {
            isRunning = false;
            ClientHandler.closeAll();
            serverSocket.close();      
        }
    }
}
