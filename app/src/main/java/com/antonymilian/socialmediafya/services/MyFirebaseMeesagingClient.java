package com.antonymilian.socialmediafya.services;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.antonymilian.socialmediafya.channel.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMeesagingClient extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        if(title != null){
            if(title.equals("NUEVO MENSAJE")){
                int idNotification = Integer.parseInt(data.get("idNotification"));
                showNotificationMessage(title, body, idNotification);
            }else{
                showNotification(title, body);
            }

        }
    }

    private void showNotification(String title, String body){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
        Random random = new Random();
        int n = random.nextInt(10000);
        notificationHelper.getManager().notify(n, builder.build());
    }
    private void showNotificationMessage(String title, String body, int idNotificationChat){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
        notificationHelper.getManager().notify(idNotificationChat, builder.build());
    }
}
