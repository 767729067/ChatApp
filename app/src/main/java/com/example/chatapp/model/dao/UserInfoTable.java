package com.example.chatapp.model.dao;

public class UserInfoTable {
    //表名
    public static final String TAB_NAME="userInfoTab";
    //列名
    public static final String USER_NAME="name";
    public static final String USER_HXID="hxid";
    public static final String USER_NICK="nick";
    public static final String USER_PHOTO="photo";
    //sql语句
    public static final String CREATE_TAB="create table "
            + TAB_NAME  +"("
            + USER_HXID + " text primary key,"
            + USER_NAME + " text,"
            + USER_NICK + " text,"
            + USER_PHOTO + " text);";
    //create table userInfoTab
}
