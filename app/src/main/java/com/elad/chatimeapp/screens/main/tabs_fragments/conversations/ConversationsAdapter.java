package com.elad.chatimeapp.screens.main.tabs_fragments.conversations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elad.chatimeapp.databinding.ChatListItemBinding;
import com.elad.chatimeapp.model.Chat;

import java.util.ArrayList;

/**
 * @author - Elad Sabag
 * @date - 2/10/2023
 */
public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.MyViewHolder> {
    private ArrayList<Chat> chatsList;
    private final String currentUserUid;
    private final CallbackChat callbackChat;

    public ConversationsAdapter(ArrayList<Chat> chatsList, String currentUserUid, CallbackChat callbackChat) {
        this.chatsList = chatsList;
        this.currentUserUid = currentUserUid;
        this.callbackChat = callbackChat;
    }

    public interface CallbackChat {
        void OnChatClicked(Chat chat);
    }

    @NonNull
    @Override
    public ConversationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatListItemBinding binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsAdapter.MyViewHolder holder, int position) {
        holder.bind(chatsList.get(position), currentUserUid, callbackChat);
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public void addChat(Chat chat) {
        chatsList.add(chat);
        notifyItemInserted(chatsList.size() - 1);
    }

    public void updateList(ArrayList<Chat> chatsList) {
        this.chatsList = chatsList;
        notifyDataSetChanged();
    }

    public ArrayList<Chat> getChatsList() {
        return chatsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ChatListItemBinding binding;

        public MyViewHolder(@NonNull ChatListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chat, String currentUserUid, CallbackChat callbackChat) {
            binding.setModel(chat);
            binding.setCurrentUserUid(currentUserUid);
            binding.contactChatContainer.setOnClickListener(v -> {
                if (callbackChat != null)
                    callbackChat.OnChatClicked(chat);
            });
        }
    }
}
