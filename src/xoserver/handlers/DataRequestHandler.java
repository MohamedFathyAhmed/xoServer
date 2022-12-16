/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

/**
 *
 * @author mohamed
 */
public class DataRequestHandler {

    private static DataRequestHandler instance;

    public DataRequestHandler() {
    }


    public ResponseReceiver newResponseReceiver() {
        return this::handle;
    }

    private String handle(String request) {
        switch (request) {
            
            
            
        }
        return "";
    }
}
