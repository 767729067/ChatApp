package com.example.chatapp.model.bean;

public class MessageListInfoBean {
    private String name;
    private String messageContent;
    private boolean isMyMessage;
    private long messageDate;

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(long data) {
        this.messageDate = data;
    }

    @Override
    public String toString() {
        return "MessageListInfoBean{" +
                "name='" + name + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", isMyMessage=" + isMyMessage +
                ", messageDate=" + messageDate +
                '}';
    }
}
