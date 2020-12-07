package com.example.chatapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chatapp.model.bean.ChatMessageContentInfoBean;
import com.example.chatapp.model.database.ChatMessageContentDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageContentDatabaseDao {
    private ChatMessageContentDatabase chatMessageContentDatabase;
    public ChatMessageContentDatabaseDao(ChatMessageContentDatabase chatMessageContentDatabase){
        this.chatMessageContentDatabase=chatMessageContentDatabase;
    }
    //保存单条数据
    public void saveMessageContent(String sendName,String messageContent,boolean isMyContent,long messageDate){
        //得到数据库连接
        SQLiteDatabase db = chatMessageContentDatabase.getWritableDatabase();
        //执行语句
        ContentValues values=new ContentValues();
        values.put(ChatMessageContentTab.SEND_NAME,sendName);
        values.put(ChatMessageContentTab.MESSAGE_CONTENT,messageContent);
        values.put(ChatMessageContentTab.IS_MY_MESSAGE,isMyContent?1:0);
        values.put(ChatMessageContentTab.MESSAGE_DATE,messageDate);
        db.replace(ChatMessageContentTab.TAB_NAME,null,values);
    }
    //获取数据库中所有数据
    public List<ChatMessageContentInfoBean> getAllMessage(){
        List<ChatMessageContentInfoBean> chatMessageContentInfoList=new ArrayList<>();
        //获取数据库连接
        SQLiteDatabase db = chatMessageContentDatabase.getReadableDatabase();
        //执行查询语句
        String sql="select * from "+ChatMessageContentTab.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            ChatMessageContentInfoBean chatMessageContentInfo=new ChatMessageContentInfoBean();
            chatMessageContentInfo.setSendName(cursor.getString(cursor.getColumnIndex(ChatMessageContentTab.SEND_NAME)));
            chatMessageContentInfo.setMessageContent(cursor.getString(cursor.getColumnIndex(ChatMessageContentTab.MESSAGE_CONTENT)));
            chatMessageContentInfo.setMessageDate(cursor.getLong(cursor.getColumnIndex(ChatMessageContentTab.MESSAGE_DATE)));
            chatMessageContentInfo.setMyMessage((cursor.getInt(cursor.getColumnIndex(ChatMessageContentTab.IS_MY_MESSAGE)))==1?true:false);
            chatMessageContentInfoList.add(chatMessageContentInfo);
        }
        //关闭资源
        cursor.close();
        //返回数据
        return chatMessageContentInfoList;
    }
}
