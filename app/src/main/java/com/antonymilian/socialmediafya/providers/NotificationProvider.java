package com.antonymilian.socialmediafya.providers;

import com.antonymilian.socialmediafya.models.FCMBody;
import com.antonymilian.socialmediafya.models.FCMResponse;
import com.antonymilian.socialmediafya.retrofit.IFCMApi;
import com.antonymilian.socialmediafya.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider(){

    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }

}
