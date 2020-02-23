package com.example.smartchart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.ModelClass.Users;
import com.example.smartchart.Retrofit.Data;
import com.example.smartchart.Retrofit.FCMAPI;
import com.example.smartchart.Retrofit.MessageEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SheduleMessageBroadcast extends BroadcastReceiver {
    private static final String TAG = "SheduleMessageBroadcast";



    @Override
    public void onReceive(Context context, Intent intent) {



        String Senderid = intent.getStringExtra("sender");
        String Body = intent.getStringExtra("message");

        String userid = intent.getStringExtra("userid");

        String messageid = intent.getStringExtra("messageid");

        Long timetamp = intent.getLongExtra("timestamp", 0);


        Log.d(TAG, "onReceive: sheduller " + Body);
        Log.d(TAG, "onReceive:badsacs " + Senderid);



        MessageData message = new MessageData();


        DatabaseHandler messageDatabaseHandler = new DatabaseHandler(context);
        message.setMessageId(messageid);
        message.setConversionId(Senderid);
        message.setBody(Body);
        message.setTimeStamp(String.valueOf(timetamp));
        message.setSenderId(userid);
        Log.d(TAG, "MessageEntity Class: " + message.toString());


        messageDatabaseHandler.insertMessage(message);


        sendmessage(Senderid, Body, userid, messageid, timetamp);


    }

    private void sendmessage(String senderid, String body, String userid, String messageid, Long timetamp) {

        Retrofit retrofit = BaseApplication.getRetrofitInstance();
        FCMAPI api = retrofit.create(FCMAPI.class);
        MessageEntity messageEntity = new MessageEntity();
        Data data = new Data();
        data.senderId = userid;
        data.receiverId = senderid;
        data.body = body;
        data.messageId = messageid;
        data.timeStamp = timetamp;
        messageEntity.data = data;
        messageEntity.to = "/topics/" + senderid;
        Log.d(TAG, "sendMessage: RecevierId : " + senderid);


        api.sendMessage(messageEntity).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: MessageEntity shedule send successfully");
                    //  messageData.setDeliveryStatus("sucessfull");

                    Log.d(TAG, "onResponse: 12");
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

}

