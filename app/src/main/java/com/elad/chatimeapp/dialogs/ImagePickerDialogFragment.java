package com.elad.chatimeapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.utils.PermissionsUtil;

/**
 * @author - Elad Sabag
 * @date - 2/2/2023
 */
public class ImagePickerDialogFragment extends DialogFragment {
    public static final String TAG = "ImagePickerDialogFragment";
    private final ImagePickerDialogFragment.ImagePickerDialogCallback imagePickerDialogCallback;

    public ImagePickerDialogFragment(ImagePickerDialogFragment.ImagePickerDialogCallback imagePickerDialogCallback) {
        this.imagePickerDialogCallback = imagePickerDialogCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        String[] options = {"Take Picture", "Select From Gallery"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    if (imagePickerDialogCallback != null)
                        imagePickerDialogCallback.onCamera();
                    break;
                case 1:
                    if (imagePickerDialogCallback != null)
                        imagePickerDialogCallback.onGallery();
                    break;
            }
        });
        return builder.create();
    }

    public interface ImagePickerDialogCallback {
        void onCamera();
        void onGallery();
    }
}
