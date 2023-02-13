package com.elad.chatimeapp.screens.main.tabs_fragments.chats;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentChatsBinding;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.screens.main.MainViewModel;
import com.elad.chatimeapp.utils.ChatIdGenerator;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;
    private ArrayList<Chat> chatsList;
    private ChatsAdapter adapter;
    private NavController navController;
    private MainViewModel viewModel;
    private Context context;
    private final ActivityResultLauncher<Void> contactLauncher = registerForActivityResult(
            new ActivityResultContracts.PickContact(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        getContactDetails(uri);
                    }
                }
            }
    );
    private final ChatsAdapter.CallbackChat callbackChat = new ChatsAdapter.CallbackChat() {
        @Override
        public void OnChatClicked(Chat chat) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("chat", chat);
            navController.navigate(R.id.action_main_dest_to_conversation_dest, bundle);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        binding.setModel(viewModel.getUser());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        chatsList = new ArrayList<>(viewModel.getUser().getChats().values());
        adapter = new ChatsAdapter(chatsList, callbackChat);
        initViews();
    }

    private void initViews() {
        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.chatsRecyclerView.setAdapter(adapter);
        binding.chatsBtnAdd.setOnClickListener(v -> onPlusClicked());
    }

    private void onPlusClicked() {
//        navController.navigate(R.id.action_main_dest_to_contacts_dest);
        contactLauncher.launch(null);
    }

    private void getContactDetails(Uri contactUri) {
        // Define a projection that specifies the columns from the database to query
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        // Query the Contacts Provider
        Cursor cursor = context.getContentResolver().query(
                contactUri,
                projection,
                null,
                null,
                null
        );

        // Check if the cursor has at least one result
        if (cursor.moveToFirst()) {
            // Get the ID and Display Name of the contact
            int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            if (idColumnIndex == -1 || displayNameColumnIndex == -1) {
                // The columns are not found in the cursor, handle the error
                Log.e("Contacts", "Error: Missing required columns in cursor");
            } else {
                // The columns are found in the cursor, continue with processing
                String contactId = cursor.getString(idColumnIndex);
                String contactName = cursor.getString(displayNameColumnIndex);
                // Check if the contact has a phone number
                int hasPhoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                if (hasPhoneNumberColumnIndex == -1) {
                    // The column is not found in the cursor, handle the error
                    Log.e("Contacts", "Error: Missing required column in cursor");
                } else {
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(hasPhoneNumberColumnIndex));
                    if (hasPhoneNumber > 0) {
                        // Query the Phone Numbers of the contact
                        Cursor phoneCursor = context.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{contactId},
                                null
                        );

                        // Iterate over the results of the phone number query
                        while (phoneCursor.moveToNext()) {
                            // Get the phone number of the contact
                            int phoneNumberColumnIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            if (phoneNumberColumnIndex == -1) {
                                // The column is not found in the cursor, handle the error
                                Log.e("Contacts", "Error: Missing required column in cursor");
                            } else {
                                String phoneNumber = phoneCursor.getString(phoneNumberColumnIndex);

                                // Log the contact name and phone number
                                Log.i("Contacts", contactName + ": " + phoneNumber);

                                handleContact(contactName, phoneNumber);
                            }
                        }
                        // Close the phone cursor
                        phoneCursor.close();
                    }
                }
            }
        }
        // Close the cursor for the contact query
        cursor.close();
    }

    private void handleContact(String contactName, String phoneNumber) {
        FirebaseUser currentUser = viewModel.getmAuth().getCurrentUser();
        // TODO prevent duplicate chats
        if (currentUser != null) {
            Chat chat = new Chat(ChatIdGenerator.generateChatId(currentUser.getPhoneNumber(), phoneNumber),contactName, "" ,"", new ArrayList<>());
            viewModel.addChatToUser(chat, currentUser.getUid());
            adapter.addChat(chat);
        }
    }
}