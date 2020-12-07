package com.example.chatapp.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.controller.adapter.AddFriendRequestListAdapter;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.InvitationInfoBean;
import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class AddFriendResponseActivity extends AppCompatActivity {
    private ListView lv_add_request;//申请添加好友的列表
    private AddFriendRequestListAdapter addFriendRequestListAdapter;//添加好友的监听
    private LocalBroadcastManager ibm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_response);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ibm.unregisterReceiver(new MyBroadCast());
    }

    private void initData() {
        //回调监听的接口
        AddFriendRequestListAdapter.OnInviteListener onInviteListener=new AddFriendRequestListAdapter.OnInviteListener() {
            @Override
            public void onAgree(final InvitationInfoBean invitationInfo) {
                //向服务器告知我已经接受好友的请求
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(invitationInfo.getUserInfo().getHxid());
                            //更新数据库
                            Model.getInstance().getDbManager().getInviteDatabaseDao().updateInvitationStatus(InvitationInfoBean.InvitationStatus.INVITE_ACCEPT,invitationInfo.getUserInfo().getHxid());
                            //更新页面
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriendResponseActivity.this,"接受邀请",Toast.LENGTH_SHORT).show();

                                    //刷新页面
                                    refresh();
                                }
                            });

                        } catch (HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriendResponseActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onRefuse(final InvitationInfoBean invitationInfo) {
                //向服务器告知我已经拒绝好友的请求
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(invitationInfo.getUserInfo().getHxid());

                            //更新数据库
                            Model.getInstance().getDbManager().getInviteDatabaseDao().removeInvitation(invitationInfo.getUserInfo().getHxid());
                            //页面变化
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriendResponseActivity.this,"你拒绝了邀请",Toast.LENGTH_SHORT).show();

                                    refresh();
                                }
                            });
                        } catch (HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriendResponseActivity.this,"拒绝失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        //创建适配器对象
        addFriendRequestListAdapter = new AddFriendRequestListAdapter(AddFriendResponseActivity.this,onInviteListener);
        lv_add_request.setAdapter(addFriendRequestListAdapter);
        //刷新方法
        refresh();
        //广播注册
        ibm = LocalBroadcastManager.getInstance(this);
        ibm.registerReceiver(new MyBroadCast(),new IntentFilter(Constant.INVITE_CHANGED));
        //从环信服务器获取所有的联系人
    }


    class MyBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    }

    private void refresh() {
        //获取数据库中的所有邀请信息
        List<InvitationInfoBean> invitations = Model.getInstance().getDbManager().getInviteDatabaseDao().getInvitations();
        //刷新适配器
        addFriendRequestListAdapter.refresh(invitations);
    }

    private void initView() {
        lv_add_request=findViewById(R.id.lv_add_request);
    }
}
