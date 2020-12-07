package com.example.chatapp.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.chatapp.model.dao.UserInfoTable;

public class UserInfoDatabase extends SQLiteOpenHelper {
    public UserInfoDatabase(@Nullable Context context) {
        super(context, "userInfo.db", null, 1);
    }
    //创建数据库的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建账号信息的表
        db.execSQL(UserInfoTable.CREATE_TAB);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
