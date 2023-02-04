package com.elad.chatimeapp.screens.otp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.elad.chatimeapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author - Elad Sabag
 * @date - 1/23/2023
 */
@HiltViewModel
public class OtpViewModel extends ViewModel {
    private static final String TAG = "OtpViewModel";
    private final FirebaseAuth mAuth;
    private String phoneNumber;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private OnResultCallback onResultCallback;

    @Inject
    public OtpViewModel(FirebaseAuth mAuth, Context appContext)  {
        this.mAuth = mAuth;
        this.mCallbacks = initCallbacks(appContext);
    }

    public void setOnResultCallback(OnResultCallback onResultCallback) { this.onResultCallback = onResultCallback; }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks initCallbacks(Context appContext) {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                String errorMessage;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorMessage = appContext.getString(R.string.invalid_request);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    errorMessage = appContext.getString(R.string.too_many_requests);
                } else {
                    errorMessage = appContext.getString(R.string.general_request_error);
                }

                if (onResultCallback != null)
                    onResultCallback.onFailure(errorMessage);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                if (onResultCallback != null)
                    onResultCallback.onCodeSent();
            }
        };
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Activity activity) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();

                        if (onResultCallback != null)
                            onResultCallback.onSignIn();
                        // Update UI
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        String errorMessage;
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            errorMessage = activity.getString(R.string.code_invalid);
                        } else if (task.getException() instanceof FirebaseNetworkException) {
                            errorMessage = activity.getString(R.string.firebase_network_error);
                        } else {
                            errorMessage = activity.getString(R.string.general_code_error);
                        }

                        if (onResultCallback != null)
                            onResultCallback.onFailure(errorMessage);
                    }
                });
    }

    public void sendCode(String phoneNumber, Activity activity) {
        this.phoneNumber = phoneNumber;

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void resendCode(Activity activity) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(mResendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public PhoneAuthCredential buildCredential(String code) {
        return PhoneAuthProvider.getCredential(mVerificationId, code);
    }

    public interface OnResultCallback {
        void onCodeSent();
        void onSignIn();
        void onFailure(String errorMessage);
    }
}
