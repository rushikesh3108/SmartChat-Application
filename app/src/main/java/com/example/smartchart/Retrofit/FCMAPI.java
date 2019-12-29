package com.example.smartchart.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMAPI {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAAYLdr6a4:APA91bEHYBjnDm3_oy7Wir3Gi_49iZ6LAgG43K37I-lMZEL6OC3p0EQotIseevPUIpE6fvvjzdCjfShPe0DWOnnvtjQM043Dst8g35dph7syr0dXJxD8poGYq4tn35sK9kP38FPJw-Lp"
    })
    @POST("/fcm/send")
    Call<ResponseBody> sendMessage(@Body MessageEntity messageEntity);
}
