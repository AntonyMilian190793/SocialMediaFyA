package com.antonymilian.socialmediafya.receivers;

import static com.antonymilian.socialmediafya.services.MyFirebaseMeesagingClient.NOTIFICATION_REPLY;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.antonymilian.socialmediafya.models.Message;
import com.antonymilian.socialmediafya.providers.MessageProvider;
import com.antonymilian.socialmediafya.providers.TokenProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;

public class MessageReceiver extends BroadcastReceiver {

    String mExtraIdSender;
    String mExtraIdReceiver;
    String mExtraIdChat;
    String mExtraUsernameSender;
    String mExtraUsernameReceiver;
    String mExtraImageSender;
    String mExtraImageReceiver;
    int mExtraIdnotification;

    TokenProvider mTokenProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        mExtraIdSender = intent.getExtras().getString("idSender");
        mExtraIdReceiver = intent.getExtras().getString("idReceiver");
        mExtraIdChat = intent.getExtras().getString("idChat");
        mExtraUsernameSender = intent.getExtras().getString("usernameSender");
        mExtraUsernameReceiver = intent.getExtras().getString("usernameReceiver");
        mExtraImageSender = intent.getExtras().getString("imageSender");
        mExtraImageReceiver = intent.getExtras().getString("imageReceiver");

        mExtraIdnotification = intent.getExtras().getInt("idNotification");

        mTokenProvider = new TokenProvider();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mExtraIdnotification);

        String message = getMessageText(intent).toString();

        sendMessage(message);

    }

    private void sendMessage(String messageText) {
        final Message message = new Message();
        message.setIdChat(mExtraIdChat);
        message.setIdSender(mExtraIdReceiver);
        message.setIdReceiver(mExtraIdSender);
        message.setTimestamp(new Date().getTime());
        message.setViewed(false);
        message.setIdChat(mExtraIdChat);
        message.setMessage(messageText);

        MessageProvider messageProvider = new MessageProvider();

        messageProvider.create(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //getToken(message);
                }
            }
        });
    }
    private CharSequence getMessageText(Intent intent){
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if(remoteInput != null){
            return remoteInput.getCharSequence(NOTIFICATION_REPLY);
        }
        return null;
    }
}
