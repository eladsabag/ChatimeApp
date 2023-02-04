package com.elad.chatimeapp.screens.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

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
    private final MutableLiveData<User> userMutableLiveData;
    private final DatabaseRepository.OnUserDataChangedListener onUserDataChangedListener = new DatabaseRepository.OnUserDataChangedListener() {
        @Override
        public void onUserDataChanged(User user) { userMutableLiveData.postValue(user); }

        @Override
        public void onError(Exception e) { Log.d(TAG, e.getMessage()); }
    };


    @Inject
    public MainViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
        this.userMutableLiveData = new MutableLiveData<>();
        this.user = new User();
        if (mAuth.getCurrentUser() != null) {
            repository.getUser(onUserDataChangedListener, mAuth.getUid());
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

    public void logout(NavController navController) {
        mAuth.signOut();
        navController.navigate(R.id.action_main_fragment_to_splash_dest);
    }
}
