package com.antonymilian.socialmediafya.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.adapters.MessageAdapter;
import com.antonymilian.socialmediafya.models.Chat;
import com.antonymilian.socialmediafya.models.Message;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.ChatsProvider;
import com.antonymilian.socialmediafya.providers.MessageProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String mExtraIdUser1;
    String mExtraIdUser2;
    String mExtraIdChat;
    ChatsProvider mChatsProvider;
    UsersProvider mUsersProvider;
    MessageProvider mMessageProvider;
    AuthProvider mAuhAuthProvider;
    EditText mEditTextMessage;
    ImageView mImageViewSendMessage;
    CircleImageView mCircleImageProfile;
    TextView mTextViewUsername;
    TextView mTextViewRelativeTime;
    ImageView mImageViewBack;
    View mActionBarView;
    RecyclerView mRecyclerViewMessage;
    MessageAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatsProvider = new ChatsProvider();
        mMessageProvider = new MessageProvider();
        mAuhAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        mRecyclerViewMessage =findViewById(R.id.recycleViewMessage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerViewMessage.setLayoutManager(linearLayoutManager);

        mExtraIdUser1 = getIntent().getStringExtra("idUser1");
        mExtraIdUser2 = getIntent().getStringExtra("idUser2");
        mExtraIdChat = getIntent().getStringExtra("idChat");

        showCustomToolbar(R.layout.custom_chat_toolbar);

        mImageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        checkChatExists();
    }

    public void onStart() {
        super.onStart();
        Query query = mMessageProvider.getMessageByChat(mExtraIdChat);
        FirestoreRecyclerOptions<Message> options =
                new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        mAdapter = new MessageAdapter(options, ChatActivity.this);
        mRecyclerViewMessage.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.startListening();
    }

    private void sendMessage() {
        String textMessage = mEditTextMessage.getText().toString();

        if(!textMessage.isEmpty()){
            Message message = new Message();
            message.setIdChat(mExtraIdChat);

            if(mAuhAuthProvider.getUid().equals(mExtraIdUser1)){
                  message.setIdSender(mExtraIdUser1);
                  message.setIdReciver(mExtraIdUser2);
            }else{
                message.setIdSender(mExtraIdUser2);
                message.setIdReciver(mExtraIdUser1);
            }
            message.setTimestamp(new Date().getTime());
            message.setViewed(false);
            message.setIdChat(mExtraIdChat);
            message.setMessage(textMessage);

            mMessageProvider.create(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        mEditTextMessage.setText("");
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(ChatActivity.this, "El mensaje se creó correctamente!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChatActivity.this, "El mensaje no se pudo crear!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showCustomToolbar(int resource) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarView = inflater.inflate(resource, null);
        actionBar.setCustomView(mActionBarView);
        mCircleImageProfile = mActionBarView.findViewById(R.id.circleImageProfile);
        mTextViewUsername = mActionBarView.findViewById(R.id.textViewUsername);
        mTextViewRelativeTime = mActionBarView.findViewById(R.id.textViewRelativeTime);
        mImageViewBack = mActionBarView.findViewById(R.id.imageViewBack);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getUserInfo();
    }

    private void getUserInfo() {
        String idUserInfo = "";

        if(mAuhAuthProvider.getUid().equals(mExtraIdUser1)){
            idUserInfo = mExtraIdUser2;
        }else{
            idUserInfo = mExtraIdUser1;
        }
        mUsersProvider.getUser(idUserInfo).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if(imageProfile != null){
                            if(!imageProfile.equals("")){
                                Picasso.with(ChatActivity.this).load(imageProfile).into(mCircleImageProfile);

                            }
                        }
                    }
                }
            }
        });
    }

    private void checkChatExists(){
        mChatsProvider.getChatByUser1AndUser2(mExtraIdUser1, mExtraIdUser2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();

                if(size == 0){
                    createChat();
                }else{
                    mExtraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                }
            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setIdUser1(mExtraIdUser1);
        chat.setIdUser2(mExtraIdUser2);
        chat.setWriting(false);
        chat.setTimestamp(new Date().getTime());
        chat.setId(mExtraIdUser1 + mExtraIdUser2);
        ArrayList<String> ids = new ArrayList<>();
        ids.add(mExtraIdUser1);
        ids.add(mExtraIdUser2);
        chat.setIds(ids);
        mChatsProvider.create(chat);
    }
}