package com.elad.chatimeapp.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

/**
 * @author - Elad Sabag
 * @date - 1/24/2023
 */
public class DatabaseRepository {
    private static final String TAG = "DatabaseRepository";
    private final FirebaseDatabase mDatabase;

    @Inject
    public DatabaseRepository(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public void addUser(User user, String uid) {
        mDatabase.getReference("users").child(uid).setValue(user);
    }

    public void deleteUser(String uid) {
        mDatabase.getReference("users").child(uid).removeValue();
    }

    public void addChat(Chat chat, String uid) {
        mDatabase.getReference("chats").child(uid).push().setValue(chat);
    }

    public void deleteChat(String chatId, String uid) {
        mDatabase.getReference("chats").child(uid).child(chatId).removeValue();
    }

    public void getUser(final OnUserDataChangedListener listener, String uid) {
        mDatabase.getReference("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void listenForUserUpdates(final OnUserDataChangedListener listener, String uid) {
        mDatabase.getReference("users").child(uid)
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

    public void listenForChatUpdates(final OnChatDataChangedListener listener, String uid) {
        mDatabase.getReference("chats").child(uid)
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

        void onChatChanged(Chat chat);

        void onChatRemoved(Chat chat);

        void onError(Exception e);
    }
}
