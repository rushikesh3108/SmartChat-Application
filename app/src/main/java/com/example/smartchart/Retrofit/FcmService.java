package com.example.smartchart.Retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.MessageData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class FcmService extends FirebaseMessagingService {

    private static final String TAG = "FcmService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());

        DatabaseHandler messageDatabaseHandler = new DatabaseHandler(this);

        try {

            Log.d(TAG, "onMessageReceived: 0 ");
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
            Log.d(TAG, "onMessageReceived: 1");
            String senderId = jsonObject.getString("senderId");
            Log.d(TAG, "onMessageReceived: "+senderId);
            String messageBody = jsonObject.getString("body");
            Log.d(TAG, "onMessageReceived:'body  "+messageBody);
            String messageId = jsonObject.getString("messageId");
            Log.d(TAG, "onMessageReceived: msgid"+messageId);

            String conversationId = senderId;
            Log.d(TAG, "onMessageReceived: conversation id"+conversationId);

            Log.d(TAG, "onMessageReceived: 2");

            String timeStamp = jsonObject.getString("timeStamp");
            Log.d(TAG, "onMessageReceived timestamp:"+timeStamp);


            MessageData messagedata = new MessageData();
            messagedata.setMessageId(messageId);
            Log.d(TAG, "onMessageReceived: "+messageId);
            messagedata.setBody(messageBody);
            Log.d(TAG, "onMessageReceived: "+messageBody);
            messagedata.setConversionId(conversationId);
            Log.d(TAG, "onMessageReceived: "+conversationId);
            messagedata.setSenderId(senderId);
            Log.d(TAG, "onMessageReceived: "+senderId);
            messagedata.setTimeStamp(timeStamp);
            Log.d(TAG, "onMessageReceived: "+timeStamp);
           messageDatabaseHandler.insertMessage(messagedata);
        }

        catch (Exception e)
        {

            Log.e(TAG, "onMessageReceived: " + e.getMessage());
        }


    }
}
