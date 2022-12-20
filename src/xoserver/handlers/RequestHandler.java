/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xoserver.handlers;

/**
 *
 * @author fathy
 */
public class RequestHandler {

    protected String createResponse(String... args) {
        String response = "";
        int index;
        for (index = 0; index < args.length - 1; index++) {
            response += args[index] + RequestType.MESSAGE_SPLITER;
        }
        response += args[index];
        return response;
    }

    protected String createResponse(boolean success, String type, String successMessage, String failMessage) {
        String message = failMessage;
        if (success) {
            message = successMessage;
        }
        return createResponse(type, success + "", message);

    }

    protected String createInfoResponse(boolean success) {
        String message = "failed to sent";
        if (success) {
            message = "sent successfully";

        }
        return createResponse(RequestType.INFO, success + "", message);
    }

}
