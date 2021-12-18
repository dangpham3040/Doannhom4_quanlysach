package com.example.doannhom4_quanlythuvien.Notification;

import com.example.doannhom4_quanlythuvien.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationApi {

    @POST("notification/sendToAll")
    Call<List<Notification>> addNotificaion(@Body Notification notification);
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://app-notification-android.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
