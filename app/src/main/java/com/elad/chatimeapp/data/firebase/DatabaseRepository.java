package com.elad.chatimeapp.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.Message;
import com.elad.chatimeapp.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author - Elad Sabag
 * @date - 1/24/2023
 */
public class DatabaseRepository {
    private static final String TAG = "DatabaseRepository";
    private static final String USERS_NODE = "users";
    private static final String CHATS_NODE = "chats";
    private final FirebaseDatabase mDatabase;

    @Inject
    public DatabaseRepository(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public void addUser(User user, String uid) {
        mDatabase.getReference(USERS_NODE).child(uid).setValue(user);
    }

    public void deleteUser(String uid) {
        mDatabase.getReference(USERS_NODE).child(uid).removeValue();
    }

    public void addChat(String uid1, String uid2, String chatId, Chat chat, OnChatDataChangedListener listener) {
        mDatabase.getReference(CHATS_NODE).child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "Chat ID already taken");
                    listener.onError(null);
                } else {
                    // The chat ID is available, create a new chat node in the "Chats" node with the chat ID as the key
                    mDatabase.getReference(CHATS_NODE).child(chatId).setValue(chat);
                    // Save the chat ID in the "Users" node for each user
                    Map<String, Object> map = new HashMap<>();
                    map.put(chatId, true);

                    mDatabase.getReference(USERS_NODE).child(uid1).child(CHATS_NODE).updateChildren(map);
                    if (!uid2.isEmpty()) // add only if second user exist
                        mDatabase.getReference(USERS_NODE).child(uid2).child(CHATS_NODE).updateChildren(map);

                    listener.onChatAdded(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    public void deleteChat(String chatId) {
        mDatabase.getReference(CHATS_NODE).child(chatId).removeValue();
    }

    public void addMessagesToChat(String chatId, ArrayList<Message> messages) {
        mDatabase.getReference(CHATS_NODE).child(chatId).child("messages").setValue(messages);
    }



    public void getUserIdByPhoneNumber(String phoneNumber, OnUserRetrievedListener listener) {
        mDatabase.getReference().child(USERS_NODE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Loop through the snapshot to find the user with the matching phone number
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String userPhoneNumber = childSnapshot.child("phoneNumber").getValue(String.class);
                        Log.d(TAG, userPhoneNumber + " " + phoneNumber);
                        if (userPhoneNumber != null && userPhoneNumber.equals(phoneNumber)) {
                            String uid = childSnapshot.getKey();
                            listener.onUserIdRetrieved(uid);
                            return;
                        }
                    }
                }
                // Phone number not found
                listener.onUserIdRetrievalFailed("Error retrieving user ID: phone number not found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onUserIdRetrievalFailed("Error retrieving user ID: " + error.getMessage());
            }
        });
    }

    public void getUserProfileImageById(String uid, OnUserRetrievedListener listener) {
        mDatabase.getReference().child(USERS_NODE).child(uid).child("profileImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileImage = snapshot.getValue(String.class);
                if (listener != null)
                    listener.onUserProfileImageRetrieved(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onUserProfileImageRetrievalFailed("Error retrieving user profile image: " + error.getMessage());
            }
        });
    }

    public void getUser(String uid, final OnUserDataChangedListener listener) {
        mDatabase.getReference(USERS_NODE).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                // Do something with the retrieved user object
                if (listener != null)
                    listener.onUserDataChanged(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
                if (listener != null)
                    listener.onError(databaseError.toException());
            }
        });
    }

    public void getChatsByChatIds(Map<String, Boolean> chatIds, OnChatsRetrievedListener listener) {
        ArrayList<Chat> chats = new ArrayList<>();

        for (String chatId : chatIds.keySet()) {
            mDatabase.getReference().child(CHATS_NODE).child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        chats.add(chat);
                    }
                    if (chats.size() == chatIds.size()) {
                        listener.onChatsRetrieved(chats);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onChatRetrievalFailed("Error retrieving chat: " + error.getMessage());
                }
            });
        }
    }


    public void listenForUserUpdates(final OnUserDataChangedListener listener, String uid) {
        mDatabase.getReference(USERS_NODE).child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (listener != null)
                            listener.onUserDataChanged(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (listener != null)
                            listener.onError(databaseError.toException());
                    }
                });
    }

    public void listenToSingleChatMessagesUpdates(String chatId, final OnChatDataChangedListener listener) {
        mDatabase.getReference(CHATS_NODE).child(chatId).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> arrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message value = snapshot.getValue(Message.class);
                    arrayList.add(value);
                }
                if (listener != null)
                    listener.onMessageAdded(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null)
                    listener.onError(databaseError.toException());
            }
        });
    }

    public void listenForChatUpdates(String chatId, final OnChatDataChangedListener listener) {
        mDatabase.getReference(CHATS_NODE).child(chatId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        if (listener != null)
                            listener.onChatAdded(chat);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        if (listener != null)
                            listener.onChatChanged(chat);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        if (listener != null)
                            listener.onChatRemoved(chat);
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        // Not needed for this example
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (listener != null)
                            listener.onError(databaseError.toException());
                    }
                });
    }

    public interface OnUserDataChangedListener {
        void onUserDataChanged(User user);

        void onError(Exception e);
    }

    public interface OnChatDataChangedListener {
        void onChatAdded(Chat chat);
        void onMessageAdded(ArrayList<Message> messages);
        void onChatChanged(Chat chat);
        void onChatRemoved(Chat chat);
        void onError(Exception e);
    }
    public interface OnUserRetrievedListener {
        void onUserIdRetrieved(String uid);
        void onUserProfileImageRetrieved(String profileImage);
        void onUserIdRetrievalFailed(String errorMessage);
        void onUserProfileImageRetrievalFailed(String errorMessage);
    }

    public interface OnChatsRetrievedListener {
        void onChatsRetrieved(ArrayList<Chat> chats);
        void onChatRetrievalFailed(String errorMessage);
    }
}
