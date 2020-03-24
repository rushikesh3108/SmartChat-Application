package com.example.smartchart.Retrofit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.MessageActivity;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class FcmService extends FirebaseMessagingService {

    private static final String TAG = "FcmService";
    String MessaageBody,Title;

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
        //notification channelid set on contactfragment.
        PendingIntent pendingIntent=PendingIntent.getActivity( this,0,new Intent(this, MessageActivity.class ),0 );
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"mynotification")
                .setContentTitle(Title)
                .setContentText(MessaageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound( RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon( R.mipmap.ic_launcher)
                .setContentIntent( pendingIntent )
                .setDefaults( Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        Log.d( TAG, "onMessageReceived: "+MessaageBody );
        Log.d( TAG, "onMessageReceived: "+Title );

        NotificationManager notificationManager =
                ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE);

        notificationManager.notify(99, notificationBuilder.build());



    }
}
