package com.elad.chatimeapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author - Elad Sabag
 * @date - 1/27/2023
 */
public class Chat implements Serializable {
    private String id;
    private String name;
    private String date;
    private String profileImage;
    private ArrayList<Message> messages;

    public Chat() {}

    public Chat(String id, String name, String date, String profileImage, ArrayList<Message> messages) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.profileImage = profileImage;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
