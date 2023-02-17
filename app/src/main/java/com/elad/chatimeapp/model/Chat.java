package com.elad.chatimeapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author - Elad Sabag
 * @date - 1/27/2023
 */
public class Chat implements Serializable {
    private String chatId;
    private String uid1;
    private String uid2;
    private String userName1;
    private String userName2;
    private String date;
    private ArrayList<Message> messages;

    public Chat() {}

    public Chat(String chatId, String uid1, String uid2, String userName1, String userName2, String date, ArrayList<Message> messages) {
        this.chatId = chatId;
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.userName1 = userName1;
        this.userName2 = userName2;
        this.date = date;
        this.messages = messages;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
