package com.elad.chatimeapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.elad.chatimeapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

/**
 * @author - Elad Sabag
 * @date - 1/27/2023
 */
public class Util {
    public static void openHtmlTextDialog(Activity activity, String fileNameInAssets) {
        String str = "";
        InputStream is = null;

        try {
            is = activity.getAssets().open(fileNameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        materialAlertDialogBuilder.setPositiveButton(activity.getString(R.string.close), null);

        materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));

        AlertDialog al = materialAlertDialogBuilder.show();
        TextView alertTextView = al.findViewById(android.R.id.message);
        if (alertTextView != null)
            alertTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void openBrowserLink(String link, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }

    public static byte[] convertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    public static String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedString;
    }

    public static Bitmap decodeBitmapFromBase64(String encodedString) {
        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
