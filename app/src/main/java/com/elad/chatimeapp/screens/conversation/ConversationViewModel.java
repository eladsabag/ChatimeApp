package com.elad.chatimeapp.screens.conversation;

import androidx.lifecycle.ViewModel;

import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author - Elad Sabag
 * @date - 2/13/2023
 */
@HiltViewModel
public class ConversationViewModel extends ViewModel {
    private static final String TAG = "ConversationViewModel";
    private final FirebaseAuth mAuth;
    private final DatabaseRepository repository;

    @Inject
    public ConversationViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
    }
}
