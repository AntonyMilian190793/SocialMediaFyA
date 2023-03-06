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
import com.antonymilian.socialmediafya.models.Message;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.antonymilian.socialmediafya.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ViewHolder> {

    Context context;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    public MessageAdapter(FirestoreRecyclerOptions<Message> options, Context context){
        super(options);
        this.context = context;
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String messageId = document.getId();
        holder.textViewMessage.setText(message.getMessage());
        String relativeTime = RelativeTime.getTimeAgo(message.getTimestamp(), context);
        holder.textViewDate.setText(relativeTime);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView textViewDate;
        ImageView imageViewViewed;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDateMessage);
            imageViewViewed = view.findViewById(R.id.imageviewViewedMessage);
            viewHolder = view;
        }
    }
}
