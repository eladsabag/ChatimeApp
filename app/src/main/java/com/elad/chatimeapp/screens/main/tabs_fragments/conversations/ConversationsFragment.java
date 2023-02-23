package com.elad.chatimeapp.screens.main.tabs_fragments.conversations;

import static com.elad.chatimeapp.utils.Constants.CHAT_ID_EXTRA;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentConversationsBinding;
import com.elad.chatimeapp.dialogs.PermissionDialogFragment;
import com.elad.chatimeapp.model.Chat;
import com.elad.chatimeapp.model.User;
import com.elad.chatimeapp.screens.main.MainViewModel;
import com.elad.chatimeapp.utils.HashUtil;
import com.elad.chatimeapp.utils.PermissionsUtil;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ConversationsFragment extends Fragment {
    private FragmentConversationsBinding binding;
    private ArrayList<Chat> chatsList;
    private ConversationsAdapter adapter;
    private NavController navController;
    private MainViewModel viewModel;
    private Context context;
    private final ActivityResultLauncher<Void> contactLauncher = registerForActivityResult(
            new ActivityResultContracts.PickContact(),
            uri -> {
                if (uri != null) {
                    getContactDetails(uri);
                }
            }
    );
    private final ConversationsAdapter.CallbackChat callbackChat = new ConversationsAdapter.CallbackChat() {
        @Override
        public void OnChatClicked(Chat chat) {
            Bundle bundle = new Bundle();
            bundle.putString(CHAT_ID_EXTRA, chat.getChatId());
            navController.navigate(R.id.action_main_dest_to_chat_dest, bundle);
        }
    };
    private final ActivityResultLauncher<String> requestContactPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    contactLauncher.launch(null);
                } else {
                    requestContactPermissionsWithRationaleCheck();
                }
            }
    );
    private final Observer<ArrayList<Chat>> chatsObserver = new Observer<>() {
        @Override
        public void onChanged(ArrayList<Chat> chats) {
            adapter.updateList(chats);
            chatsList = chats;
        }
    };
    private final Observer<Chat> chatObserver = new Observer<>() {
        @Override
        public void onChanged(Chat chat) {
            if (adapter != null && chat != null) {
                adapter.addChat(chat);
                chatsList = adapter.getChatsList();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        chatsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConversationsBinding.inflate(inflater, container, false);
        binding.setModel(viewModel.getCurrentUser());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewModel.getChatsLiveData().observe(getViewLifecycleOwner(), chatsObserver);
        viewModel.getChatLiveData().observe(getViewLifecycleOwner(), chatObserver);
        initViews();
    }

    private void initViews() {
        adapter = new ConversationsAdapter(chatsList,viewModel.getCurrentUserUid(), callbackChat);
        binding.chatsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.chatsRecyclerView.setAdapter(adapter);
        binding.chatsBtnAdd.setOnClickListener(v -> onPlusClicked());
    }

    private void onPlusClicked() {
        if (!PermissionsUtil.hasReadContactsPermissions(requireContext())) {
            PermissionsUtil.requestReadContactsPermissions(requestContactPermissionLauncher);
            return;
        }
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
        if (currentUser == null) return;

        phoneNumber = phoneNumber
                .replace("-","")
                .replace(" ", "");

        Chat chat = new Chat(
                HashUtil.generateChatId(currentUser.getPhoneNumber(), phoneNumber),
                currentUser.getUid(), // me
                "", // contact, later
                viewModel.getCurrentUser().getName(), // me
                contactName, // contact
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()),
                new ArrayList<>(),
                viewModel.getCurrentUser().getProfileImage(), // me
                "" // contact, later
        );
        viewModel.getContactUserIfExistAndAddChat(chat, phoneNumber);
    }

    private void requestContactPermissionsWithRationaleCheck() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {
            new PermissionDialogFragment(getString(R.string.contact_permissions_title), getString(R.string.contact_permissions_message), new PermissionDialogFragment.PermissionDialogCallback() {
                @Override
                public void onConfirm() { PermissionsUtil.requestReadContactsPermissions(requestContactPermissionLauncher); }
                @Override
                public void onCancel() { PermissionsUtil.openPermissionsSettings(requireActivity()); }
            }).show(getChildFragmentManager(), PermissionDialogFragment.TAG);
        } else {
            PermissionsUtil.openPermissionsSettings(requireActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.dismissChat();
    }
}