package com.elad.chatimeapp.utils;

import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.elad.chatimeapp.BuildConfig;
import com.elad.chatimeapp.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;

/**
 * @author - Elad Sabag
 * @date - 1/28/2023
 */
public class BindingAdapters {

    @BindingAdapter(value = {"text", "subtext"})
    public static void setSpannableText(MaterialTextView materialTextView, String text, String[] subtext) {
        materialTextView.setText(text, MaterialTextView.BufferType.SPANNABLE);
        SpannableString str = (SpannableString) materialTextView.getText();
        String[] links = materialTextView.getContext().getResources().getStringArray(R.array.terms_and_policy_links);

        if (links.length != subtext.length) return;

        for (int i = 0; i < subtext.length; i++) {
            int start = text.indexOf(subtext[i]);
            int end = start + subtext[i].length();
            int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Util.openBrowserLink(links[finalI], widget.getContext());
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(materialTextView.getContext().getColor(R.color.primaryColor));
                }
            };
            str.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        materialTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @BindingAdapter("centerInParent")
    public static void setCenterInParent(LinearLayout linearLayout, Boolean centerInParent) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        if (centerInParent)
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        else
            layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.setLayoutParams(layoutParams);
    }

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

    @BindingAdapter(value = {"messageText"}, requireAll = false)
    public static void setMessageText(MaterialTextView view, String messageText) {
        try {
            view.setText(HashUtil.decrypt(messageText, BuildConfig.SECRET_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter(value = {"filter", "text"}, requireAll = false)
    public static void setText(MaterialAutoCompleteTextView view, boolean filter, String text) {
        view.setText(text, filter);
    }
}
