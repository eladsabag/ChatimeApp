package com.elad.chatimeapp.screens.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elad.chatimeapp.databinding.ConversationListItemBinding;
import com.elad.chatimeapp.model.Message;

import java.util.ArrayList;

/**
 * @author - Elad Sabag
 * @date - 2/10/2023
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private ArrayList<Message> messageList;
    private final String currentUserUid;

    public ChatAdapter(ArrayList<Message> messageList, String currentUserUid) {
        this.messageList = messageList;
        this.currentUserUid = currentUserUid;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConversationListItemBinding binding = ConversationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        holder.bind(messageList.get(position), currentUserUid);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void updateList(ArrayList<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ConversationListItemBinding binding;

        public MyViewHolder(@NonNull ConversationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message, String currentUserUid) {
            binding.setModel(message);
            binding.setCurrentUserUid(currentUserUid);
        }
    }
}
