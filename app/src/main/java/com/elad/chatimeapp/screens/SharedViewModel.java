package com.elad.chatimeapp.screens;

import static com.elad.chatimeapp.utils.Constants.USER_EXTRA;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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
 * @date - 1/21/2023
 */
@HiltViewModel
public class SharedViewModel extends ViewModel {
    private final static String TAG = "SplashViewModel";
    private final FirebaseAuth mAuth;
    private final DatabaseRepository repository;
    private User currentUser;

    @Inject
    public SharedViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
        this.currentUser = new User();
    }

    public void handleNavigation(NavController navController) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    getUserAndNavigate(navController, mAuth.getCurrentUser().getUid());
                } else {
                    navController.navigate(R.id.action_splash_dest_to_otp_send_fragment);
                }
            }
        }, 2000L);
    }

    private void getUserAndNavigate(NavController navController, String uid) {
        repository.getUser(uid, new DatabaseRepository.OnUserDataChangedListener() {
            @Override
            public void onUserDataChanged(User user) {
                currentUser = user != null ? user : currentUser;
                navController.navigate(R.id.action_splash_dest_to_main_fragment);
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, e.getMessage());
                navController.navigate(R.id.action_splash_dest_to_otp_send_fragment);
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void resetUser() {
        this.currentUser = new User();
    }
}
