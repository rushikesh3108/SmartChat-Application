package com.example.smartchart;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchart.Adapter.MessageAdapter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.Retrofit.Data;
import com.example.smartchart.Retrofit.FCMAPI;
import com.example.smartchart.Retrofit.MessageEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    public static final String BROADCAST = "broadcast RECEIVER";
    public static final String UPDATE_MESSAGE_BRODCAST = "update_message_broadcast";

    String reciverID, name, mobile, txtmessage, senderID, messageID;

    private long timeStamp;

    FloatingActionButton btnSend;
    TextView textView;
    ImageView imageView;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;


    Toolbar mtoolbar;

    List<MessageData> messageDataList;
    EditText message;


    Context context;

    DatabaseHandler messageDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_message);
        btnSend = findViewById(R.id.send);
        message = findViewById(R.id.txtmessage);
        mtoolbar = findViewById(R.id.msgtool_bar);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.Image);

        recyclerView = findViewById(R.id.recycler_view);


        context = this;
        Intent intent = getIntent();
        reciverID = intent.getStringExtra("ReciverUserID");
        //reciverID=intent.getStringExtra("ReceiverUserID1");
        Log.d(TAG, "onCreate reciver id :  " + reciverID);
        mobile = intent.getStringExtra("number");
        Log.d(TAG, "onCreate mobileno : ");
        name = getIntent().getStringExtra("name");
        textView.setText(name);


        SharedPreferences preferences = getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        senderID = preferences.getString(AppConstant.LOGGED_IN_USER_ID, "");

        //Broadcastadb
        IntentFilter intentFilter = new IntentFilter(BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);

        messageDatabaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);

        IntentFilter intentFilter1=new IntentFilter(UPDATE_MESSAGE_BRODCAST);
        registerReceiver(UpdateBroadcastReceiver, intentFilter1);

        messageDataList = new ArrayList<MessageData>();

        //Setup recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        messageDataList = messageDatabaseHandler.getMessageData(reciverID);
        messageAdapter = new MessageAdapter(context, messageDataList);
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        recyclerView.setAdapter(messageAdapter);

        Drawable drawable = btnSend.getDrawable();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        //setupToolBar
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(UpdateBroadcastReceiver);
    }


    private BroadcastReceiver UpdateBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle= intent.getExtras();
            if ((bundle!= null)){

                String messageId =bundle.getString(AppConstant.BundleKeys.MESSAGE_ID);

                DatabaseHandler updatedatabasehander = new DatabaseHandler(context);
                MessageData message = updatedatabasehander.getMessageById(messageId);
                Log.d(TAG, "onReceive: udate"+message);
                messageAdapter.updateMessageToAdapter(message);

            }


        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.d(TAG, "onReceive: ");

                String msgId = bundle.getString("messageId");
                String delive= bundle.getString("deliverystatus");
                Log.d(TAG, "onReceive broadcast for delivery status "+delive);
                Log.d(TAG, "on BroadCast Receive: " + msgId);


                Log.d(TAG, "onReceive: senderID " + senderID);

                //Get message data from database using messageId
                DatabaseHandler handler = new DatabaseHandler(context);
                MessageData message = handler.getMessageById(msgId);

                //Log.d(TAG, "onReceive: conversationId "+message.getConversionId());
                messageAdapter.addMessageToAdapter(message);


            }

        }
    };


    public void sendMessage(View view) {
        timeStamp = System.currentTimeMillis();

        String time = String.valueOf(timeStamp);


        txtmessage = message.getText().toString().replace(" ", "_");

        messageID = Utils.generateUniqueMessageId();


        recyclerView.smoothScrollToPosition(message.getBottom());
        Log.d(TAG, "sendMessage: senderID" + senderID);
        Log.d(TAG, "sendMessage: recieverID" + reciverID);
        Log.d(TAG, "sendMessage: name" + name);
        Retrofit retrofit = BaseApplication.getRetrofitInstance();
        FCMAPI api = retrofit.create(FCMAPI.class);
        MessageEntity messageEntity = new MessageEntity();
        Data data = new Data();
        data.senderId = senderID;
        data.receiverId = reciverID;
        data.body = txtmessage;
        data.messageId = messageID;
        data.timeStamp = timeStamp;
        messageEntity.data = data;
        messageEntity.to = "/topics/" + reciverID;
        Log.d(TAG, "sendMessage: RecevierId : " + reciverID);

        saveMessage(senderID, reciverID, messageID, txtmessage, time, AppConstant.DELIVERY_STATUS_PENDING);

        api.sendMessage(messageEntity).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: MessageEntity send successfully");
                  //  messageData.setDeliveryStatus("sucessfull");

                    Log.d(TAG, "onResponse: 12");
                    messageDatabaseHandler.updateMessagestatus(AppConstant.DELIVERY_STATUS_SENT, messageID);
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        message.setText("");
    }

    public void saveMessage(String senderID, String reciverID, String messageID, String body, String timeStamp ,String deliverystaus) {

        Log.d(TAG, "saveMessage: ");
        Log.d(TAG, "saveMessage: sernderID " + senderID);
        Log.d(TAG, "saveMessage: reciverID " + reciverID);
        Log.d(TAG, "saveMessage: msgID " + messageID);
        Log.d(TAG, "saveMessage: msgBody " + body);
        Log.d(TAG, "saveMessage: timeStamp" + timeStamp);

        MessageData message = new MessageData();

        message.setDeliveryStatus(deliverystaus);


        message.setMessageId(messageID);
        message.setConversionId(reciverID);
        message.setBody(body);
        message.setTimeStamp(timeStamp);
        message.setSenderId(senderID);
        Log.d(TAG, "MessageEntity Class: " + message.toString());

        Log.d(TAG, "saveMessage: delivery status  "+deliverystaus);

        messageDatabaseHandler.insertMessage(message);
    }
}
