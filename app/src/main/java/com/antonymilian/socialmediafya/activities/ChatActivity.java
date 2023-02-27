package com.antonymilian.socialmediafya.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.models.Chat;
import com.antonymilian.socialmediafya.providers.ChatsProvider;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String mExtraIdUser1;
    String mExtraIdUser2;
    ChatsProvider mChatsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mExtraIdUser1 = getIntent().getStringExtra("idUser1");
        mExtraIdUser2 = getIntent().getStringExtra("idUser2");
        mChatsProvider = new ChatsProvider();

        createChat();
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setIdUser1(mExtraIdUser1);
        chat.setIdUser2(mExtraIdUser2);
        chat.setWriting(false);
        chat.setTimestamp(new Date().getTime());
        mChatsProvider.create(chat);
    }
}