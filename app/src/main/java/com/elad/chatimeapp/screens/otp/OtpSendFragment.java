package com.elad.chatimeapp.screens.otp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentOtpSendBinding;
import com.elad.chatimeapp.dialogs.ErrorDialogFragment;
import com.elad.chatimeapp.dialogs.LoadingDialogFragment;
import com.elad.chatimeapp.dialogs.SendPhoneDialogFragment;
import com.google.firebase.auth.PhoneAuthCredential;

public class OtpSendFragment extends Fragment {
    private OtpViewModel viewModel;
    private FragmentOtpSendBinding binding;
    private LoadingDialogFragment loadingDialogFragment;
    private NavController navController;
    private final OtpViewModel.OnResultCallback onResultCallback = new OtpViewModel.OnResultCallback() {
        @Override
        public void onCodeSent() {
            loadingDialogFragment.dismiss();
            navController.navigate(R.id.action_otp_send_fragment_to_otp_auth_fragment);
        }

        @Override
        public void onSignIn() {}

        @Override
        public void onFailure(String errorMessage) {
            binding.sendEditPhone.setText("");
            loadingDialogFragment.dismiss();
            new ErrorDialogFragment(errorMessage).show(getChildFragmentManager(), ErrorDialogFragment.TAG);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OtpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOtpSendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loadingDialogFragment = new LoadingDialogFragment();
        viewModel.setOnResultCallback(onResultCallback);
        initViews();
    }

    private void initViews() {
        binding.setText(getString(R.string.agree_terms_and_policy));
        binding.setSubtext(getResources().getStringArray(R.array.agree_terms_and_policy_subtext));
        binding.setIsOrientationPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        binding.sendBtnOk.setOnClickListener(v -> onOkClicked());
        binding.sendCbxAgree.setOnCheckedChangeListener((buttonView, isChecked) -> binding.sendBtnOk.setEnabled(isChecked));
    }

    private void onOkClicked() {
        String countryCode = binding.sendSpnCcp.getSelectedCountryCodeWithPlus();
        String phoneNumber = binding.sendEditPhone.getText().toString();
        String fullPhoneNumber = String.format("%s%s",countryCode, phoneNumber);
        new SendPhoneDialogFragment(fullPhoneNumber, () -> {
            loadingDialogFragment.show(getChildFragmentManager(), LoadingDialogFragment.TAG);
            viewModel.sendCode(fullPhoneNumber, requireActivity());
        }).show(getChildFragmentManager(), SendPhoneDialogFragment.TAG);
    }
}