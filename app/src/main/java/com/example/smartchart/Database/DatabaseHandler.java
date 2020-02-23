package com.example.smartchart.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.smartchart.AppConstant;
import com.example.smartchart.MessageActivity;
import com.example.smartchart.ModelClass.Chat;
import com.example.smartchart.ModelClass.MessageData;
import com.example.smartchart.ModelClass.Shedulermessagedata;
import com.example.smartchart.ModelClass.Users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.smartchart.Fragments.chats.BROADCAST_SEARCHBAR;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SmartChat1.db";

    private static final String TAG = "DatabaseHandler";
    public static final int DATABASE_VERSION = 1;

    String messageId;

    Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    public void updateMessagestatus(String deliverystaus, String messageID) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Messages.DELVERY_STATUS, deliverystaus);
        long raw = database.update(Messages.TABLE_NAME, contentValues, Messages.MESSAGE_ID + "=?", new String[]{String.valueOf(messageID)});
        Log.d(TAG, "inside updateStudent : Row : " + raw);
        Intent intent = new Intent(MessageActivity.UPDATE_MESSAGE_BRODCAST);
        intent.putExtra(AppConstant.BundleKeys.MESSAGE_ID, messageID);
        context.sendBroadcast(intent);
        Log.d(TAG, "updateMessagestatus: " + AppConstant.BundleKeys.MESSAGE_ID);
    }

    public static class User {
        public static final String TABLE_NAME = "Contacts";
        public static final String ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String USER_SUR_NAME = "user_sur_name";
        public static final String USER_MOBILE = "user_mobile";
        public static final String USER_ID = "user_id";
    }

    public static class Messages {
        public static final String TABLE_NAME = "Message";
        public static final String ID = "id";
        public static final String SENDER_ID = "sender_id";
        public static final String CONVERSION_ID = "conversion_id";
        public static final String MESSAGE_ID = "message_id";
        public static final String BODY = "body";
        public static final String TIME_STAMP = "time_stamp";
        public static final String DELVERY_STATUS = "delivery_status";

    }

    public static class Shedule {
        public static final String TABLE_NAME = "Shedule";
        public static final String ID = "id";
        public static final String SENDER_ID = "sender_id";
        public static final String MESSAGE_ID = "message_id";
        public static final String BODY = "body";
        public static final String TIME_STAMP = "time_stamp";

    }


    public static class Chats {

        public static final String TABLE_NAME = "Chat";
        public static final String CHATID = "chatid";
        public static final String CONVERSION_ID = "conversion_id";
        public static final String MESSAGE_ID = "message_id";


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: ");
        String createContactQuery = "create table " + User.TABLE_NAME + " ( " +
                User.ID + " integer primary key AUTOINCREMENT, " +
                User.USER_NAME + " text, " +
                User.USER_SUR_NAME + " text, " +
                User.USER_MOBILE + " text, " +
                User.USER_ID + " text);";
        sqLiteDatabase.execSQL(createContactQuery);
        Log.d(TAG, "onCreate: ");


        String createMessageQuery = "create table " + Messages.TABLE_NAME + " ( " +
                Messages.ID + " integer primary key AUTOINCREMENT," +
                Messages.SENDER_ID + " text, " +
                Messages.CONVERSION_ID + " text, " +
                Messages.MESSAGE_ID + " text, " +
                Messages.BODY + " text, " +
                Messages.DELVERY_STATUS + " text, " +
                Messages.TIME_STAMP + " text);";
        sqLiteDatabase.execSQL(createMessageQuery);
        Log.d(TAG, "onCreate: 123 message table" + createMessageQuery);


        String createSheduleQuery = "create table " + Shedule.TABLE_NAME + " ( " +
                Shedule.ID + " integer primary key AUTOINCREMENT," +
                Shedule.SENDER_ID + " text, " +
                Shedule.BODY + " text, " +
                Shedule.MESSAGE_ID + " text, " +
                Shedule.TIME_STAMP + " long);";
        sqLiteDatabase.execSQL(createSheduleQuery);
        Log.d(TAG, "onCreate: Shedule  table :: " + createMessageQuery);


        String createChatQuery = "create table " + Chats.TABLE_NAME + " ( " +
                Chats.CHATID + " integer primary key AUTOINCREMENT, " +
                Chats.CONVERSION_ID + " text UNIQUE, " +
                Chats.MESSAGE_ID + " text, " +
                "FOREIGN KEY(" + Chats.MESSAGE_ID + ") REFERENCES " + Messages.TABLE_NAME + "(" + Messages.MESSAGE_ID + "));";
        sqLiteDatabase.execSQL(createChatQuery);
    }

    public void insertMessage(MessageData message) {
        Log.d(TAG, "insertMessage: ");
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        messageId = message.getMessageId().toString();
        Log.d(TAG, "insertMessage: msgID :" + messageId);
        contentValues.put(Messages.MESSAGE_ID, message.getMessageId());
        contentValues.put(Messages.CONVERSION_ID, message.getConversionId());
        contentValues.put(Messages.BODY, message.getBody());
        contentValues.put(Messages.TIME_STAMP, message.getTimeStamp());
        contentValues.put(Messages.SENDER_ID, message.getSenderId());


        contentValues.put(Messages.DELVERY_STATUS, message.getDeliveryStatus());

        long row = database.insertWithOnConflict(Messages.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "Inside insertuser() -> Row : " + row);


        Intent intent = new Intent(MessageActivity.BROADCAST);
        intent.putExtra("ConversionID", message.getConversionId());

        intent.putExtra("deliverystatus", message.getDeliveryStatus());
        intent.putExtra("messageId", messageId);
        context.sendBroadcast(intent);


        //Make chatlist model data from message dat
        Chat chat = new Chat();
        chat.unreadCount = 0;
        chat.message = message;
        insertChat(chat);
    }

    public void insertChat(Chat chat) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Chats.CONVERSION_ID, chat.message.getConversionId());
        contentValues.put(Chats.MESSAGE_ID, chat.message.getMessageId());
        long row = database.insertWithOnConflict(Chats.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        Log.d(TAG, "Inside insertChat() -> Row : " + row);
    }

    public void InsertSheduleMesage(Shedulermessagedata shedulermessagedata) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Shedule.SENDER_ID, shedulermessagedata.getSenderid());
        contentValues.put(Shedule.BODY, shedulermessagedata.getBody());
        contentValues.put(Shedule.TIME_STAMP, shedulermessagedata.getTime());
        contentValues.put(Shedule.MESSAGE_ID, shedulermessagedata.getMessageid());

        long row = database.insertWithOnConflict(Shedule.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        Log.d(TAG, "Inside shedulemessage() -> Row : " + row);

    }


    public List<Users> displayUserContact() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + User.TABLE_NAME + ";";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count : " + cursor.getCount());
        List<Users> userContactList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Users user = new Users();
            Log.d(TAG, "get userdata: ");
            String id = cursor.getString(cursor.getColumnIndex(User.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(User.USER_NAME));
            String userSurName = cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME));
            String userMobile = cursor.getString(cursor.getColumnIndex(User.USER_MOBILE));
            user.setId(id);
            user.setFirstname(userName);
            user.setLastname(userSurName);
            user.setPhonenumber(userMobile);
            userContactList.add(user);
        }
        Log.d(TAG, "userlist: " + userContactList);
        cursor.close();
        return userContactList;
    }


    public List<String> displayUserID() {
        String id;
        List<String> userID = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + User.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count: " + cursor.getCount());
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(User.USER_ID));
            userID.add(id);
        }
        cursor.close();
        return userID;
    }


    public void insertUser(Users userContact) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.USER_NAME, userContact.getFirstname());
        contentValues.put(User.USER_SUR_NAME, userContact.getLastname());
        contentValues.put(User.USER_MOBILE, userContact.getPhonenumber());
        contentValues.put(User.USER_ID, userContact.getId());
        long row = database.insertWithOnConflict(User.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        Log.d(TAG, "insertUser: " + row);
    }

    public MessageData getMessageById(String messageId) {

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Messages.TABLE_NAME + " WHERE " + Messages.MESSAGE_ID + " = '" + messageId + "' ;";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count : " + cursor.getCount());
        MessageData messages = new MessageData();

        while (cursor.moveToNext()) {
            String senderId = cursor.getString(cursor.getColumnIndex(Messages.SENDER_ID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Messages.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Messages.MESSAGE_ID));
            String body = cursor.getString(cursor.getColumnIndex(Messages.BODY));
            String timeStamp = cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP));
            String deliverystatus = cursor.getString(cursor.getColumnIndex(Messages.DELVERY_STATUS));
            messages.setSenderId(senderId);
            messages.setConversionId(conversionId);
            messages.setMessageId(messageID);
            messages.setBody(body);

            messages.setDeliveryStatus(deliverystatus);
            messages.setTimeStamp(timeStamp);
        }
        cursor.close();
        return messages;
    }

    public List<MessageData> getMessageData(String conversationID) {

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Messages.TABLE_NAME + " WHERE " + Messages.CONVERSION_ID + " = '" + conversationID + "' ;";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count message : " + cursor.getCount());
        List<MessageData> messagelist = new ArrayList<>();

        while (cursor.moveToNext()) {
            MessageData message = new MessageData();
            Log.d(TAG, "getStudentData: ");
            String senderId = cursor.getString(cursor.getColumnIndex(Messages.SENDER_ID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Messages.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Messages.MESSAGE_ID));
            String body = cursor.getString(cursor.getColumnIndex(Messages.BODY));
            String timeStamp = cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP));

            String deliverystatus = cursor.getString(cursor.getColumnIndex(Messages.DELVERY_STATUS));

            Log.d(TAG, "getMessageData: " + deliverystatus);

            message.setDeliveryStatus(deliverystatus);


            message.setSenderId(senderId);
            message.setConversionId(conversionId);
            message.setMessageId(messageID);
            message.setBody(body);
            message.setTimeStamp(timeStamp);
            messagelist.add(message);
            Log.d(TAG, "getMessageData: Msg data " + body);
            Log.d(TAG, "getMessageData: messagelist" + messagelist.toString());
        }
        cursor.close();
        return messagelist;


    }


    public List<Chat> getChatList() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Chats.TABLE_NAME +
                " JOIN " + User.TABLE_NAME +
                " ON " + User.USER_ID + " = " + Chats.TABLE_NAME + "." + Chats.CONVERSION_ID +
                " JOIN " + Messages.TABLE_NAME +
                " ON " + Messages.TABLE_NAME + "." + Messages.MESSAGE_ID + " = " + Chats.TABLE_NAME + "." + Chats.MESSAGE_ID
                + " ORDER BY " + Messages.TIME_STAMP + " DESC"
                + " ;";
        Log.d(TAG, "getChatList: Query" + query);

        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count chat : " + cursor.getCount());
        List<Chat> chatList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Chat chat = new Chat();
            Log.d(TAG, "get chat data: ");
            String chatId = cursor.getString(cursor.getColumnIndex(Chats.CHATID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Chats.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Chats.MESSAGE_ID));
            chat.setChatId(Integer.parseInt(chatId));
            MessageData messageData = new MessageData();
            messageData.setTimeStamp(cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP)));
            messageData.setMessageId(messageId);
            messageData.setConversionId(conversionId);
            messageData.setBody(cursor.getString(cursor.getColumnIndex(Messages.BODY)));

            chat.message = messageData;


            Users userContacts = new Users();
            userContacts.setFirstname(cursor.getString(cursor.getColumnIndex(User.USER_NAME)));
            userContacts.setLastname(cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME)));
            userContacts.setId(cursor.getString(cursor.getColumnIndex(User.USER_ID)));
            // chat.user = userContacts;
            chat.user = userContacts;


            Log.d(TAG, "getChatData: Chat ID " + chatId);
            Log.d(TAG, "getChatData: message ID" + messageID);
            Log.d(TAG, "getChatData: conversation ID" + conversionId);


            chatList.add(chat);

            Log.d(TAG, "getChatList: " + chatList);
        }
        cursor.close();
        return chatList;
    }

    public Chat getChatByConversationId(String conversionID) {
        Log.d(TAG, "getChatByConversationId: " + conversionID);

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Chats.TABLE_NAME +
                " JOIN " + User.TABLE_NAME +
                " ON " + User.USER_ID + " = " + Chats.TABLE_NAME + "." + Chats.CONVERSION_ID +
                " JOIN " + Messages.TABLE_NAME +
                " ON " + Messages.TABLE_NAME + "." + Messages.MESSAGE_ID + " = " + Chats.TABLE_NAME + "." + Chats.MESSAGE_ID + " WHERE " + Chats.TABLE_NAME + "." + Chats.CONVERSION_ID + " ='" + conversionID + "';";
        Log.d(TAG, " Query : " + query);

        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "getChatByConversationId() ==> cursor_count: " + cursor.getCount());
        cursor.moveToFirst();
        Chat chat = new Chat();
        String chatId = cursor.getString(cursor.getColumnIndex(Chats.CHATID));
        String conversionId = cursor.getString(cursor.getColumnIndex(Chats.CONVERSION_ID));
        String messageID = cursor.getString(cursor.getColumnIndex(Chats.MESSAGE_ID));

        chat.setChatId(Integer.parseInt(chatId));
        MessageData message = new MessageData();
        message.setConversionId(conversionId);
        message.setMessageId(messageID);
        message.setBody(cursor.getString(cursor.getColumnIndex(Messages.BODY)));
        message.setTimeStamp(cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP)));
        chat.message = message;

        Users mUser = new Users();
        mUser.setFirstname(cursor.getString(cursor.getColumnIndex(User.USER_NAME)));
        mUser.setLastname(cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME)));
        mUser.setId(cursor.getString(cursor.getColumnIndex(User.USER_ID)));
        chat.user = mUser;
        cursor.close();
        return chat;

    }

    public void search(String searchquery) {
        Log.d(TAG, "search: " + searchquery);
        SQLiteDatabase database = this.getReadableDatabase();
        Log.d(TAG, "search:1 " + searchquery);
        String query = "SELECT * FROM " + Chats.TABLE_NAME +
                " JOIN " + User.TABLE_NAME +
                " ON " + User.USER_ID + " = " + Chats.TABLE_NAME + "." + Chats.CONVERSION_ID +
                " JOIN " + Messages.TABLE_NAME +
                " ON " + Messages.TABLE_NAME + "." + Messages.MESSAGE_ID + " = " + Chats.TABLE_NAME + "." + Chats.MESSAGE_ID +
                " WHERE " + User.USER_NAME + " LIKE '%" + searchquery + "%'" +
                " ;";
        Log.d(TAG, "getChatList: Query" + query);

        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count chat : " + cursor.getCount());
        List<Chat> searcher = new ArrayList<>();

        while (cursor.moveToNext()) {
            Chat chat = new Chat();
            Log.d(TAG, "get chat data: ");
            String chatId = cursor.getString(cursor.getColumnIndex(Chats.CHATID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Chats.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Chats.MESSAGE_ID));
            chat.setChatId(Integer.parseInt(chatId));
            MessageData messageData = new MessageData();
            messageData.setTimeStamp(cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP)));
            messageData.setMessageId(messageId);
            messageData.setConversionId(conversionId);
            messageData.setBody(cursor.getString(cursor.getColumnIndex(Messages.BODY)));

            chat.message = messageData;


            Users userContacts = new Users();
            String fullName = cursor.getString(cursor.getColumnIndex(User.USER_NAME)) + cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME));
            userContacts.setFirstname(cursor.getString(cursor.getColumnIndex(User.USER_NAME)));
            userContacts.setLastname(cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME)));
            userContacts.setId(cursor.getString(cursor.getColumnIndex(User.USER_ID)));
            // chat.user = userContacts;
            chat.user = userContacts;

            searcher.add(chat);


            Log.d(TAG, "getChatData: Chat ID " + chatId);
            Log.d(TAG, "getChatData: Name : " + fullName);
            Log.d(TAG, "getChatData: message ID" + messageID);
            Log.d(TAG, "getChatData: conversation ID" + conversionId);


        }

        Log.d(TAG, "listsize: " + searcher.size());
        Intent intent = new Intent(BROADCAST_SEARCHBAR);
        intent.putExtra("data", (Serializable) searcher);
        context.sendBroadcast(intent);
    }

    /*
        public void Contactsearch(String searchquery) {
            Log.d(TAG, "search: "+searchquery);
            SQLiteDatabase database = this.getReadableDatabase();
            Log.d(TAG, "search:1 "+searchquery);
            String query = "SELECT * FROM " + Chats.TABLE_NAME +
                    " JOIN " + User.TABLE_NAME +
                    " ON " + User.USER_ID + " = " + Chats.TABLE_NAME + "." + Chats.CONVERSION_ID +
                    " JOIN " + Messages.TABLE_NAME +
                    " ON " + Messages.TABLE_NAME + "." + Messages.MESSAGE_ID + " = " + Chats.TABLE_NAME + "." + Chats.MESSAGE_ID +
                    " WHERE " + User.USER_NAME + " LIKE '%" + searchquery + "%'" +
                    " ;";
            Log.d(TAG, "getChatList: Query" + query);

            Cursor cursor = database.rawQuery(query, null);
            Log.d(TAG, "Cursor Count chat : " + cursor.getCount());
            List<Chat> searcher = new ArrayList<>();

            while (cursor.moveToNext()) {
                Chat chat = new Chat();
                Log.d(TAG, "get chat data: ");
                String chatId = cursor.getString(cursor.getColumnIndex(Chats.CHATID));
                String conversionId = cursor.getString(cursor.getColumnIndex(Chats.CONVERSION_ID));
                String messageID = cursor.getString(cursor.getColumnIndex(Chats.MESSAGE_ID));
                chat.setChatId(Integer.parseInt(chatId));
                MessageData messageData = new MessageData();
                messageData.setTimeStamp(cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP)));
                messageData.setMessageId(messageId);
                messageData.setConversionId(conversionId);
                messageData.setBody(cursor.getString(cursor.getColumnIndex(Messages.BODY)));

                chat.message = messageData;


                Users userContacts = new Users();
                String fullName = cursor.getString(cursor.getColumnIndex(User.USER_NAME)) + cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME));
                userContacts.setFirstname(cursor.getString(cursor.getColumnIndex(User.USER_NAME)));
                userContacts.setLastname(cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME)));
                userContacts.setId(cursor.getString(cursor.getColumnIndex(User.USER_ID)));
                // chat.user = userContacts;
                chat.user = userContacts;

                searcher.add(chat);


                Log.d(TAG, "getChatData: Chat ID " + chatId);
                Log.d(TAG, "getChatData: Name : " + fullName);
                Log.d(TAG, "getChatData: message ID" + messageID);
                Log.d(TAG, "getChatData: conversation ID" + conversionId);




            }

            Log.d( TAG, "listsize: "+searcher.size() );
            Intent intent=new Intent(BROADCAST_SEARCHBAR);
            intent.putExtra( "data", (Serializable) searcher);
            context.sendBroadcast( intent );
        }
    */
    public List<Shedulermessagedata> getSheduleList() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Shedule.TABLE_NAME +
                " JOIN " + User.TABLE_NAME +
                " ON " + User.USER_ID + " = " + Shedule.TABLE_NAME + "." + Shedule.SENDER_ID +
                " ORDER BY " + Shedule.TIME_STAMP + " DESC"
                + " ;";
        Log.d(TAG, "getSheduleList: " + query);
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count chat : " + cursor.getCount());
        List<Shedulermessagedata> Shedlist = new ArrayList<>();

        while (cursor.moveToNext()) {
            Chat chat = new Chat();
            Log.d(TAG, "get chat data: ");
            Shedulermessagedata messageData = new Shedulermessagedata();
            Long time = cursor.getLong(cursor.getColumnIndex(Shedule.TIME_STAMP));
            String messid = cursor.getString(cursor.getColumnIndex(Shedule.MESSAGE_ID));
            String body = cursor.getString(cursor.getColumnIndex(Shedule.BODY));

            messageData.setBody(body);
            messageData.setMessageid(messid);
            messageData.setTime(time);


            /* chat.shedulermessagedata = messageData;*/


            Users userContacts = new Users();
            String fullName = cursor.getString(cursor.getColumnIndex(User.USER_NAME)) + cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME));


            String name =cursor.getString(cursor.getColumnIndex(User.USER_NAME));
             String lastname =(cursor.getString(cursor.getColumnIndex(User.USER_SUR_NAME)));
            String id =cursor.getString(cursor.getColumnIndex(User.USER_ID));
            Users users = new Users();
            users.setId(id);
            users.setFirstname(name);
            users.setLastname(lastname);

            messageData.setUsers(users);

            Log.d(TAG, "getSheduleList: -> messid " + messid);
            Log.d(TAG, "getSheduleList: -> id " + id);
            Log.d(TAG, "getSheduleList: -> fullname : " + fullName);
            Log.d(TAG, "getSheduleList: -> time :" + time);

            Log.d(TAG, "getSheduleList: -> bodvy :" + body);

            Shedlist.add(messageData);
            //Shedlist.add(userContacts);



            Log.d(TAG, "getChatList: from shedlist  " + Shedlist);
        }
        cursor.close();
        return Shedlist;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {




    }
}
