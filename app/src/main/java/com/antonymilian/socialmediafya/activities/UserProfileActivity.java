package com.antonymilian.socialmediafya.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.adapters.MyPostAdapter;
import com.antonymilian.socialmediafya.models.Post;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.PostProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.antonymilian.socialmediafya.utils.ViewedMessageHelper;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView mTextViewUsername;
    TextView mTextViewPhome;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    ImageView mImageViewCover;
    CircleImageView mCicleImageViewProfile;
    Toolbar mToolbar;
    FloatingActionButton mFabChat;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    String mExtraidUser;
    MyPostAdapter mAdapter;
    RecyclerView mRecyclerView;
    TextView mTextViewPostExist;
    ListenerRegistration mListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        mTextViewEmail = findViewById(R.id.textViewEmail);
        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewPhome = findViewById(R.id.textViewPhone);
        mTextViewPostNumber = findViewById(R.id.textViewPostNumber);
        mTextViewPostExist = findViewById(R.id.textViewPostExist);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mCicleImageViewProfile = findViewById(R.id.circleImageProfile);
        mFabChat = findViewById(R.id.fabChat);
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycleViewMyPost);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mExtraidUser = getIntent().getStringExtra("idUser");

        if(mAuthProvider.getUid().equals(mExtraidUser)){
            mFabChat.setEnabled(false);
        }

        mFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChatActivity();
            }
        });

        getUser();
        getPostNumber();
        checkIfExistPost();
    }

    private void goToChatActivity() {
        Intent intent = new Intent(UserProfileActivity.this, ChatActivity.class);
        intent.putExtra("idUser1", mAuthProvider.getUid());
        intent.putExtra("idUser2", mExtraidUser);
        startActivity(intent);
    }

    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mExtraidUser);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mAdapter = new MyPostAdapter(options, UserProfileActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, UserProfileActivity.this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.startListening();
    }
    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, UserProfileActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListener != null){
            mListener.remove();
        }
    }

    private void checkIfExistPost() {
        mListener = mPostProvider.getPostByUser(mExtraidUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(queryDocumentSnapshots != null){
                    int numberPost = queryDocumentSnapshots.size();
                    if(numberPost > 0){
                        mTextViewPostExist.setText("Publicaciones");
                        mTextViewPostExist.setTextColor(Color.RED);
                    }else{
                        mTextViewPostExist.setText("No hay publicaciones");
                        mTextViewPostExist.setTextColor(Color.GRAY);
                    }
                }

            }
        });
    }

    private void getPostNumber(){
        mPostProvider.getPostByUser(mExtraidUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }

    private void getUser(){
        mUsersProvider.getUser(mExtraidUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("email")){
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if(documentSnapshot.contains("phone")){
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhome.setText(phone);
                    }
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");

                        if(imageProfile != null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(UserProfileActivity.this).load(imageProfile).into(mCicleImageViewProfile);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        String imageCover = documentSnapshot.getString("image_cover");

                        if(imageCover != null){
                            if(!imageCover.isEmpty()){
                                Picasso.with(UserProfileActivity.this).load(imageCover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}