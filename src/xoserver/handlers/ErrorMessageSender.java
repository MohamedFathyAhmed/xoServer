/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

/**
 *
 * @author Marina
 */
@FunctionalInterface
public interface ErrorMessageSender {

    void sendMessage(String message);
}
