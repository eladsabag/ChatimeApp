package com.elad.chatimeapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.elad.chatimeapp.R;

/**
 * @author - Elad Sabag
 * @date - 2/2/2023
 */
public class PermissionDialogFragment extends DialogFragment {
    public static final String TAG = "PermissionDialogFragment";
    private final String permissionTitle;
    private final String permissionMessage;
    private final PermissionDialogFragment.PermissionDialogCallback permissionDialogCallback;

    public PermissionDialogFragment(String permissionTitle, String permissionMessage, PermissionDialogFragment.PermissionDialogCallback permissionDialogCallback) {
        this.permissionTitle = permissionTitle;
        this.permissionMessage = permissionMessage;
        this.permissionDialogCallback = permissionDialogCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(permissionTitle)
                .setMessage(permissionMessage)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    if (permissionDialogCallback != null)
                        permissionDialogCallback.onConfirm();
                    dialog.cancel();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                    if (permissionDialogCallback != null)
                        permissionDialogCallback.onCancel();
                    dialog.cancel();
                    }
                )
                .create();
    }

    public interface PermissionDialogCallback {
        void onConfirm();
        void onCancel();
    }
}
