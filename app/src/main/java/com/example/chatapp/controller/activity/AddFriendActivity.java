package com.example.chatapp.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.UserInfoBean;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;


public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView add_search;//查找的按钮
    private EditText add_search_account;//输入的查找账号
    private ConstraintLayout search_friend_add;//查找到账号时显示的布局
    private ImageView add_search_icon;//查找到的好友的头像
    private TextView add_search_name;//查找到好友的名称
    private Button add_search_add;//查找到添加好友的按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initView();
        initListener();
    }

    private void initListener() {
        add_search.setOnClickListener(this);
        add_search_add.setOnClickListener(this);
    }
    private UserInfoBean userInfo;
    //监听事件
    @Override
    public void onClick(View v) {
        //点击查找按钮
        if(v==add_search){
            //点击获得输入要查找的账号
            final String account=add_search_account.getText().toString();
            //判断是否为空
            if(TextUtils.isEmpty(account)){
                Toast.makeText(AddFriendActivity.this,"输入的查找账号不能为空",Toast.LENGTH_SHORT).show();
                return;
            }else {
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //判断账号是否存在
                        userInfo = new UserInfoBean(account);
                        //更新ui显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示布局
                                search_friend_add.setVisibility(View.VISIBLE);
                                //替换名称
                                add_search_name.setText(userInfo.getName());
                                add_search_icon.setImageResource(R.drawable.head_icon);
                            }
                        });
                    }
                });
            }

        }
        //点击添加好友按钮
        if(v==add_search_add){
            Model.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    //去环信服务器添加好友
                    try {
                        EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddFriendActivity.this,"添加好友邀请发送成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        Toast.makeText(AddFriendActivity.this,"添加好友邀请发送失败",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void initView() {
        add_search=findViewById(R.id.add_search);
        add_search_account=findViewById(R.id.add_search_account);
        add_search_icon=findViewById(R.id.add_search_icon);
        add_search_name=findViewById(R.id.add_search_name);
        add_search_add=findViewById(R.id.add_search_add);
        search_friend_add=findViewById(R.id.search_friend_add);
    }
}
