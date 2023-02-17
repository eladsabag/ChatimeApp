package com.elad.chatimeapp.screens.main.tabs_fragments.settings;

import static com.elad.chatimeapp.utils.Constants.NOTIFICATION_ON;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentSettingsBinding;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.screens.SharedViewModel;
import com.elad.chatimeapp.screens.main.MainViewModel;
import com.elad.chatimeapp.utils.SharedPrefsUtil;

public class SettingsFragment extends Fragment {
    private MainViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentSettingsBinding binding;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.setModel(viewModel.getUser());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initViews();
    }

    private void initViews() {
        binding.settingsSwitchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> SharedPrefsUtil.getInstance().putBooleanToSP(NOTIFICATION_ON, isChecked));
        binding.settingsBtnLogout.setOnClickListener(v -> onLogoutClicked());
    }

    private void onLogoutClicked() {
        sharedViewModel.resetUser();
        viewModel.logout(navController);
    }
}