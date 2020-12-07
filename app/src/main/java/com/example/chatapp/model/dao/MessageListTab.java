package com.example.chatapp.model.dao;

public class MessageListTab {
    public static final String TAB_NAME="tab_message_list";

    public static final String NAME="name";
    public static final String MESSAGE_CONTENT="message_content";
    public static final String IS_MY_MESSAGE="is_my_message";
    public static final String MESSAGE_DATE="message_date";

    public static final String CREATE_TAB="create table "
            +TAB_NAME+ " ("
            +NAME+ " text primary key,"
            +MESSAGE_CONTENT+ " text,"
            +MESSAGE_DATE+ " integer,"
            +IS_MY_MESSAGE+" integer);";
}
