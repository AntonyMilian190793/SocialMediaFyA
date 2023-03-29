package com.antonymilian.socialmediafya.services;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.antonymilian.socialmediafya.channel.NotificationHelper;
import com.antonymilian.socialmediafya.models.Message;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

                showNotificationMessage(data);
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
    private void showNotificationMessage(Map<String, String> data){
        String title = data.get("title");
        String body = data.get("body");
        String usernameSender = data.get("usernameSender");
        String usernameReceiver = data.get("usernameReceiver");
        String lastMessage = data.get("lastMessage");
        String mesaggesJSON = data.get("messages");
        String imageSender = data.get("imageSender");
        String imageReceiver = data.get("imageReceiver");

        final int idNotification = Integer.parseInt(data.get("idNotification"));
        Gson gson = new Gson();
        Message[] messages = gson.fromJson(mesaggesJSON, Message[].class);

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getApplicationContext())
                                .load(imageSender)
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmapSender, Picasso.LoadedFrom from) {

                                        Picasso.with(getApplicationContext())
                                                .load(imageReceiver)
                                                .into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(Bitmap bitmapReceiver, Picasso.LoadedFrom from) {

                                                        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
                                                        NotificationCompat.Builder builder =
                                                                notificationHelper.getNotificationMessage(
                                                                        messages,
                                                                        usernameSender,
                                                                        usernameReceiver,
                                                                        lastMessage,
                                                                        bitmapSender,
                                                                        bitmapReceiver
                                                                );

                                                        notificationHelper.getManager().notify(idNotification, builder.build());
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Drawable errorDrawable) {

                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                    }
                });

    }
}
