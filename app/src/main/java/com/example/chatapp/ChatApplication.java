package com.example.chatapp;

import android.app.Application;
import android.content.Context;

import com.example.chatapp.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

public class ChatApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化EMClient
        EMOptions options=new EMOptions();
        options.setAcceptInvitationAlways(false);//不自动接受添加的好友
        options.setAutoAcceptGroupInvitation(false);//设置需要同意后加入群聊
        EMClient.getInstance().init(this,options);//初始化sdk
        //初始化数据模型
        Model.getInstance().init(this);
        //初始化全局上下文
        context=this;
    }
    public static Context getGlobalApplication(){
        return context;
    }
}
