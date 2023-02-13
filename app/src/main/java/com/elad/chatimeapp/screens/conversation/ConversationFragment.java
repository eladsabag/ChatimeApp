package com.elad.chatimeapp.screens.conversation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elad.chatimeapp.databinding.FragmentConversationBinding;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.Message;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ConversationFragment extends Fragment {
    private FragmentConversationBinding binding;
    private ConversationViewModel viewModel;
    private ConversationAdapter adapter;
    private ArrayList<Message> messageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ConversationViewModel.class);
        readArgumentsAndSetData();
    }

    private void readArgumentsAndSetData() {
        messageList = new ArrayList<>();
        if (getArguments() != null) {
            String json = getArguments().getString("chat", "");
            if (json != null && !json.isEmpty()) {
                Chat chat = new Gson().fromJson(json, Chat.class);
                messageList = chat.getMessages();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConversationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ConversationAdapter(messageList);
        initViews();
    }

    private void initViews() {
        binding.conversation.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.conversation.setAdapter(adapter);
    }
}