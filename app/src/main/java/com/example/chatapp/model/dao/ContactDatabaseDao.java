package com.example.chatapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.model.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

//联系人表的操作类
public class ContactDatabaseDao {
    private DBHelper dbHelper;
    public ContactDatabaseDao(DBHelper dbHelper) {
        this.dbHelper=dbHelper;
    }
    //获取所有联系人信息
    public List<UserInfoBean> getContacts(){
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行操作语句
        String sql="select * from "+ ContactTable.TAB_NAME+" where "+ ContactTable.IS_CONTACT+"=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfoBean> userInfoBeanList=new ArrayList<>();
        while (cursor.moveToNext()){
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.HXID)));
            userInfoBean.setName(cursor.getString(cursor.getColumnIndex(ContactTable.NAME)));
            userInfoBean.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.NICK)));
            userInfoBean.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.PHOTO)));
            userInfoBeanList.add(userInfoBean);
        }
        //关闭资源
        cursor.close();
        //返回数据
        return userInfoBeanList;
    }
    //通过环信id获取单个联系人
    public UserInfoBean getContactByHxid(String hxid){
        if(hxid==null){
            return null;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行操作语句
        String sql="select * from "+ ContactTable.TAB_NAME+" where "+ ContactTable.HXID+"=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxid});
        UserInfoBean userInfoBean =null;
        if(cursor.moveToNext()){
            userInfoBean = new UserInfoBean();
            userInfoBean.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.HXID)));
            userInfoBean.setName(cursor.getString(cursor.getColumnIndex(ContactTable.NAME)));
            userInfoBean.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.NICK)));
            userInfoBean.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.PHOTO)));
        }
        //关闭资源
        cursor.close();
        //返回数据
        return  userInfoBean;
    }
    //通过环信id获取用户联系人信息
    public List<UserInfoBean> getContactByHx(List<String> hxids){
        if(hxids==null||hxids.size()<=0){
            return null;
        }
        List<UserInfoBean> userInfoBeanList=new ArrayList<>();
        //遍历执行
        for(String hxid:hxids){
            UserInfoBean userInfoBean = getContactByHxid(hxid);
            userInfoBeanList.add(userInfoBean);
        }
        //返回数据
        return userInfoBeanList;
    }
    //保存单个联系人
    public void saveContact(UserInfoBean userInfo,Boolean isMyContact){
        if(userInfo==null){
            return;
        }
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //执行操作语句
        ContentValues values=new ContentValues();
        values.put(ContactTable.HXID,userInfo.getHxid());
        values.put(ContactTable.NAME,userInfo.getName());
        values.put(ContactTable.NICK,userInfo.getNick());
        values.put(ContactTable.PHOTO,userInfo.getPhoto());
        values.put(ContactTable.IS_CONTACT,isMyContact?1:0);
        //添加数据
        db.replace(ContactTable.TAB_NAME,null,values);
    }
    //多个联系人的保存
    public void saveContacts(List<UserInfoBean> userInfoList,boolean isMyContact){
        //安全校验
        if(userInfoList==null||userInfoList.size()<=0){
            return;
        }
        //遍历保存
        for(UserInfoBean userInfo:userInfoList){
            saveContact(userInfo,isMyContact);
        }
    }
    //删除联系人
    public void deleteContactByHxid(String hxid){
        if(hxid==null){
            return;
        }
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //执行删除语句
        db.delete(ContactTable.TAB_NAME, ContactTable.HXID+"=?",new String[]{hxid});
    }
}
