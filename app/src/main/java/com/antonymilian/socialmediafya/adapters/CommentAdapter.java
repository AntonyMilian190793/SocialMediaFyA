package com.antonymilian.socialmediafya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.models.Comment;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder> {

    Context context;
    UsersProvider mUsersProvider;

    public CommentAdapter(FirestoreRecyclerOptions<Comment> options, Context context){
        super(options);
        this.context = context;
        mUsersProvider = new UsersProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String commentId = document.getId();
        String idUser = document.getString("idUser");


        holder.textViewComment.setText(comment.getComment());
        getUserInfo(idUser, holder);


    }

    private void getUserInfo(String idUSer, final ViewHolder holder){
        mUsersProvider.getUser(idUSer).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        holder.textViewUsername.setText(username);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if(imageProfile != null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(context).load(imageProfile).into(holder.circleImagenComment);
                            }
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comment, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUsername;
        TextView textViewComment;
        ImageView circleImagenComment;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewUsername = view.findViewById(R.id.textViewUsername);
            textViewComment = view.findViewById(R.id.textViewComment)   ;
            circleImagenComment = view.findViewById(R.id.circleImagenComment);
            viewHolder = view;
        }
    }
}
