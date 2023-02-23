package com.elad.chatimeapp.model;

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
    private String phoneNumber;
    private boolean firstLogin = true;
    private HashMap<String, Boolean> chats = new HashMap<>();

    public User() {}

    public User(String name, String profileImage, String status, String gender, String phoneNumber, boolean firstLogin, HashMap<String, Boolean> chats) {
        this.name = name;
        this.profileImage = profileImage;
        this.status = status;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.firstLogin = firstLogin;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    @Bindable
    public HashMap<String, Boolean> getChats() {
        return chats;
    }

    public void setChats(HashMap<String, Boolean> chats) {
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
}
