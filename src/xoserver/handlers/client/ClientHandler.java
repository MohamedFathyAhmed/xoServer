/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers.client;

import data.database.DataAccessLayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import xoserver.handlers.ErrorMessageSender;
import xoserver.handlers.RequestHandlerSwitcher;
import xoserver.handlers.ResponseReceiver;

/**
 *
 * @author Apple
 */
public class ClientHandler {

    private static final Vector<ClientHandler> onlineClients = new Vector();
    private static final Vector<ClientHandler> guestClients = new Vector();
    private static final Vector<ClientHandler> inGameClients = new Vector();
    private static final Vector<String> names = new Vector<>();

    private static Consumer<Integer> guestClientsUpdater;
    private static Consumer<Integer> onlineClientsUpdater;
    private static Consumer<Integer> inGameClientsUpdater;
    private static Consumer<Integer> offlineClientsUpdater;

    private StreamHandler streamHandler;
    private ErrorMessageSender errorMessageSender;
    private Socket socket;
    private RequestHandlerSwitcher requestHandlerSwitcher;

    private ResponseReceiver responseReceiver;
    private String name;

    public ClientHandler(Socket socket, ErrorMessageSender errorMessageSender) throws IOException {
        this.socket = socket;
        this.requestHandlerSwitcher = RequestHandlerSwitcher.getInstance();
        this.errorMessageSender = errorMessageSender;
        responseReceiver = requestHandlerSwitcher.newAuthRequestHandler();
        streamHandler = new StreamHandler();
        names.add(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress());
        streamHandler.start();
        name = Thread.currentThread().getName();
        guestClients.add(this);
        updateClientsUpdater();
        sendConnectedSuccessfully();
    }

    public static void setOnlineClientsUpdater(Consumer<Integer> onlineClientsUpdater) {
        ClientHandler.onlineClientsUpdater = onlineClientsUpdater;
    }

    public static void setGuestClientsUpdater(Consumer<Integer> guestClientsUpdater) {
        ClientHandler.guestClientsUpdater = guestClientsUpdater;
    }

    public static void setInGameClientsUpdater(Consumer<Integer> inGameClientsUpdater) {
        ClientHandler.inGameClientsUpdater = inGameClientsUpdater;
    }

    public static void setOfflineClientsUpdater(Consumer<Integer> offlineClientsUpdater) {
        ClientHandler.offlineClientsUpdater = offlineClientsUpdater;
    }

    private void sendConnectedSuccessfully() throws IOException {
        streamHandler.write("connected;;true");
    }

    public ResponseReceiver getInGame(ResponseReceiver responseReceiver) throws IOException {
        ResponseReceiver oldResponseReceiver = this.responseReceiver;
        inGameClients.add(this);
        onlineClients.remove(this);
        this.responseReceiver = responseReceiver;
        updateClientsUpdater();
        return oldResponseReceiver;
    }

    public void getOutOfGame() {
        this.responseReceiver = requestHandlerSwitcher.newDataRequestHandler();
        onlineClients.add(this);
        inGameClients.remove(this);
        updateClientsUpdater();
    }

    public void loggedIn(String name) {
        this.name = name;
        responseReceiver = requestHandlerSwitcher.newDataRequestHandler();
        guestClients.remove(this);
        onlineClients.add(this);
        updateClientsUpdater();
        names.add(name);
    }

    public void loggedOut() {
        names.remove(name);
        this.name = Thread.currentThread().getName();
        responseReceiver = requestHandlerSwitcher.newAuthRequestHandler();
        onlineClients.remove(this);
        guestClients.add(this);
        updateClientsUpdater();
    }

    public static Vector<ClientHandler> getOnlineClientHandlers() {
        return onlineClients;
    }

    public static Vector<ClientHandler> getGuestClientHandlers() {
        return guestClients;
    }

    public static Vector<ClientHandler> getInGameClientsHandlers() {
        return inGameClients;
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

    public static ClientHandler getOnlineClientHandlerByName(String name) {
        List<ClientHandler> onlineClients = ClientHandler.onlineClients;
        for (ClientHandler clientHandler : onlineClients) {
            if (clientHandler.getName().equals(name)) {
                return clientHandler;
            }
        }
        return null;
    }

    public static ClientHandler getGuestClientHandlerByName(String name) {
        for (ClientHandler clientHandler : guestClients) {
            if (clientHandler.getName().equals(name)) {
                return clientHandler;
            }
        }
        return null;
    }

    public void request(String request) throws IOException {
        streamHandler.write(request);
    }

    public static ClientHandler getInGameClientHandlerByName(String name) {
        for (ClientHandler clientHandler : inGameClients) {
            if (clientHandler.getName().equals(name)) {
                return clientHandler;
            }
        }
        return null;
    }

    void close() throws IOException {
        names.remove(name);
        socket.close();
        streamHandler.close();
    }

    public static void closeAll() throws IOException {
        for (ClientHandler clientHandler : onlineClients) {
            clientHandler.close();
        }

        for (ClientHandler clientHandler : guestClients) {
            clientHandler.close();
        }

        for (ClientHandler clientHandler : inGameClients) {
            clientHandler.close();
        }
        names.clear();
        onlineClients.clear();
        guestClients.clear();
        inGameClients.clear();
        updateClientsUpdater();
    }

    public static void updateClientsUpdater() {
        int playerCount = 0;
        try {
            playerCount = DataAccessLayer.getPlayersCount();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        guestClientsUpdater.accept(guestClients.size());
        onlineClientsUpdater.accept(onlineClients.size());
        inGameClientsUpdater.accept(inGameClients.size());
        offlineClientsUpdater.accept(playerCount - (onlineClients.size() + inGameClients.size()));
    }

    public static boolean isClientLoggedIn(String name) {
        for (String loggedInName : names) {
            if (name.equals(loggedInName)) {
                return true;
            }
        }
        return false;
    }

    private synchronized void removeClient() {
        onlineClients.remove(this);
        guestClients.remove(this);
        inGameClients.remove(this);
    }

    class StreamHandler extends Thread {

        private final BufferedReader bufferedReader;
        private final PrintWriter printWriter;
        private boolean isRunning = true;

        public StreamHandler() throws IOException {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    String response = bufferedReader.readLine();
                    write(responseReceiver.sendData(name, response));
                } catch (IOException ex) {
                    errorMessageSender.sendMessage(ex.getMessage());
                    ex.printStackTrace();
                } catch (NullPointerException ex1) {
                    ex1.printStackTrace();
                    errorMessageSender.sendMessage(ex1.getMessage());
                    try {
                        if (isRunning) {
                            ClientHandler.this.close();
                            removeClient();
                            updateClientsUpdater();
                        }
                    } catch (IOException ex) {
                        errorMessageSender.sendMessage(ex.getMessage());
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        void write(String response) throws IOException {
            printWriter.println(response);
        }

        void close() throws IOException {
            isRunning = false;
            printWriter.close();
            bufferedReader.close();
        }
    }
}
