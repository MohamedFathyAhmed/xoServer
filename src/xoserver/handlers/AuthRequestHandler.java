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
public class AuthRequestHandler {

    private static AuthRequestHandler instance;

    public AuthRequestHandler() {

    }

    public ResponseReceiver newResponseReceiver() {
        return this::handle;
    }

    public synchronized String handle(String request) {
        switch (request) {
            //handle me

        }
        return "";
    }
}
