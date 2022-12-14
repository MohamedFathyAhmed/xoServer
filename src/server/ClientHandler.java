/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Apple
 */
public class ClientHandler {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private String name;
    private static final Vector<ClientHandler> onlineClients = new Vector<>();
    private ClientState state;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        onlineClients.add(this);
        state = ClientState.GUEST;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void close() throws IOException {

        socket.close();
        bufferedReader.close();
        bufferedWriter.close();
        onlineClients.remove(this);

    }

    public static String[] getAll() {
        String[] onlineClientsNames = new String[ClientHandler.onlineClients.size()];
        for (int i = 0; i < onlineClients.size(); i++) {
            onlineClientsNames[i] = onlineClients.get(i).getName();
        }
        return onlineClientsNames; //OnlineClinets;
    }

    public static Vector<ClientHandler> getOnlineClients() {

        return onlineClients;

    }

// to search for client in vector 
    public static ClientHandler getClientByName(String name) {

        for (int i = 0; i < onlineClients.size(); i++) {
            if (onlineClients.get(i).getName().equals(name)) {
                return onlineClients.get(i);
            }
        }
        return null;
    }

    public static void closeAll() throws IOException {
        for (int i = 0; i < onlineClients.size(); i++) {
            onlineClients.get(i).close();

        }

    }

    //
    class StreamHandler extends Thread {

        private boolean isRunning;
        
        // to read from client request 
        @Override
        public void run() {
            
            while (isRunning) {

                bufferedReader.readLine();

            }

        }

        void close() {

            isRunning = false;

        }
        // to write answer for client
        void write(String response) throws IOException{
            
            bufferedWriter.write(response);
            
        }
    }
}
