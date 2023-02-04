package com.elad.chatimeapp.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import com.elad.chatimeapp.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.gson.Gson;

/**
 * @author - Elad Sabag
 * @date - 1/21/2023
 */
public class MyBindingAdapters {
    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String profileImage) {
        Bitmap bitmap = null;
        if (profileImage != null && !profileImage.isEmpty())
            bitmap = Util.decodeBitmapFromBase64(profileImage);

        Glide.with(view.getContext())
                .load(bitmap != null ? bitmap : R.drawable.male)
                .placeholder(R.drawable.male)
                .into(view);
    }

    @BindingAdapter(value = {"filter", "text"}, requireAll = false)
    public static void setText(MaterialAutoCompleteTextView view, boolean filter, String text) {
        view.setText(text, filter);
    }
}
