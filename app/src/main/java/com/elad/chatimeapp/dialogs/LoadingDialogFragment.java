package com.elad.chatimeapp.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.elad.chatimeapp.R;
import com.elad.chatimeapp.databinding.FragmentLoadingDialogBinding;

/**
 * @author - Elad Sabag
 * @date - 1/28/2023
 */
public class LoadingDialogFragment extends DialogFragment {
    public static final String TAG = "LoadingDialogFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLoadingDialogBinding binding = FragmentLoadingDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.setCancelable(false);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
