package com.example.smartchart.ModelClass;

public class Shedulermessagedata {

    String id ,body,senderid;
long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getSenderid() {
        return senderid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }


}
