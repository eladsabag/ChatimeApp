package com.elad.chatimeapp.screens.chat;

import static com.elad.chatimeapp.utils.Constants.CHAT_ID_EXTRA;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elad.chatimeapp.databinding.FragmentChatBinding;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.Message;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private ChatViewModel viewModel;
    private String chatId = "";
    private NavController navController;
    private ChatAdapter adapter;
    private ArrayList<Message> messageList;
    private final Observer<Chat> chatObserver = new Observer<>() {
        @Override
        public void onChanged(Chat chat) {
            if (chat.getMessages() != null) {
                if (viewModel.getmAuth().getCurrentUser() != null) {
                    messageList = chat.getMessages();
                    adapter.updateList(messageList);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        messageList = new ArrayList<>();
        readFromArgumentsAndSetData();
    }

    private void readFromArgumentsAndSetData() {
        if (getArguments() != null) {
            chatId = getArguments().getString(CHAT_ID_EXTRA, "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel.listenToSingleChatMessagesUpdates(chatId);
        viewModel.getChatLiveData().observe(getViewLifecycleOwner(), chatObserver);
        initViews();
    }

    private void initViews() {
        adapter = new ChatAdapter(messageList, viewModel.getCurrentUserUid());
        binding.chat.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.chat.setAdapter(adapter);
        binding.chatImgSend.setOnClickListener(v -> onSendClicked());
        binding.chatEdt.addTextChangedListener(textWatcher);
        binding.chatImgSend.setEnabled(false);
    }

    private void onSendClicked() {
        Editable message = binding.chatEdt.getText();
        if (message != null && !message.toString().isEmpty() && !message.toString().isBlank()) {
            viewModel.sendMessage(chatId, new Message(viewModel.getCurrentUserUid(), message.toString()));
            binding.chatEdt.setText("");
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            Editable message = binding.chatEdt.getText();
            boolean enabled = message != null && !message.toString().isEmpty() && !message.toString().isBlank();
            binding.chatImgSend.setEnabled(enabled);
            binding.chatImgSend.setAlpha(enabled ? 1f : 0.5f);
        }
    };
}