package com.example.chatapp.model.bean;

public class ChatMessageContentInfoBean {
    private String sendName;//发送者的姓名
    private String messageContent;//发送的内容
    private long messageDate;//发送消息的时间
    private boolean isMyMessage;//是不是自己发的消息

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
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

    public void setMessageDate(long messageDate) {
        this.messageDate = messageDate;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }

    @Override
    public String toString() {
        return "ChatMessageContentInfoBean{" +
                "sendName='" + sendName + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageDate=" + messageDate +
                ", isMyMessage=" + isMyMessage +
                '}';
    }
}
