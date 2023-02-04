package com.elad.chatimeapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.elad.chatimeapp.BuildConfig;

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
     * This function check if the user has location permissions.
     * The permissions: ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION.
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has location permissions else false
     */
    public static boolean hasLocationPermissions(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
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
     * This function check if the user has microphone permission.
     * The permission: RECORD_AUDIO
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has microphone permission else false
     */
    public static boolean hasMicrophonePermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has calendar permissions.
     * The permissions: READ_CALENDAR, WRITE_CALENDAR
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has calendar permissions else false
     */
    public static boolean hasCalendarPermissions(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has contacts permissions.
     * The permissions: READ_CONTACTS, WRITE_CONTACTS
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has contacts permissions else false
     */
    public static boolean hasContactsPermissions(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has body sensors permission.
     * The permission: BODY_SENSORS
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has body sensors permission else false
     */
    public static boolean hasBodySensorsPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has phone call permission.
     * The permission: CALL_PHONE
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has phone call permission else false
     */
    public static boolean hasPhoneCallPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function check if the user has bluetooth permissions.
     * For Android 12+ it checks for the newer permissions,
     * else it checks for older permissions.
     * The permission: BLUETOOTH_SCAN, BLUETOOTH_CONNECT, BLUETOOTH, BLUETOOTH_ADMIN
     * @param context - The context is required in order to execute the permissions check.
     * @return true if has bluetooth permissions else false
     */
    public static boolean hasBluetoothPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_ADMIN
                    ) == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * This function request for location permissions.
     * The permissions: ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION.
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestLocationPermissions(final ActivityResultLauncher<String[]> permissionLauncher) {
        permissionLauncher.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    /**
     * This function request for microphone permission.
     * The permission: RECORD_AUDIO
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestMicrophonePermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    /**
     * This function request for read phone state permission.
     * The permission: READ_PHONE_STATE
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestReadPhoneStatePermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * This function request for send SMS permission.
     * The permission: SEND_SMS
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestSendSMSPermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.SEND_SMS);
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
     * This function request for calendar permissions.
     * The permissions: READ_CALENDAR, WRITE_CALENDAR
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestCalendarPermissions(final ActivityResultLauncher<String[]> permissionLauncher) {
        permissionLauncher.launch(new String[] {
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
        });
    }

    /**
     * This function request for contacts permissions.
     * The permissions: READ_CONTACTS, WRITE_CONTACTS
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestContactsPermissions(final ActivityResultLauncher<String[]> permissionLauncher) {
        permissionLauncher.launch(new String[] {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
        });
    }

    /**
     * This function request for body sensors permission.
     * The permission: BODY_SENSORS
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestBodySensorsPermission(final ActivityResultLauncher<String[]> permissionLauncher) {
        permissionLauncher.launch(new String[] {
                Manifest.permission.BODY_SENSORS
        });
    }

    /**
     * This function request for camera permission.
     * The permission: CAMERA
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestCameraPermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.CAMERA);
    }

    /**
     * This function request for phone call permission.
     * The permission: CALL_PHONE
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestPhoneCallPermission(final ActivityResultLauncher<String> permissionLauncher) {
        permissionLauncher.launch(Manifest.permission.CALL_PHONE);
    }

    /**
     * This function request for bluetooth permissions.
     * For Android 12+ it asks for the newer permissions,
     * else it asks for older permissions.
     * The permission: BLUETOOTH_SCAN, BLUETOOTH_CONNECT, BLUETOOTH, BLUETOOTH_ADMIN
     * @param permissionLauncher - The launcher that suppose to execute the request.
     */
    public static void requestBluetoothPermissions(final ActivityResultLauncher<String[]> permissionLauncher) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
            });
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
            });
        }
    }
}
