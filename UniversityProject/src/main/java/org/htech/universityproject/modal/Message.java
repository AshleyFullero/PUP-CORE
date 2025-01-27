package org.htech.universityproject.modal;

import org.htech.universityproject.dao.MessageDao;

import java.sql.Timestamp;
import java.util.List;

public class Message {

    private int receiverId;
    private String content;

    private int senderId;
    private String senderUsername;
    private Timestamp timestamp;

    byte[] profilePicBytes;

    public Message(int senderId, int receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public Message(int receiverId, String content, Timestamp timestamp, String senderUsername, byte[] profilePicBytes) {
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
        this.profilePicBytes = profilePicBytes;
    }

    public Message(int receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
    }

    public Message(int senderId, String content, Timestamp timestamp, String senderUsername) {
        this.senderId = senderId;
        this.content = content;
        this.senderUsername = senderUsername;
        this.timestamp = timestamp;
    }

    public boolean sendMessage(Message message){
        MessageDao messageDao = new MessageDao();
        return messageDao.sendMessage(message);
    }

    public void sendMessage(List<String> recipients){

    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getProfilePicBytes() {
        return profilePicBytes;
    }

    public void setProfilePicBytes(byte[] profilePicBytes) {
        this.profilePicBytes = profilePicBytes;
    }
}
