package com.example.chatapp.model.database;

import android.content.Context;

import com.example.chatapp.model.dao.ContactDatabaseDao;
import com.example.chatapp.model.dao.InviteDatabaseDao;

//邀请信息表和联系人的操作类的管理类
public class DBManager {
    private DBHelper dbHelper;
    private ContactDatabaseDao contactDatabaseDao;
    private InviteDatabaseDao inviteDatabaseDao;
    public DBManager(Context context,String name) {
        //创建数据库
        dbHelper = new DBHelper(context, name);
        //创建数据库中两张表的操作类
        contactDatabaseDao = new ContactDatabaseDao(dbHelper);//联系人操作类
        inviteDatabaseDao = new InviteDatabaseDao(dbHelper);//邀请信息操作类
    }
    //获取联系人表的操作类对象
    public ContactDatabaseDao getContactDatabaseDao() {
        return contactDatabaseDao;
    }
    //获取邀请信息表的操作类对象
    public InviteDatabaseDao getInviteDatabaseDao() {
        return inviteDatabaseDao;
    }
    //关闭数据库的方法
    public void close() {
        dbHelper.close();
    }
}
