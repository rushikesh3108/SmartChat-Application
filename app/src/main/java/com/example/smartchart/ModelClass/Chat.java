package com.example.smartchart.ModelClass;

import java.io.Serializable;

public class Chat implements Serializable {

    public int chatId;

    public int unreadCount;

    public MessageData message;

    public Users user;



    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Users getUser() {
        return user;
    }

    public MessageData getMessage() {
        return message;
    }

    public void setMessage(MessageData message) {
        this.message = message;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
