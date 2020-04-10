package com.example.smartchart;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.service.voice.AlwaysOnHotwordDetector;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.example.smartchart.Adapter.MessageAdapter;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.Retrofit.Data;
import com.example.smartchart.Retrofit.FCMAPI;
import com.example.smartchart.Retrofit.MessageEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.smartchart.AppConstant.DELIVERY_STATUS_SENT;
import static com.example.smartchart.AppConstant.LOGGED_IN_USER_ID;
import static com.example.smartchart.AppConstant.PENDING_MESSAGE_SENDTO_DATABASE;


public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    public static final String BROADCAST = "broadcast RECEIVER";
    public static final String UPDATE_MESSAGE_BRODCAST = "update_message_broadcast";
    public static final String MESSAGEID_STATUS_UPDATE = "messageID status update";



    String reciverID, name, mobile, txtmessage, senderID, messageID, profileImage, status, number;

    private long timeStamp;
    String s;
    FloatingActionButton btnSend;
    TextView textView, textViewStatus;
    CircularImageView imageView;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;

    SharedPreferences preferences;
    Toolbar mtoolbar;

    List<MessageData> messageDataList;
    EditText message;

    FloatingActionButton scrolldownbutton;

    Context context;

    DatabaseHandler messageDatabaseHandler;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_message);
        btnSend = findViewById(R.id.send);
        scrolldownbutton = findViewById(R.id.scroll_down_button);
        textViewStatus = findViewById(R.id.status);
        mtoolbar = findViewById(R.id.msgtool_bar);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.Image);
        message = findViewById(R.id.txtmessage);
        recyclerView = findViewById(R.id.recycler_view);


        context = this;
        Intent intent = getIntent();
        reciverID = intent.getStringExtra("ReciverUserID");
        profileImage =intent.getStringExtra("dp");
        Log.d(TAG, "onCreate reciver id :  " + reciverID);
        mobile = intent.getStringExtra("number");
        name = getIntent().getStringExtra("name");
        status = getIntent().getStringExtra("status");
        Log.d(TAG, "onCreate: status  : " + status);
        Log.d(TAG, "onCreate: name = " + name);
        textView.setText(name);
        textViewStatus.setText(status);

        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getRandomColor();
        s = name.substring(0, 1);
        TextDrawable drawable1 = TextDrawable.builder().buildRound(s, color);


        Drawable d = new BitmapDrawable(drawableToBitmap(drawable1));

        Glide.with(context)
                .load(profileImage)
                .placeholder(d)
                .into(imageView);



        preferences = getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        senderID = preferences.getString(LOGGED_IN_USER_ID, "");

        //Broadcastadb
        IntentFilter intentFilter = new IntentFilter(BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);

        IntentFilter intentFilter2 = new IntentFilter(MESSAGEID_STATUS_UPDATE);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, intentFilter2);

        //INTERNET AUTO DETETCTED
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);



        messageDatabaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);

        IntentFilter intentFilter1 = new IntentFilter(UPDATE_MESSAGE_BRODCAST);
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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx == 0 && dy == 0) {
                    scrolldownbutton.hide();
                } else if (dy < 0) {
                    scrolldownbutton.show();
                } else if (dy > 0) {
                    scrolldownbutton.hide();
                }
            }
        });

        scrolldownbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

            }
        });
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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
        unregisterReceiver(receiver);
    }


    private BroadcastReceiver UpdateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if ((bundle != null)) {

                String messageId = bundle.getString(AppConstant.BundleKeys.MESSAGE_ID);

                DatabaseHandler updatedatabasehander = new DatabaseHandler(context);
                MessageData message = updatedatabasehander.getMessageById(messageId);
                Log.d(TAG, "onReceive: udate" + message);
                messageAdapter.updateMessageToAdapter(message);

            }


        }
    };

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String MessageIDStatus = null;
            Bundle bundle = intent.getExtras();
            if ((bundle != null)) {
                MessageIDStatus = bundle.getString("Messagestatus");

            }
            isNetworkAvailable(context, MessageIDStatus);
        }

        public void isNetworkAvailable(Context context, String messageID) {

            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();

                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {

                                if (preferences.contains(PENDING_MESSAGE_SENDTO_DATABASE)) {
                                    String result = preferences.getString(AppConstant.PENDING_MESSAGE_SENDTO_DATABASE, null);
                                    Gson gson = new Gson();
                                    MessageData[] favoriteItems = gson.fromJson(result, MessageData[].class);
                                    messageDataList = Arrays.asList(favoriteItems);
                                    Log.d(TAG, "onCreate+Messageid: " + messageDataList);
                                    for (MessageData data : messageDataList) {
                                        Log.d(TAG, "isNetworkAvailableb: " + data.getMessageId());
                                        messageDatabaseHandler.updateMessagestatus(DELIVERY_STATUS_SENT, data.getMessageId());
                                    }
                                }
                                messageDatabaseHandler.updateMessagestatus(DELIVERY_STATUS_SENT, messageID);

                            }
                            return;
                        }
                    }
                }
            }

            isConnected = false;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.d(TAG, "onReceive: ");

                String msgId = bundle.getString("messageId");
                String delive = bundle.getString("deliverystatus");
                Log.d(TAG, "onReceive broadcast for delivery status " + delive);
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
        if (txtmessage.isEmpty()) {
            message.setError("required");
            message.requestFocus();
            return;
        }

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


                messageDatabaseHandler.Pendingmessagesupdate();

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        message.setText("");
    }

    public void saveMessage(String senderID, String reciverID, String messageID, String body, String timeStamp, String deliverystaus) {

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

        Log.d(TAG, "saveMessage: delivery status  " + deliverystaus);

        messageDatabaseHandler.insertMessage(message);
    }

    public void status(String status) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String base64id = sharedPreferences.getString(LOGGED_IN_USER_ID, null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert base64id != null;
        DatabaseReference myRef = database.getReference("Users").child(base64id.concat("=="));

        Log.d(TAG, "status: " + status + myRef);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        myRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();


        status("online");
    }
}
