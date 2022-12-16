/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author mohamed
 */
public class ClientHanlder {

    private static final Vector<ClientHanlder> onlineClients = new Vector();

    private final StreamHandler streamHandler;
    private final ErrorMessageSender errorMessageSender;
    private ResponseReceiver responseReceiver;
    private final Socket socket;
    private ClientState state;
    private String name;

    public ClientHanlder(Socket socket, ErrorMessageSender errorMessageSender) throws IOException {
        this.errorMessageSender = errorMessageSender;
        this.socket = socket;

        streamHandler = new StreamHandler();
        state = ClientState.GUEST;
        streamHandler.start();
        onlineClients.add(this);
    }

    ResponseReceiver getInGame(ResponseReceiver responseReceiver) {
        ResponseReceiver oldResponseReceiver = this.responseReceiver;
        state = ClientState.BUSY;
        this.responseReceiver = responseReceiver;
        return oldResponseReceiver;
    }

    void getOutOfGame(ResponseReceiver responseReceiver) {
        this.responseReceiver = responseReceiver;
        state = ClientState.FREE;
    }

    void loggedIn() {
        state = ClientState.FREE;
    }

    void loggedOut() {
        state = ClientState.GUEST;
    }

    void setState(ClientState state) {
        this.state = state;
    }

    public ClientState getState() {
        return state;
    }

    public static Vector<ClientHanlder> getClientHanlders() {
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

    public static ClientHanlder getClientHanlderByName(String name) {
        for (ClientHanlder clientHanlder : onlineClients) {
            if (clientHanlder.getName().equals(name)) {
                return clientHanlder;
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
        for (ClientHanlder clientHanlder : onlineClients) {
            clientHanlder.close();
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
