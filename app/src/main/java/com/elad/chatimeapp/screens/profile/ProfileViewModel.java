package com.elad.chatimeapp.screens.profile;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author - Elad Sabag
 * @date - 2/3/2023
 */
@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";
    private final FirebaseAuth mAuth;
    private final DatabaseRepository repository;
    private User user;
    private final MutableLiveData<User> userMutableLiveData;
    private final DatabaseRepository.OnUserDataChangedListener onUserDataChangedListener = new DatabaseRepository.OnUserDataChangedListener() {
        @Override
        public void onUserDataChanged(User user) { userMutableLiveData.postValue(user); }

        @Override
        public void onError(Exception e) { Log.d(TAG, e.getMessage()); }
    };

    @Inject
    public ProfileViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
        this.userMutableLiveData = new MutableLiveData<>();
        this.user = new User();
        if (mAuth.getCurrentUser() != null) {
            repository.getUser(onUserDataChangedListener, mAuth.getUid());
        }
    }

    public void saveUser() {
        if (mAuth.getCurrentUser() != null) {
            repository.addUser(user, mAuth.getCurrentUser().getUid());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void updateUser(User user) {
        this.user.updateUser(user);
    }
}
