/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marina
 */
public class ServerHandler extends Thread{//implements Message{
   private boolean isRunning;
   private final ServerSocket serverSocket ;
   private final ErrorMessageSender errorMessageSender;
   
    ServerHandler (int port,ErrorMessageSender errorMessageSender) throws IOException{
        serverSocket=new ServerSocket(port);
        this.errorMessageSender=errorMessageSender;
    }
 

    @Override
    public void run() {
        while (isRunning) {
            try {
                new ClientHandler(serverSocket.accept());
               
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
   
   
   
    private void close() throws IOException{
        isRunning=false;
        serverSocket.close();
        
       }
   

    
}
