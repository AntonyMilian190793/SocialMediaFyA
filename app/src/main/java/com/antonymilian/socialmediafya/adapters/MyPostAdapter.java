package com.antonymilian.socialmediafya.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.activities.PostDetailActivity;
import com.antonymilian.socialmediafya.models.Post;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.LikesProvider;
import com.antonymilian.socialmediafya.providers.PostProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.antonymilian.socialmediafya.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Post, MyPostAdapter.ViewHolder> {

    Context context;
    UsersProvider mUsersProviders;
    LikesProvider mLikesProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    public MyPostAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUsersProviders = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();


    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();
        String relativeTime = RelativeTime.getTimeAgo(post.getTimestamp(), context);
        holder.textViewRelativeTime.setText(relativeTime);

        holder.textViewtitle.setText(post.getTitle().toUpperCase());

        if(post.getIdUser().equals(mAuthProvider.getUid())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if(post.getImage1() != null){
            if(!post.getImage1().isEmpty()){
                Picasso.with(context).load(post.getImage1()).into(holder.circleImageViewPost);
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

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDelet(postId);
            }
        });



    }

    private void showConfirmDelet(String postId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar publicación")
                .setMessage("¿Estás seguro de realizar esta acción?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        detelePost(postId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void detelePost(String postId) {
        mPostProvider.detele(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                
                if(task.isSuccessful()){
                    Toast.makeText(context, "El post se eliminó correctamente!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "No se pudo eliminar el post!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewtitle;
        TextView textViewRelativeTime;
        CircleImageView circleImageViewPost;
        ImageView imageViewDelete;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewtitle = view.findViewById(R.id.textViewTitleMyPost);
            textViewRelativeTime = view.findViewById(R.id.textViewRelativeTimeMyPost);
            circleImageViewPost = view.findViewById(R.id.circleImagenMyPost);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteMyPost);
            viewHolder = view;
        }
    }
}
