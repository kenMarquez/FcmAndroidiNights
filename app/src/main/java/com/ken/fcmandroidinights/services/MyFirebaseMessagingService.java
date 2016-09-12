package com.ken.fcmandroidinights.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ken.fcmandroidinights.MainActivity;
import com.ken.fcmandroidinights.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ken on 11/09/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        sendNotification("prueba", "prueba", new HashMap<String, String>());
        Map<String, String> data = remoteMessage.getData();
        logData(data);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            logBody(remoteMessage.getNotification());
            String message = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();

            sendNotification(title, message, data);
        }


    }

    private void logData(Map<String, String> data) {
        // Check if message contains a data payload.
        if (data != null && data.size() > 0) {
            Log.d(TAG, "Message data payload: " + data);
        }
    }

    private void logBody(RemoteMessage.Notification notification) {
        // Check body data.
        Log.d(TAG, "Message Notification Body: " + notification.getBody());
        Log.d(TAG, "Message Notification Body: " + notification.getTitle());
        Log.d(TAG, "Message Notification Body: " + notification.getSound());
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title, String message, Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);

        for (String key : data.keySet()) {
            intent.putExtra(key, data.get(key));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int id = random.nextInt(2000);
        notificationManager.notify(id, notificationBuilder.build());
    }


    private void log(String content) {
        Log.i(TAG, content);
    }


}
