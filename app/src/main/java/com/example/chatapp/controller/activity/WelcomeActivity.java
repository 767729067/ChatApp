package com.example.chatapp.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.chatapp.R;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.UserInfoBean;
import com.hyphenate.chat.EMClient;

public class WelcomeActivity extends AppCompatActivity {
    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(isFinishing()){
                return;
            }

            //判断进入主页面还登录页面
            switch (msg.what){
                case 0:startLoginOrMain();
                break;
            }
        }
    };

    private void startLoginOrMain() {
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断当前用户是否登录过
                if(EMClient.getInstance().isLoggedInBefore()){
                    //登录过
                    UserInfoBean userInfo = Model.getInstance().getUserInfoDatabaseAao().getUserInfoByHxid(EMClient.getInstance().getCurrentUser());
                    if(userInfo==null){
                        //如果没有 跳转到登录页面
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        //跳转到主页面之前要跳转到登录的方法
                        Model.getInstance().loginSuccess(userInfo);
                        //如果数据库有信息 直接跳转到主页面
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //两秒后打开界面
        myHandler.sendEmptyMessageDelayed(0,2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //页面销毁时 删除消息
        myHandler.removeCallbacksAndMessages(null);
    }
}
