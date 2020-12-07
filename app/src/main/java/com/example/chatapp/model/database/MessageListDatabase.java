package com.example.chatapp.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.chatapp.model.dao.MessageListTab;

public class MessageListDatabase extends SQLiteOpenHelper {
    public MessageListDatabase(@Nullable Context context,String name) {
        super(context, name+"MessageList", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageListTab.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
