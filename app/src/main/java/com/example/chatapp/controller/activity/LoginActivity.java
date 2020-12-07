package com.example.chatapp.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.UserInfoBean;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class LoginActivity extends AppCompatActivity {
    private EditText et_login_userName;
    private EditText et_login_passWord;
    private Button bt_login_login;
    private Button bt_login_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();//初始化控件
        initListener();//绑定监听事件
    }

    private void initListener() {
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击登录按钮
                final String getUserName=et_login_userName.getText().toString();
                final String getPassWord=et_login_passWord.getText().toString();
                if(!TextUtils.isEmpty(getUserName) && !TextUtils.isEmpty(getPassWord)){
                    //当不为空时创建线程登录
                    Model.getInstance().getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            EMClient.getInstance().login(getUserName, getPassWord, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    //保存用户账号
                                    Model.getInstance().loginSuccess(new UserInfoBean(getUserName));
                                    Model.getInstance().getUserInfoDatabaseAao().addUserInfo(new UserInfoBean(getUserName));
                                    //提示登录成功
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                            //登录成功跳转到登录页面
                                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            //跳转之后销毁登录页面
                                            finish();
                                        }
                                    });
                                }

                                @Override
                                public void onError(int i, String s) {
                                    //登录失败
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击注册按钮
                final String getUserName=et_login_userName.getText().toString();
                final String getPassWord=et_login_passWord.getText().toString();
                //当不为空时创建线程注册
                if(!TextUtils.isEmpty(getUserName)&&!TextUtils.isEmpty(getPassWord)){
                    Model.getInstance().getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(getUserName,getPassWord);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else {
                    //当输入为空时跳回主线程更新ui toast显示输入账号或密码为空
                    Toast.makeText(LoginActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //初始化控件
    private void initView() {
        et_login_userName = findViewById(R.id.et_login_userName);
        et_login_passWord=findViewById(R.id.et_login_passWord);
        bt_login_login=findViewById(R.id.bt_login_login);
        bt_login_register=findViewById(R.id.bt_login_register);
    }
}
