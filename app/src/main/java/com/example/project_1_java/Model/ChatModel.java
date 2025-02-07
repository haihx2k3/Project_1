package com.example.project_1_java.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;

import java.util.List;

public class ChatModel implements Serializable {
    private String chatRoom;
    private List<String> userId;
    private Object timeStamp;
    private String lastMessageSenderId;
    private String lastMessage;

    public ChatModel() {}

    public ChatModel(String chatRoom, List<String> userId, Object timeStamp, String lastMessageSenderId) {
        this.chatRoom = chatRoom;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }
    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessageId) {
        this.lastMessage = lastMessageId;
    }
}
