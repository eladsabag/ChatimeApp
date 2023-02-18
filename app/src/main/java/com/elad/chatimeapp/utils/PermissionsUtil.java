package com.elad.chatimeapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import java.util.Map;

/**
 * @author - Elad Sabag
 * @date - 1/21/2023
 */
public class PermissionsUtil {
    /**
     * This function opens the permissions settings on user's phone.
     * @param activity - The activity that executes this operation.
     */
    public static void openPermissionsSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * This function checks if all permissions is granted for given key, value map.
     * @param isGranted - The map that contains the un/granted permissions, key - permission, value true if granted else false.
     * @return true if all permissions granted else false
     */
    public static boolean checkIfAllPermissionsGranted(Map<String, Boolean> isGranted) {
        for (Boolean value : isGranted.values())
            if (!value) return false;
        return true;
    }

    /**
     * This function check if the user has camera permission.
     * The permission: CAMERA
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has camera permission else false
     */
    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has storage permissions.
     * The permissions: READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has storage permission else false
     */
    public static boolean hasStoragePermissions(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has read contacts permissions.
     * The permissions: READ_CONTACTS
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has read contacts permissions else false
     */
    public static boolean hasReadContactsPermissions(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function request for storage permissions.
     * The permissions: READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestStoragePermissions(final ActivityResultLauncher<String[]> permissionLauncher) {
        permissionLauncher.launch(new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }

    /**
     * This function request for read media images permission.
     * The permissions: READ_MEDIA_IMAGES
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestReadMediaImagesPermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
    }

    /**
     * This function request for read contacts permission.
     * The permission: READ_CONTACTS
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestReadContactsPermissions(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS);
    }

    /**
     * This function request for camera permission.
     * The permission: CAMERA
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestCameraPermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.CAMERA);
    }
}
