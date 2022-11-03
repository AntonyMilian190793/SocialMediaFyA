package com.antonymilian.socialmediafya.models;

public class User {

    private String id;
    private String email;
    private String username;
    private String phone;
    private long timestamp;


    public User(){

    }

    public User(String id, String email, String username, String phone, long timestamp) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
