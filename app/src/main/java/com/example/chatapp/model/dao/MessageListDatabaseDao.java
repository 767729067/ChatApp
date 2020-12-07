package com.example.chatapp.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chatapp.model.bean.MessageListInfoBean;
import com.example.chatapp.model.database.MessageListDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageListDatabaseDao {
    private Context context;
    private MessageListDatabase messageListDatabase;
    private List<MessageListInfoBean> MessageListInfoList;
    public MessageListDatabaseDao(Context context,String name){
        this.context=context;
        messageListDatabase=new MessageListDatabase(context,name);
    }
    //保存到数据库
    public void saveMessageList(String name,String messageContent,long messagedate,boolean isMyMessage){
        //获取数据库对象
        SQLiteDatabase db = messageListDatabase.getWritableDatabase();
        //执行语句
        ContentValues values=new ContentValues();
        values.put(MessageListTab.NAME,name);
        values.put(MessageListTab.MESSAGE_CONTENT,messageContent);
        values.put(MessageListTab.MESSAGE_DATE,messagedate);
        values.put(MessageListTab.IS_MY_MESSAGE,isMyMessage?1:0);
        db.replace(MessageListTab.TAB_NAME,null,values);
    }
    //获取数据库的信息
    public List<MessageListInfoBean> getMessageList(){
        MessageListInfoList=new ArrayList<>();
        //创建数据库连接
        SQLiteDatabase db = messageListDatabase.getReadableDatabase();
        //执行查询语句
        String sql="select * from "+MessageListTab.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            MessageListInfoBean messageInfo=new MessageListInfoBean();
            messageInfo.setName(cursor.getString(cursor.getColumnIndex(MessageListTab.NAME)));
            messageInfo.setMessageContent(cursor.getString(cursor.getColumnIndex(MessageListTab.MESSAGE_CONTENT)));
            messageInfo.setMessageDate(cursor.getLong(cursor.getColumnIndex(MessageListTab.MESSAGE_DATE)));
            messageInfo.setMyMessage((cursor.getInt(cursor.getColumnIndex(MessageListTab.IS_MY_MESSAGE)))==1?true:false);
            MessageListInfoList.add(messageInfo);
        }
        //关闭资源
        cursor.close();
        return MessageListInfoList;
    }
    //通过名称修改单条数据
    public void updateDataByName(String name,Boolean isMyMessage){
        //创建数据库连接
        SQLiteDatabase db = messageListDatabase.getWritableDatabase();
        //执行修改语句
        ContentValues values=new ContentValues();
        values.put(MessageListTab.IS_MY_MESSAGE,isMyMessage?1:0);
        db.update(MessageListTab.TAB_NAME,values,MessageListTab.NAME+"=?",new String[]{name});
    }
    //通过name删除数据
    public void removeByName(String name){
        //获取数据库连接
        SQLiteDatabase db = messageListDatabase.getWritableDatabase();
        //执行语句
        db.delete(MessageListTab.TAB_NAME,MessageListTab.NAME+"=?",new String[]{name});
    }
}
