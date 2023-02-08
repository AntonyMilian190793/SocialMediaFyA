package com.antonymilian.socialmediafya.providers;

import com.antonymilian.socialmediafya.models.Comment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommentsProvider {

    CollectionReference mCollection;

    public CommentsProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comment comment){
        return mCollection.document().set(comment);
    }
}
