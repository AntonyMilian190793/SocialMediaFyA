package com.antonymilian.socialmediafya.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.activities.PostDetailActivity;
import com.antonymilian.socialmediafya.models.Like;
import com.antonymilian.socialmediafya.models.Post;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.LikesProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {

    Context context;
    UsersProvider mUsersProviders;
    LikesProvider mLikesProvider;
    AuthProvider mAuthProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUsersProviders = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();


    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();

        holder.textViewtitle.setText(post.getTitle().toUpperCase());
        holder.textViewDescription.setText(post.getDescripcion());

        if(post.getImage1() != null){
            if(!post.getImage1().isEmpty()){
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);
            }
        }
            holder.viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("id", postId);
                    context.startActivity(intent);
                }
            });

        holder.imageViewLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like like = new Like();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                like(like, holder);
            }
        });

        getUserUInfo(post.getIdUser(), holder);
        getNumerLikesByPost(postId, holder);
        checkIfExistsLike(postId,  mAuthProvider.getUid(), holder);
    }

    private void getNumerLikesByPost(String idPost, final ViewHolder holder){
        mLikesProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    int numerLikes = queryDocumentSnapshots.size();

                    if(numerLikes == 0){
                        holder.textViewLikes.setText(" No tiene me gusta");
                    }else if(numerLikes == 1){
                        holder.textViewLikes.setText(String.valueOf(numerLikes) + " Me gusta");
                    }else{
                        holder.textViewLikes.setText(String.valueOf(numerLikes) + " Me gustas");
                    }
                }else{
                    Toast.makeText(context, "La consulta es nula", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void like(final Like like, final ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();

                if(numberDocuments>0){
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_fyagray);
                    mLikesProvider.delete(idLike);
                }else{
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_fya);
                    mLikesProvider.create(like);
                }
            }
        });
    }

    private void checkIfExistsLike(String idPost, String idUser, final ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(idPost, idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();

                if(numberDocuments>0){
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_fya);

                }else{
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_fyagray);
                }
            }
        });
    }

    private void getUserUInfo(String idUser, final ViewHolder holder) {
        mUsersProviders.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        holder.textViewUsername.setText("By: " + username.toUpperCase());
                    }
                }

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewtitle;
        TextView textViewDescription;
        TextView textViewUsername;
        TextView textViewLikes;
        ImageView imageViewPost;
        ImageView imageViewLikes;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewtitle = view.findViewById(R.id.textViewTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            textViewUsername = view.findViewById(R.id.textViewUsernamePostCard);
            textViewLikes = view.findViewById(R.id.textViewLikes);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            imageViewLikes = view.findViewById(R.id.imageViewLikes);
            viewHolder = view;
        }
    }
}
