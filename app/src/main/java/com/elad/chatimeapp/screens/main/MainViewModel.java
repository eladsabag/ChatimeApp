package com.elad.chatimeapp.screens.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.utils.ChatIdGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author - Elad Sabag
 * @date - 1/27/2023
 */
@HiltViewModel
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private User user;
    private final FirebaseAuth mAuth;
    private final DatabaseRepository repository;

    @Inject
    public MainViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
        this.user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void logout(NavController navController) {
        mAuth.signOut();
        navController.navigate(R.id.action_main_fragment_to_splash_dest);
    }

    public void addChatToUser(Chat chat, String uid) {
        HashMap<String, Chat> chatsHashMap = user.getChats();
        chatsHashMap.put(chat.getId(), chat);
        user.setChats(chatsHashMap);

        repository.addChat(chat, uid);
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public boolean deleteChat(Chat chat) {
        if (mAuth.getCurrentUser() != null) {
            repository.deleteChat(chat.getId(), mAuth.getCurrentUser().getUid());
            return true;
        }
        return false;
    }
}
