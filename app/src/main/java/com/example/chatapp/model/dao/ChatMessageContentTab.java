package com.example.chatapp.model.dao;

public class ChatMessageContentTab {
    public static final String TAB_NAME="chat_content";
    public static final String TAB_ID="id";
    public static final String SEND_NAME="send_name";
    public static final String MESSAGE_CONTENT="message_content";
    public static final String MESSAGE_DATE="message_date";
    public static final String IS_MY_MESSAGE="is_my_message";

    public static final String CREATE_TAB="create table "
            +TAB_NAME+" ("
            +TAB_ID+" integer primary key autoincrement,"
            +SEND_NAME+" text,"
            +MESSAGE_CONTENT+" text,"
            +MESSAGE_DATE+" integer,"
            +IS_MY_MESSAGE+" integer);";
}
