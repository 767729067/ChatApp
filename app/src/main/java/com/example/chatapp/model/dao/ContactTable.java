package com.example.chatapp.model.dao;
//联系人的信息
public class ContactTable {
    public static final String TAB_NAME="tab_contact";
    //用户的信息
    public static final String HXID="hxid";
    public static final String NAME="name";
    public static final String NICK="nick";
    public static final String PHOTO="photo";
    //是否是联系人
    public static final String IS_CONTACT="is_contact";
    //建表语句
    public static final String CREATE_TAB="create table "
            +TAB_NAME+"("
            +HXID+" text primary key,"
            +NAME+" text,"
            +NICK+" text,"
            +PHOTO+" text,"
            +IS_CONTACT+" integer);";
}
