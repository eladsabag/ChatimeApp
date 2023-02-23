package com.elad.chatimeapp.screens.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentMainBinding;
import com.elad.chatimeapp.screens.main.tabs_fragments.conversations.ConversationsFragment;
import com.elad.chatimeapp.screens.main.tabs_fragments.settings.SettingsFragment;
import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment {
    private MainViewModel viewModel;
    private FragmentMainBinding binding;
    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    setFirstSelectionTab();
                    break;
                case 1:
                    setSecondSelectionTab();
                    break;
                default:break;
            }
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.listenToUserUpdates();
        initViews();
    }

    private void initViews() {
        setInitialFragment();
        binding.mainLayTabs.addOnTabSelectedListener(onTabSelectedListener);
    }

    private void setInitialFragment() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_tabs_container, new ConversationsFragment());
        fragmentTransaction.commit();
    }

    private void setFirstSelectionTab() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_tabs_container, new ConversationsFragment());
        fragmentTransaction.commit();
    }

    private void setSecondSelectionTab() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_tabs_container, new SettingsFragment());
        fragmentTransaction.commit();
    }
}