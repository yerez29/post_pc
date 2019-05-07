package com.example.firststeptoselfchat;

import java.util.Comparator;

public class Message {
    private String msgContent;
    private String timeStamp;
    private String msgKey;

    public Message()
    {

    }

    public Message(String content, String time, String key)
    {
        msgContent = content;
        timeStamp = time;
        msgKey = key;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMsgKey() {
        return msgKey;
    }
}
