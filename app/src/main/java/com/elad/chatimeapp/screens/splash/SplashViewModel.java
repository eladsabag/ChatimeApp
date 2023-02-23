package com.elad.chatimeapp.screens.splash;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.utils.Constants;
import com.elad.chatimeapp.utils.SharedPrefsUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private final DatabaseRepository repository;

    @Inject
    public SplashViewModel(FirebaseAuth mAuth, DatabaseRepository repository) {
        this.mAuth = mAuth;
        this.repository = repository;
    }

    public void handleNavigation(NavController navController) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mAuth.getCurrentUser() != null) {
                getUserAndNavigate(mAuth.getCurrentUser(), navController);
            } else {
                navController.navigate(R.id.action_splash_dest_to_otp_send_fragment);
            }
        }, 2000L);
    }

    private void getUserAndNavigate(FirebaseUser currentUser, NavController navController) {
        repository.getUser(currentUser.getUid(), new DatabaseRepository.OnUserDataChangedListener() {
            @Override
            public void onUserDataChanged(User user) {
                SharedPrefsUtil.getInstance().putObject(Constants.USER, user);
                SharedPrefsUtil.getInstance().putBooleanToSP(Constants.FIRST_LOGIN, user.isFirstLogin());

                if (user.isFirstLogin()) {
                    navController.navigate(R.id.action_splash_dest_to_profile_dest);
                } else {
                    navController.navigate(R.id.action_splash_dest_to_main_fragment);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }
}
