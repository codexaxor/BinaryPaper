package com.codexaxor.mac.googlevisionv1;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody());
    }

    public void showNotification(String title, String message)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications").setContentTitle("This is my title").setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true).setContentText("This is my text");
        NotificationManagerCompat manager =  NotificationManagerCompat.from(this);

        manager.notify(999, builder.build());
    }
}
