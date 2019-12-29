package com.example.smartchart.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartchart.AppConstant;
import com.example.smartchart.Database.DatabaseHandler;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    MessageData chat;
    private static final String TAG = "MessageAdapter";

    private Context mContext;
    public int globalposition;
    DatabaseHandler databaseHandler;

    private List<MessageData> mChat;

    public MessageAdapter(Context mContext, List<MessageData> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sender_side_message, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.receiver_side_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    public void Delivery(String delivery) {
        // mChat.add(delivery);
        Log.d(TAG, "Delivery: " + delivery);
/*
        mChat.add(globalposition, );
*/


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         chat = mChat.get(position);
        globalposition = position;

        holder.show_message.setText(chat.getBody().replace("_", " "));

        Long time = Long.parseLong(chat.getTimeStamp());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date result = new Date(time);

        holder.txtTimeStamp.setText(dateFormat.format(result));

       String str= chat.getDeliveryStatus();
        Log.d(TAG, "onBindViewHolder: stringgggg "+str);
        Log.d(TAG, "onBindViewHolder:  delivery status : "+chat.getDeliveryStatus());
        if ("pending".equalsIgnoreCase(chat.getDeliveryStatus())){

            holder.imgsend.setImageResource(R.drawable.pendingstatus);

        }
        else {
            holder.imgsend.setImageResource(R.drawable.successstatus);

        }

    }

    public void addMessageToAdapter(MessageData message) {
        mChat.add(message);
        this.notifyItemInserted(mChat.size() - 1);
    }
    public void updateMessageToAdapter(MessageData messageData){
        int indexToUpdate = -1;
        for (MessageData message : mChat) {
            if (message.getMessageId().equalsIgnoreCase(messageData.getMessageId())) {
                indexToUpdate = mChat.indexOf(message);
                break;
            }
        }
        if (indexToUpdate != -1) {
            mChat.set(indexToUpdate, messageData);
            notifyItemChanged(indexToUpdate);
        }
    }

    @Override
    public int getItemViewType(int position) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        String SenderID = sharedPreferences.getString(AppConstant.LOGGED_IN_USER_ID, "");
        Log.d(TAG, "getItemViewType 1: " + mChat.get(position).getSenderId());
        Log.d(TAG, "getItemViewType 2: " + SenderID);
        if (mChat.get(position).getSenderId().equals(SenderID)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        TextView txtTimeStamp;
       ImageView imgsend;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeStamp = itemView.findViewById(R.id.timestamp);
            imgsend = itemView.findViewById(R.id.delivery);


            show_message = itemView.findViewById(R.id.show_message);
        }
    }
}
