package com.elad.chatimeapp.screens;

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
public class SplashViewModel extends ViewModel {
    private final static String TAG = "SplashViewModel";
    private final FirebaseAuth mAuth;

    @Inject
    public SplashViewModel(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void handleNavigation(NavController navController) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mAuth.getCurrentUser() != null) {
                navController.navigate(R.id.action_splash_dest_to_main_fragment);
            } else {
                navController.navigate(R.id.action_splash_dest_to_otp_send_fragment);
            }
        }, 2000L);
    }
}