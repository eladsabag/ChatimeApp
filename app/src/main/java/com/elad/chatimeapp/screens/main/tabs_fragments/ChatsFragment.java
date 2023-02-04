package com.elad.chatimeapp.screens.main.tabs_fragments;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;
    private NavController navController;
    private final ActivityResultLauncher<Void> contactLauncher = registerForActivityResult(
            new ActivityResultContracts.PickContact(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        String[] projection = {
//                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        };

                        Cursor cursor = requireContext().getContentResolver().query(uri, projection,
                                null, null, null);

                        cursor.moveToFirst();

//                        int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                        String number = cursor.getString(numberColumnIndex);

                        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String name = cursor.getString(nameColumnIndex);

                        Toast.makeText(requireContext(), name + " ", Toast.LENGTH_SHORT).show();

                        cursor.close();
                    }
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initViews();
    }

    private void initViews() {
        binding.chatsBtnAdd.setOnClickListener(v -> onPlusClicked());
    }

    private void onPlusClicked() {
//        navController.navigate(R.id.action_main_dest_to_contacts_dest);
        contactLauncher.launch(null);
    }
}