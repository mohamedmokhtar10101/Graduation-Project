package com.gp.findlost.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutionException;

public class AppMessagingService extends FirebaseMessagingService {

    private static final String TAG = AppMessagingService.class.getName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v(TAG, "RemoteMessage " + remoteMessage);
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String notificationBody;
        String notificationTitle;

        Log.v(TAG, "RemoteMessage " + remoteMessage.getData().get("title"));
        Log.v(TAG, "RemoteMessage " + remoteMessage.getData().get("body"));

        try {
            boolean isAppRunning = new RunningTaskManager().execute(this).get();
            if (isAppRunning) {
                Log.v(TAG, "Foreground");
                EventBus.getDefault().post("New Message");
                return;
            } else {
                Log.v(TAG, "Background");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            notificationTitle = remoteMessage.getData().get("title");
            notificationBody = remoteMessage.getData().get("body");
            AppNotificationBuilder.pushNotification(
                    notificationTitle,
                    notificationBody,
                    getApplicationContext());

        } catch (NullPointerException exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
}
