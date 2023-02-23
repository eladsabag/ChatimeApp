package com.elad.chatimeapp.screens.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.Message;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.utils.Constants;
import com.elad.chatimeapp.utils.SharedPrefsUtil;
import com.google.firebase.auth.FirebaseAuth;

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
    private final User currentUser;
    private final FirebaseAuth mAuth;
    private final DatabaseRepository repository;
    private final MutableLiveData<ArrayList<Chat>> chatsLiveData;
    private final MutableLiveData<Chat> chatLiveData;

    @Inject
    public MainViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
        this.currentUser = SharedPrefsUtil.getInstance().getObject(Constants.USER, User.class);
        this.chatsLiveData = new MutableLiveData<>();
        this.chatLiveData = new MutableLiveData<>();
    }

    public void listenToUserUpdates() {
        repository.listenForUserUpdates(getCurrentUserUid(), new DatabaseRepository.OnUserDataChangedListener() {
            @Override
            public void onUserDataChanged(User user) {
                currentUser.updateUser(user);
                getChats();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void getContactUserIfExistAndAddChat(Chat chat, String phoneNumber) {
        repository.getUserIdByPhoneNumber(phoneNumber, new DatabaseRepository.OnUserRetrievedListener() {
            @Override
            public void onUserIdRetrieved(String uid) {
                Log.i(TAG, "onUserIdRetrieved");
                chat.setUid2(uid);
                getUserChatProfileImage(uid, chat);
            }
            @Override
            public void onUserIdRetrievalFailed(String errorMessage) {
                Log.e(TAG, "onUserIdRetrievalFailed: " + errorMessage);
                addChat(chat);
            }
            @Override
            public void onUserProfileImageRetrieved(String profileImage) {}
            @Override
            public void onUserProfileImageRetrievalFailed(String errorMessage) {}
        });
    }

    private void getUserChatProfileImage(String uid, Chat chat) {
        repository.getUserProfileImageById(uid, new DatabaseRepository.OnUserRetrievedListener() {
            @Override
            public void onUserIdRetrieved(String uid) {}
            @Override
            public void onUserIdRetrievalFailed(String errorMessage) {}
            @Override
            public void onUserProfileImageRetrieved(String profileImage) {
                Log.i(TAG, "onUserProfileImageRetrieved");
                chat.setProfileImage2(profileImage);
                addChat(chat);
            }
            @Override
            public void onUserProfileImageRetrievalFailed(String errorMessage) {
                Log.e(TAG, "onUserProfileImageRetrievalFailed" + errorMessage);
                addChat(chat);
            }
        });
    }

    private void addChat(Chat chat) {
        repository.addChat(
                chat.getUid1(),
                chat.getUid2(),
                chat.getChatId(),
                chat,
                new DatabaseRepository.OnChatDataChangedListener() {
                    @Override
                    public void onChatAdded(Chat chat) {
                        Log.i(TAG, "onChatAdded");

                        HashMap<String, Boolean> chatsHashMap = currentUser.getChats();
                        chatsHashMap.put(chat.getChatId(), chat.getUid2() != null && !chat.getUid2().isEmpty());
                        currentUser.setChats(chatsHashMap);

                        chatLiveData.postValue(chat);
                    }
                    @Override
                    public void onMessageAdded(ArrayList<Message> messages) { Log.i(TAG, "onMessageAdded"); }
                    @Override
                    public void onChatChanged(Chat chat) { Log.i(TAG, "onChatChanged"); }
                    @Override
                    public void onChatRemoved(Chat chat) { Log.i(TAG, "onChatRemoved"); }
                    @Override
                    public void onError(Exception e) { Log.e(TAG, "onError"); }
                }
        );
    }

    public void getChats() {
        repository.getChatsByChatIds(currentUser.getChats(), new DatabaseRepository.OnChatsRetrievedListener() {
            @Override
            public void onChatsRetrieved(ArrayList<Chat> chats) {
                chatsLiveData.postValue(chats != null ? chats : new ArrayList<>());
            }

            @Override
            public void onChatRetrievalFailed(String errorMessage) {
                chatsLiveData.postValue(new ArrayList<>());
                Log.d(TAG, errorMessage);
            }
        });
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public String getCurrentUserUid() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
    }

    public MutableLiveData<ArrayList<Chat>> getChatsLiveData() {
        return chatsLiveData;
    }

    public MutableLiveData<Chat> getChatLiveData() {
        return chatLiveData;
    }

    public void dismissChat() {
        chatLiveData.postValue(null);
    }

    public void logout(NavController navController) {
        mAuth.signOut();
        navController.navigate(R.id.action_main_fragment_to_splash_dest);
    }
}
