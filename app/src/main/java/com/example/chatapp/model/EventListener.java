package com.example.chatapp.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatapp.model.bean.InvitationInfoBean;
import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.utils.Constant;
import com.example.chatapp.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

//全局事件监听类
public class EventListener{
    private Context context;
    private LocalBroadcastManager lbm;
    public EventListener(Context context) {
        this.context=context;

        //创建一个本地广播的管理
        lbm=LocalBroadcastManager.getInstance(context);

        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(new ContactListener());

        //注册一个接收消息的监听
        EMClient.getInstance().chatManager().addMessageListener(new ChatListener());
    }
    private class ChatListener implements EMMessageListener {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for(EMMessage message:list){
                //更新数据库数据
                Model.getInstance().initChatMessageContentDatabase(message.getUserName());
                Model.getInstance().getChatMessageContentDatabaseDao()
                        .saveMessageContent(message.getUserName(),((EMTextMessageBody)message.getBody()).getMessage(),false,message.getMsgTime());

                Model.getInstance().getMessageListDatabaseDao()
                        .saveMessageList(message.getUserName(),((EMTextMessageBody)message.getBody()).getMessage(),message.getMsgTime(),false);
            }
            //发出震动
            //发送广播
            lbm.sendBroadcast(new Intent(Constant.IS_NEW_MESSAGE));
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }
    private class ContactListener implements EMContactListener {
        //联系人增加后执行的方法
        @Override
        public void onContactAdded(String hxid) {
            //数据库更新
            Model.getInstance().getDbManager().getContactDatabaseDao().saveContact(new UserInfoBean(hxid),true);
            //发送联系人更新的广播
            lbm.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        //联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxid) {
            //数据更新
            Model.getInstance().getDbManager().getContactDatabaseDao().deleteContactByHxid(hxid);
            //删除邀请信息
            Model.getInstance().getDbManager().getInviteDatabaseDao().removeInvitation(hxid);
            //发送广播
            lbm.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        //接收到联系人的新邀请
        @Override
        public void onContactInvited(String hxid, String reason) {
            //数据库更新
            InvitationInfoBean invitationInfo=new InvitationInfoBean();
            invitationInfo.setUserInfo(new UserInfoBean(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfoBean.InvitationStatus.NEW_INVITE);//新邀请
            Model.getInstance().getDbManager().getInviteDatabaseDao().addInvitation(invitationInfo);
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送邀请信息变化的广播
            lbm.sendBroadcast(new Intent(Constant.INVITE_CHANGED));
        }
        //别人同意了你的邀请
        @Override
        public void onFriendRequestAccepted(String hxid) {
            //数据库更新
            InvitationInfoBean invitationInfo=new InvitationInfoBean();
            invitationInfo.setUserInfo(new UserInfoBean(hxid));
            invitationInfo.setStatus(InvitationInfoBean.InvitationStatus.INVITE_ACCEPT_BY_PEER);//别同意了你的邀请
            Model.getInstance().getDbManager().getInviteDatabaseDao().addInvitation(invitationInfo);
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            lbm.sendBroadcast(new Intent(Constant.INVITE_CHANGED));
        }
        //别人拒绝了你的好友邀请
        @Override
        public void onFriendRequestDeclined(String hxid) {
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            lbm.sendBroadcast(new Intent(Constant.INVITE_CHANGED));
        }

    }

}
