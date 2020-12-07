package com.example.chatapp.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.controller.adapter.ChatMessageContentListAdapter;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.ChatMessageContentInfoBean;
import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private TextView tv_chat_name;//标题
    private ListView lv_chat_content;//用来放置消息的列表视图
    private EditText ed_chat_input;//消息的输入框
    private Button bt_chat_send;//发送消息的按钮
    private LocalBroadcastManager lbm;
    private ChatMessageContentBroadCastReceiver chatMessageContentBroadCastReceiver;

    private String getInputContent;//获得到的发送过去的消息
    private String hxid;//获取到的环信id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getHxidByExtra();//获取点击进人对应联系人的环信id
        //初始化聊天消息的数据库
        Model.getInstance().initChatMessageContentDatabase(hxid);
        initView();//初始化控件对象
        initData();//初始化数据并设置数据
        initListener();//设置发送按钮的点击事件
        chatContentRefresh();//刷新数据
        lbm=LocalBroadcastManager.getInstance(this);
        chatMessageContentBroadCastReceiver=new ChatMessageContentBroadCastReceiver();
        lbm.registerReceiver(chatMessageContentBroadCastReceiver,new IntentFilter(Constant.IS_MY_NEW_MESSAGE));
        lbm.registerReceiver(chatMessageContentBroadCastReceiver,new IntentFilter(Constant.IS_NEW_MESSAGE));
    }

    private void chatContentRefresh() {
        List<ChatMessageContentInfoBean> allMessageList = Model.getInstance().getChatMessageContentDatabaseDao().getAllMessage();
        ChatMessageContentListAdapter chatMessageContentListAdapter=new ChatMessageContentListAdapter(this);
        chatMessageContentListAdapter.chatMessageRefresh(allMessageList);
        lv_chat_content.setAdapter(chatMessageContentListAdapter);
    }

    private void initListener() {
        bt_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击发送按钮获取发送的消息
                getInputContent=ed_chat_input.getText().toString();
                //获取之后把消息清空
                ed_chat_input.setText("");
                if(!TextUtils.isEmpty(getInputContent)){
                    //输入的消息不为空 发送消息到环信服务
                    Model.getInstance().getThreadPool().execute(new Runnable() {//开启线程池中的线程
                        @Override
                        public void run() {
                            //发送消息到服务器上 发送单聊消息纯文本
                            EMMessage message=EMMessage.createTxtSendMessage(getInputContent,hxid);
                            EMClient.getInstance().chatManager().sendMessage(message);
                            //将发送过去的消息保存到数据库中
                            Model.getInstance().getChatMessageContentDatabaseDao()
                                    .saveMessageContent("我",getInputContent,true,System.currentTimeMillis());
                            //发送信息后再消息界面创建会话
                            Model.getInstance().getMessageListDatabaseDao().saveMessageList(hxid,"我:"+getInputContent,System.currentTimeMillis(),true);
                            //发送消息改变的广播
                            LocalBroadcastManager.getInstance(ChatActivity.this).sendBroadcast(new Intent(Constant.IS_MY_NEW_MESSAGE));
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        //获取联系人的信息
        UserInfoBean getContact = Model.getInstance().getDbManager().getContactDatabaseDao().getContactByHxid(hxid);
        tv_chat_name.setText(getContact.getName());
    }

    private void initView() {
        tv_chat_name=findViewById(R.id.tv_chat_name);
        lv_chat_content=findViewById(R.id.lv_chat_content);
        ed_chat_input=findViewById(R.id.ed_chat_input);
        bt_chat_send=findViewById(R.id.bt_chat_send);

    }

    private void getHxidByExtra() {
        Intent intent=getIntent();
        hxid=intent.getStringExtra(Constant.USER_HXID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(chatMessageContentBroadCastReceiver);
    }

    private class ChatMessageContentBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            chatContentRefresh();
            Model.getInstance().getMessageListDatabaseDao().updateDataByName(hxid,true);
            if(intent.getAction()==Constant.IS_NEW_MESSAGE) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
            }
        }
    }
}
