/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;
import xoserver.handlers.ErrorMessageSender;
import xoserver.handlers.RequestHandlerSwitcher;
import xoserver.handlers.ResponseReceiver;

/**
 *
 * @author Apple
 */
public class ClientHandler {

    private static final Vector<ClientHandler> onlineClients = new Vector();

    private final StreamHandler streamHandler;
    private final ErrorMessageSender errorMessageSender;
    private final Socket socket;
    private final RequestHandlerSwitcher requestHandlerSwitcher;
    private ResponseReceiver responseReceiver;
    private xoserver.handlers.client.ClientState state;
    private String name;

    public ClientHandler(Socket socket, ErrorMessageSender errorMessageSender) throws IOException {
        this.requestHandlerSwitcher = RequestHandlerSwitcher.getInstance();
        this.errorMessageSender = errorMessageSender;
        this.socket = socket;
        responseReceiver = requestHandlerSwitcher.newAuthRequestHandler();
        streamHandler = new StreamHandler();
        state = xoserver.handlers.client.ClientState.GUEST;
        streamHandler.start();
        onlineClients.add(this);
    }

    public ResponseReceiver getInGame(ResponseReceiver responseReceiver) {
        ResponseReceiver oldResponseReceiver = this.responseReceiver;
        state = xoserver.handlers.client.ClientState.BUSY;
        this.responseReceiver = responseReceiver;
        return oldResponseReceiver;
    }

    public void getOutOfGame(ResponseReceiver responseReceiver) {
        this.responseReceiver = responseReceiver;
        state = xoserver.handlers.client.ClientState.FREE;
    }

    void loggedIn(String name) {
        this.name = name;
        state = xoserver.handlers.client.ClientState.FREE;
        responseReceiver = requestHandlerSwitcher.newDataRequestHandler();
    }

    void loggedOut() {
        this.name = "";
        state = xoserver.handlers.client.ClientState.GUEST;
        responseReceiver = requestHandlerSwitcher.newAuthRequestHandler();
    }

    public static Vector<ClientHandler> getClientHandlers() {
        return onlineClients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseReceiver getCurrentResponseReceiver() {
        return responseReceiver;
    }

    public static ClientHandler getClientHandlerByName(String name) {
        for (ClientHandler clientHandler : onlineClients) {
            if (clientHandler.getName().equals(name)) {
                return clientHandler;
            }
        }
        return null;
    }

    void close() throws IOException {
        streamHandler.close();
        socket.close();
        onlineClients.remove(this);
    }

    void closeAll() throws IOException {
        for (ClientHandler clientHandler : onlineClients) {
            clientHandler.close();
        }
    }

    class StreamHandler extends Thread {

        private final BufferedReader bufferedReader;
        private final BufferedWriter bufferedWriter;
        private boolean isRunning;

        public StreamHandler() throws IOException {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    write(responseReceiver.sendData(bufferedReader.readLine()));
                } catch (IOException ex) {
                    errorMessageSender.sendMessage(ex.getMessage());
                    try {
                        close();
                    } catch (IOException ex1) {
                        errorMessageSender.sendMessage(ex1.getMessage());
                    }
                }
            }
        }

        void write(String response) throws IOException {
            bufferedWriter.write(response);
        }

        void close() throws IOException {
            isRunning = false;
            bufferedReader.close();
            bufferedWriter.close();
        }
    }
}
