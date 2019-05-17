package com.example.firststeptoselfchat;

import java.util.Comparator;

public class Message {
    private String msgContent;
    private String timeStamp;
    private String msgKey;
    private String deviceModel;

    public Message()
    {

    }

    public Message(String content, String time, String key, String model)
    {
        msgContent = content;
        timeStamp = time;
        msgKey = key;
        deviceModel = model;
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

    public String getDeviceModel() {
        return deviceModel;
    }
}
