package com.elad.chatimeapp.data.firebase;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.elad.chatimeapp.MainActivity;
import com.elad.chatimeapp.R;
import com.elad.chatimeapp.utils.Constants;
import com.elad.chatimeapp.utils.SharedPrefsUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

/**
 * @author - Elad Sabag
 * @date - 1/24/2023
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "MyFirebaseMessagingService";
    public static String MAIN_ACTION = "com.elad.chatimeapp.firebase.myfirebasemessagingservice.action.main";
    public static String CHANNEL_ID = "com.elad.chatimeapp.CHANNEL_ID_FOREGROUND";
    public static int NOTIFICATION_ID = 154;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // Handle message received from Firebase Cloud Messaging
//        createNotification(message);
    }

    private void createNotification(RemoteMessage message) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(
                this,
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW); //Low importance prevent visual appearance for this notification channel on top

        notificationBuilder
                .setContentIntent(pendingIntent) // Open activity
                .setOngoing(true)
                .setSmallIcon(R.drawable.chatime_logo_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle("App in progress") // title for notification TODO
                .setContentText("Content"); // content for notification TODO

        Notification notification = notificationBuilder.build();

        startForeground(NOTIFICATION_ID, notification);
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance) {
        NotificationCompat.Builder builder;
        prepareChannel(context, channelId, importance);
        builder = new NotificationCompat.Builder(context, channelId);
        return builder;
    }

    @TargetApi(26)
    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app_name);
        final NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);

            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription("Chatime Channel Description"); // TODO

                // from another answer
                nChannel.enableLights(true);
                nChannel.setLightColor(ContextCompat.getColor(context, R.color.primaryColor));

                nm.createNotificationChannel(nChannel);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, token);
        SharedPrefsUtil.getInstance().putString(Constants.FCM_TOKEN, token);
    }
}
