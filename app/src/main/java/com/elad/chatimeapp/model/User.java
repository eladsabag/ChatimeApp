package com.elad.chatimeapp.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.elad.chatimeapp.BR;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author - Elad Sabag
 * @date - 1/27/2023
 */
public class User extends BaseObservable implements Serializable {
    private String name;
    private String profileImage; // base 64 encoded image string
    private String status;
    private String gender;
    private HashMap<String, Chat> chats = new HashMap<>();

    public User() {}

    public User(String name, String profileImage, String status, String gender, HashMap<String, Chat> chats) {
        this.name = name;
        this.profileImage = profileImage;
        this.status = status;
        this.gender = gender;
        this.chats = chats;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        notifyPropertyChanged(BR.profileImage);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public HashMap<String, Chat> getChats() {
        return chats;
    }

    public void setChats(HashMap<String, Chat> chats) {
        this.chats = chats;
        notifyPropertyChanged(BR.chats);
    }

    public void updateUser(User user) {
        setGender(user.getGender());
        setName(user.getName());
        setProfileImage(user.getProfileImage());
        setStatus(user.getStatus());
        setChats(user.getChats());
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                ", chats=" + chats.toString() +
                '}';
    }
}
