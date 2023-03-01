package com.antonymilian.socialmediafya.models;

public class Message {

    private String id;
    private String idSender;
    private String idReciver;
    private String idChat;
    private String message;
    private long timestamp;
    private boolean viewed;

    public Message(){
    }

    public Message(String id, String idSender, String idReciver, String idChat, String message, long timestamp, boolean viewed) {
        this.id = id;
        this.idSender = idSender;
        this.idReciver = idReciver;
        this.idChat = idChat;
        this.message = message;
        this.timestamp = timestamp;
        this.viewed = viewed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReciver() {
        return idReciver;
    }

    public void setIdReciver(String idReciver) {
        this.idReciver = idReciver;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
