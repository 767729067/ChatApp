package com.example.chatapp.model;

import android.content.Context;

import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.model.dao.ChatMessageContentDatabaseDao;
import com.example.chatapp.model.dao.MessageListDatabaseDao;
import com.example.chatapp.model.dao.UserInfoDatabaseAao;
import com.example.chatapp.model.database.ChatMessageContentDatabase;
import com.example.chatapp.model.database.DBManager;
import com.example.chatapp.model.database.MessageListDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//数据模型层
public class Model {
    private Context context;
    private UserInfoDatabaseAao userInfoDatabaseAao;
    private ExecutorService executors=Executors.newCachedThreadPool();
    private DBManager dbManager;
    private MessageListDatabaseDao mldd;
    private ChatMessageContentDatabaseDao chatMessageContentDatabaseDao;
    private ChatMessageContentDatabase chatMessageContentDatabase;
    //单例模式
    private static Model model=new Model();
    private Model(){

    }
    //获取单例对象
    public static Model getInstance(){
        return model;
    }
    public void init(Context context){
        this.context=context;

        //创建用户数据库的操作类对象
        userInfoDatabaseAao=new UserInfoDatabaseAao(context);
        //开启全局监听
        EventListener eventListener=new EventListener(context);
    }
    //获取线程池
    public ExecutorService getThreadPool(){
        return executors;
    }
    //用户登录成功后的处理方法
    public void loginSuccess(UserInfoBean userInfo){
        if(userInfo==null){
            return;
        }
        if(dbManager!=null){
            //如果存在就先关闭掉
            dbManager.close();
        }
        //登录成功后创建表的管理类对象
        dbManager = new DBManager(context, userInfo.getName());

        //登录成功创建打开消息数据库
        mldd=new MessageListDatabaseDao(context,userInfo.getName());
    }
    public MessageListDatabaseDao getMessageListDatabaseDao(){
        return mldd;
    }
    //打开聊天页面初始化数据库
    public void initChatMessageContentDatabase(String name){
        chatMessageContentDatabase=new ChatMessageContentDatabase(context,name);
    }
    //初始化聊天记录的操作类对象
    public ChatMessageContentDatabaseDao getChatMessageContentDatabaseDao(){
        chatMessageContentDatabaseDao=new ChatMessageContentDatabaseDao(chatMessageContentDatabase);
        return chatMessageContentDatabaseDao;
    }
    //获取管理者的方法
    public DBManager getDbManager(){
        return dbManager;
    }


    public UserInfoDatabaseAao getUserInfoDatabaseAao(){
        return userInfoDatabaseAao;
    }
}
