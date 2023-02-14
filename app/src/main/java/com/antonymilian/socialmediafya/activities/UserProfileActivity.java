package com.antonymilian.socialmediafya.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.adapters.MyPostAdapter;
import com.antonymilian.socialmediafya.models.Post;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.PostProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    String mExtraidUser;
    CircleImageView mCircleImageViewBack;
    MyPostAdapter mAdapter;
    RecyclerView mRecyclerView;
    TextView mTextViewPostExist;



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
        mCircleImageViewBack = findViewById(R.id.cicleImageBack);
        mRecyclerView = findViewById(R.id.recycleViewMyPost);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mExtraidUser = getIntent().getStringExtra("idUser");
        getUser();
        getPostNumber();
        checkIfExistPost();
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
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.startListening();
    }

    private void checkIfExistPost() {
        mPostProvider.getPostByUser(mExtraidUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                int numberPost = queryDocumentSnapshots.size();

                if(numberPost > 0){
                    mTextViewPostExist.setText("Publicaciones");
                    mTextViewPostExist.setTextColor(Color.RED);
                }else{
                    mTextViewPostExist.setText("No hay publicaciones");
                    mTextViewPostExist.setTextColor(Color.GRAY);
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
}