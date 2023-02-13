package com.elad.chatimeapp.model;

/**
 * @author - Elad Sabag
 * @date - 2/13/2023
 */
public class Message {
    private String message;
    private boolean isSentByMe;

    public Message() {}

    public Message(String message, boolean isSentByMe) {
        this.message = message;
        this.isSentByMe = isSentByMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        isSentByMe = sentByMe;
    }
}
