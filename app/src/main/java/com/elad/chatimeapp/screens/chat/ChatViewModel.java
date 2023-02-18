package com.elad.chatimeapp.screens.chat;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elad.chatimeapp.BuildConfig;
import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.Message;
import com.elad.chatimeapp.utils.HashUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author - Elad Sabag
 * @date - 2/13/2023
 */
@HiltViewModel
public class ChatViewModel extends ViewModel {
    private static final String TAG = "ConversationViewModel";
    private final FirebaseAuth mAuth;
    private final DatabaseRepository repository;
    private final MutableLiveData<Chat> chatLiveData;
    private Chat currentChat;

    @Inject
    public ChatViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
        this.chatLiveData = new MutableLiveData<>();
        this.currentChat = new Chat();
    }

    public void listenToSingleChatMessagesUpdates(String chatId) {
        repository.listenToSingleChatMessagesUpdates(chatId, new DatabaseRepository.OnChatDataChangedListener() {
            @Override
            public void onChatAdded(Chat chat) {}
            @Override
            public void onMessageAdded(ArrayList<Message> messages) {
                if (messages != null) {
                    currentChat.setMessages(messages);
                    chatLiveData.postValue(currentChat);
                }
            }
            @Override
            public void onChatChanged(Chat chat) {}
            @Override
            public void onChatRemoved(Chat chat) {}
            @Override
            public void onError(Exception e) { Log.e(TAG, e.getMessage()); }
        });
    }

    public void sendMessage(String chatId, Message message) {
        try {
            Message newMessage = new Message(message.getSenderUid(), HashUtil.encrypt(message.getMessage(), BuildConfig.SECRET_KEY));
            currentChat.addMessage(newMessage);
            repository.addMessagesToChat(chatId, currentChat.getMessages());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<Chat> getChatLiveData() {
        return chatLiveData;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public String getCurrentUserUid() {
        return mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
    }
}
