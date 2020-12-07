package com.example.chatapp.model.dao;
//邀请信息的表
public class InviteTable {
    public static final String TAB_NAME="tab_invite";

    public static final String USER_HXID="user_hxid";//用户的环信id
    public static final String USER_NAME="user_name";//用户的名称

    public static final String GROUP_NAME="group_name";//群的名称
    public static final String GROUP_HXID="group_hxid";//群的环信id

    public static final String REASON="reason";//邀请的原因
    public static final String STATUS="status";//邀请的状态

    public static final String CREATE_TAB="create table "
            + TAB_NAME + "("
            + USER_HXID +" text primary key,"
            + USER_NAME +" text,"
            + GROUP_HXID +" text,"
            + GROUP_NAME +" text,"
            + REASON +" text,"
            + STATUS +" integer);";

}
