package com.example.chatapp.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.chatapp.model.dao.ChatMessageContentTab;

public class ChatMessageContentDatabase extends SQLiteOpenHelper {
    public ChatMessageContentDatabase(@Nullable Context context, @Nullable String name) {
        super(context, name+"_message_content", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ChatMessageContentTab.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
