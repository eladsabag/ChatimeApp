package com.elad.chatimeapp.screens.main.tabs_fragments.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elad.chatimeapp.databinding.ChatListItemBinding;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.screens.main.MainViewModel;

import java.util.ArrayList;

/**
 * @author - Elad Sabag
 * @date - 2/10/2023
 */
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {
    private ArrayList<Chat> chatsList;
    private MainViewModel viewModel;

    public ChatsAdapter(ArrayList<Chat> chatsList, MainViewModel viewModel) {
        this.chatsList = chatsList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ChatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatListItemBinding binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.MyViewHolder holder, int position) {
        holder.bind(chatsList.get(position), viewModel);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ChatListItemBinding binding;

        public MyViewHolder(@NonNull ChatListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chat, MainViewModel viewModel) {
            binding.setModel(chat);
//            binding.contactChatContainer.setOnLongClickListener(v -> viewModel.deleteChat(chat));
        }
    }
}
