package com.elad.chatimeapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author - Elad Sabag
 * @date - 1/21/2023
 */
public class AndroidUtils {
    /**
     * This function check if the device has a camera or not.
     * @param context - The context is required in order to execute the camera check.
     * @return true if the device has a camera, else false.
     */
    public static boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * This function check if the device has a flash or not.
     * @param context - The context is required in order to execute the flash check.
     * @return true if the device has a flash, else false.
     */
    public static boolean hasFlash(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * This function check if the device has a proximity sensor or not.
     * @param context - The context is required in order to execute the proximity sensor check.
     * @return true if the device has a proximity sensor, else false.
     */
    public static boolean hasProximitySensor(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);
    }

    /**
     * This function check if the device has a fingerprint scanner or not.
     * @param context - The context is required in order to execute the fingerprint scanner check.
     * @return true if the device has a fingerprint scanner, else false.
     */
    public static boolean hasFingerprintScanner(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
    }

    /**
     * This function check if the device has a NFC or not.
     * @param context - The context is required in order to execute the NFC check.
     * @return true if the device has a NFC, else false.
     */
    public static boolean hasNFC(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }
    /**
     * This function check if the device has a GPS or not.
     * @param context - The context is required in order to execute the GPS check.
     * @return true if the device has a GPS, else false.
     */
    public static boolean hasGPS(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }
    /**
     * This function check if the device has a microphone or not.
     * @param context - The context is required in order to execute the microphone check.
     * @return true if the device has a microphone, else false.
     */
    public static boolean hasMicrophone(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    /**
     * This function check if the device is in silent mode or not.
     * @param context - The context is required in order to execute the check.
     * @return true if the device is in silent mode, else false.
     */
    public static boolean isDeviceSilent(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    /**
     * This function vibrate the device for a certain duration.
     * @param context - The context is required in order to execute the vibration.
     * @param duration - The duration of the vibration in milliseconds.
     */
    public static void vibrate(Context context, long duration) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(duration);
        }
    }

    /**
     * This function check if the device is connected to internet or not.
     * @param context - The context is required in order to execute the check.
     * @return true if the device is connected to internet, else false.
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * This function check if the device has accelerometer sensor or not.
     * @param context - The context is required in order to execute the check.
     * @return true if the device has accelerometer sensor, else false.
     */
    public static boolean hasAccelerometerSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        return accelerometerSensor != null;
    }

    /**
     * This function check if the device has Gyroscope sensor or not.
     * @param context - The context is required in order to execute the check.
     * @return true if the device has Gyroscope sensor, else false.
     */
    public static boolean hasGyroscopeSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        return gyroscopeSensor != null;
    }

    /**
     * This function check if the device's Bluetooth is on or off.
     * @param context - The context is required in order to execute the check.
     * @return true if the device's Bluetooth is on, else false.
     */
    public static boolean isBluetoothEnabled(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    /**
     * This function check if the device's Bluetooth is connected or not.
     * @param context - The context is required in order to execute the check.
     * @return true if the device's Bluetooth is connected, else false.
     */
    public static boolean isBluetoothConnected(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled() && bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_CONNECTED;
    }

    /**
     * This function get the device screen width in pixels.
     * @param context - The context is required in order to execute the check.
     * @return the device screen width in pixels.
     */
    public static int getDeviceScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * This function get the device screen height in pixels.
     * @param context - The context is required in order to execute the check.
     * @return the device screen height in pixels.
     */
    public static int getDeviceScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    /**
     * This function check if the device is charging or not.
     * @param context - The context is required in order to execute the check.
     * @return true if the device is charging, else false.
     */
    public static boolean isDeviceCharging(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    }

    /**
     * This function set the device screen brightness.
     * @param context - The context is required in order to execute the check.
     * @param brightness - The brightness value to set, it should be between 0 and 255.
     */
    public static void setScreenBrightness(Context context, int brightness) {
        if (brightness < 0) brightness = 0;
        if (brightness > 255) brightness = 255;
        ContentResolver cResolver = context.getContentResolver();
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    /**
     * This function lock the device screen orientation.
     * @param activity - The activity that you want to lock the orientation for.
     * @param orientation - The desired orientation you want to lock the screen with (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE or ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
     */
    public static void lockOrientation(Activity activity, int orientation) {
        activity.setRequestedOrientation(orientation);
    }

    /**
     * This function get the device screen orientation.
     * @param context - The context is required in order to execute the check.
     * @return the device screen orientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE or ActivityInfo.SCREEN_ORIENTATION_PORTRAIT).
     */
    public static int getScreenOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    /**
     * This function get the device android_id.
     * @param context - The context is required in order to execute the check.
     * @return the device android_id.
     */
    public static String getDeviceAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * This function get the amount of free RAM the device has.
     * @param context - The context is required in order to execute the check.
     * @return the amount of free RAM the device has in bytes.
     */
    public static long getFreeRAM(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }
}
