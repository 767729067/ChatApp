package com.example.chatapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chatapp.model.bean.GroupInfoBean;
import com.example.chatapp.model.bean.InvitationInfoBean;
import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.model.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

//邀请信息表的操作类
public class InviteDatabaseDao {
    private DBHelper dbHelper;
    public InviteDatabaseDao(DBHelper dbHelper) {
        this.dbHelper=dbHelper;
    }
    //添加邀请
    public void addInvitation(InvitationInfoBean invitationInfo){
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //执行添加语句
        ContentValues values=new ContentValues();
        values.put(InviteTable.REASON,invitationInfo.getReason());//原因
        values.put(InviteTable.STATUS,invitationInfo.getStatus().ordinal());//ordinal()将enum的序号返回   状态
        UserInfoBean user = invitationInfo.getUserInfo();
        if(user!=null){
            //是联系人
            values.put(InviteTable.USER_HXID,invitationInfo.getUserInfo().getHxid());
            values.put(InviteTable.USER_NAME,invitationInfo.getUserInfo().getName());
        }else {
            //是群组
            values.put(InviteTable.GROUP_HXID,invitationInfo.getGroupInfo().getGroupId());
            values.put(InviteTable.GROUP_NAME,invitationInfo.getGroupInfo().getGroupName());
            values.put(InviteTable.USER_HXID,invitationInfo.getGroupInfo().getInvatePerson());
        }
        db.replace(InviteTable.TAB_NAME,null,values);
    }
    //获取邀请信息
    public List<InvitationInfoBean> getInvitations(){
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //执行查询语句
        String sql="select * from "+InviteTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<InvitationInfoBean> invitationInfoList=new ArrayList<>();
        while(cursor.moveToNext()){
            InvitationInfoBean invitationInfo = new InvitationInfoBean();
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.REASON)));
            invitationInfo.setStatus(intChangeEnum(cursor.getInt(cursor.getColumnIndex(InviteTable.STATUS))));

            String getGroupId = cursor.getString(cursor.getColumnIndex(InviteTable.GROUP_HXID));
            if(getGroupId!=null){
                //群邀请信息
                GroupInfoBean groupInfo = new GroupInfoBean();
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InviteTable.GROUP_HXID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.GROUP_NAME)));
                groupInfo.setInvatePerson(cursor.getString(cursor.getColumnIndex(InviteTable.USER_HXID)));

                //将groupInfo对象添加到invitation对象中
                invitationInfo.setGroupInfo(groupInfo);
            }else {
                //联系人邀请信息
                UserInfoBean userInfo = new UserInfoBean();
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable.USER_NAME)));

                //将userInfo对象添加到invitation对象中
                invitationInfo.setUserInfo(userInfo);
            }
            invitationInfoList.add(invitationInfo);
        }
        //关闭资源
        cursor.close();
        //返回数据
        return invitationInfoList;
    }
    public InvitationInfoBean.InvitationStatus intChangeEnum(int status){
        if(status==InvitationInfoBean.InvitationStatus.NEW_INVITE.ordinal()){
            return InvitationInfoBean.InvitationStatus.NEW_INVITE;
        }
        if(status==InvitationInfoBean.InvitationStatus.INVITE_ACCEPT.ordinal()){
            return InvitationInfoBean.InvitationStatus.INVITE_ACCEPT;
        }
        if(status==InvitationInfoBean.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()){
            return InvitationInfoBean.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }
        else {
            return null;
        }
    }
    //删除邀请信息
    public void removeInvitation(String hxid){
        //安全校验
        if(hxid==null){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //执行删除语句
        db.delete(InviteTable.TAB_NAME,InviteTable.USER_HXID+"=?",new String[]{hxid});
    }
    //更新邀请状态
    public void updateInvitationStatus(InvitationInfoBean.InvitationStatus invitationStatus,String hxid){
        if(hxid==null){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //执行更新操作
        ContentValues values=new ContentValues();
        values.put(InviteTable.STATUS,invitationStatus.ordinal());
        db.update(InviteTable.TAB_NAME,values,InviteTable.USER_HXID+"=?",new String[]{hxid});
    }
}
