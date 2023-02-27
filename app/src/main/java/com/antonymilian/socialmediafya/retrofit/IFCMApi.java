package com.antonymilian.socialmediafya.retrofit;

import com.antonymilian.socialmediafya.models.FCMBody;
import com.antonymilian.socialmediafya.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "content-Type:application/json",
            "Authorization:key=AAAAsBO8U1Y:APA91bF67R-y4MjOIHmmAhB3LA0I5WyAbKctdpdnHBGy_HwVvF9tkB-gG9_66W_wCchZnOLVZpErh_hBSYgc5lzddc8dKUYg3zlrMNIvIMwzNUrHAkt-slSyZ-252UEAa7xbA3iHkbbr\t\n"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
