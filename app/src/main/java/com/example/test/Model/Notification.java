package com.example.test.Model;

public class Notification {
    String receivedId, senderId, text, date, status;

    public Notification() {
    }

    public Notification(String receivedId, String senderId, String text, String date, String status) {
        this.receivedId = receivedId;
        this.senderId = senderId;
        this.text = text;
        this.date = date;
        this.status =status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(String receivedId) {
        this.receivedId = receivedId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
