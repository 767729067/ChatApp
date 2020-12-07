package com.example.chatapp.model.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.model.database.UserInfoDatabase;
//本地数据库的操作类
public class UserInfoDatabaseAao {
    private UserInfoDatabase userInfoDatabase;
    public UserInfoDatabaseAao(Context context) {
        userInfoDatabase=new UserInfoDatabase(context);
    }
    public void addUserInfo(UserInfoBean userInfoBean){
        //获取数据库对象
        SQLiteDatabase db=userInfoDatabase.getWritableDatabase();
        //执行操作
        ContentValues values=new ContentValues();
        //将数据添加到values中封装
        values.put(UserInfoTable.USER_HXID,userInfoBean.getHxid());
        values.put(UserInfoTable.USER_NAME,userInfoBean.getName());
        values.put(UserInfoTable.USER_NICK,userInfoBean.getNick());
        values.put(UserInfoTable.USER_PHOTO,userInfoBean.getPhoto());
        //添加表数据
        db.replace(UserInfoTable.TAB_NAME,null,values);
    }
    //根据id获得用户的信息
    public UserInfoBean getUserInfoByHxid(String hxid){
        //获取数据库对象
        SQLiteDatabase db= userInfoDatabase.getReadableDatabase();
        //执行查询语句
        String sql="select * from "+UserInfoTable.TAB_NAME+" where "+UserInfoTable.USER_HXID+" =?";
        Cursor cursor=db.rawQuery(sql,new String[]{hxid});
        UserInfoBean userInfoBean=null;
        if(cursor.moveToNext()){
            userInfoBean=new UserInfoBean();
            userInfoBean.setHxid(cursor.getString(cursor.getColumnIndex(UserInfoTable.USER_HXID)));
            userInfoBean.setName(cursor.getString(cursor.getColumnIndex(UserInfoTable.USER_NAME)));
            userInfoBean.setNick(cursor.getString(cursor.getColumnIndex(UserInfoTable.USER_NICK)));
            userInfoBean.setPhoto(cursor.getString(cursor.getColumnIndex(UserInfoTable.USER_PHOTO)));
        }

        //关闭资源
        cursor.close();
        //返回数据
        return userInfoBean;
    }
}
