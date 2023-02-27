package com.antonymilian.socialmediafya.providers;

import com.antonymilian.socialmediafya.models.Chat;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatsProvider {

    CollectionReference mCollection;

    public ChatsProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Chats");
    }

    public void create(Chat chat){
         mCollection.document(chat.getIdUser1()).collection("Users").document(chat.getIdUser2()).set(chat);
         mCollection.document(chat.getIdUser2()).collection("Users").document(chat.getIdUser1()).set(chat);
    }
}
