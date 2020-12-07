package com.example.chatapp.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.controller.adapter.MyViewPagerAdapter;
import com.example.chatapp.controller.fragment.ContactFragment;
import com.example.chatapp.controller.fragment.MessageFragment;
import com.example.chatapp.controller.fragment.MineFragment;
import com.example.chatapp.utils.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ConstraintLayout cl_message;//消息按钮
    private ConstraintLayout cl_contact;//联系人按钮
    private ConstraintLayout cl_mine;//我的按钮
    private ImageView iv_message;
    private ImageView iv_contact;
    private ImageView iv_mine;
    private TextView tv_message;
    private TextView tv_contact;
    private TextView tv_mine;
    private TextView title;//顶部标题
    private ViewPager vp_main;
    private List<Fragment> fragmentList;
    private ImageView add_bt_main;
    private ImageView chat_red_point;//有消息发送过来的时候显示红点
    private LocalBroadcastManager lbm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDataAndBindAdapter();
        registerBroadCastReceiver();
    }

    private void registerBroadCastReceiver() {
        lbm=LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(new NewMessageBroadCastReceiver(),new IntentFilter(Constant.IS_NEW_MESSAGE));
    }

    //调用放发结束主页面
    private void initDataAndBindAdapter() {
        MessageFragment messageFragment=new MessageFragment();
        ContactFragment contactFragment=new ContactFragment();
        MineFragment mineFragment=new MineFragment();
        fragmentList=new ArrayList<>();
        fragmentList.add(messageFragment);
        fragmentList.add(contactFragment);
        fragmentList.add(mineFragment);
        MyViewPagerAdapter myViewPagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        vp_main.setAdapter(myViewPagerAdapter);
        vp_main.setOnPageChangeListener(this);
    }

    private void initView() {
        cl_message=findViewById(R.id.cl_message);
        cl_contact=findViewById(R.id.cl_contact);
        cl_mine=findViewById(R.id.cl_mine);
        cl_message.setOnClickListener(this);
        cl_contact.setOnClickListener(this);
        cl_mine.setOnClickListener(this);
        iv_message=findViewById(R.id.iv_message);
        iv_message.setImageResource(R.drawable.message1);
        iv_contact=findViewById(R.id.iv_contact);
        iv_mine=findViewById(R.id.iv_mine);
        tv_message=findViewById(R.id.tv_message);
        tv_contact=findViewById(R.id.tv_contact);
        tv_mine=findViewById(R.id.tv_mine);
        title=findViewById(R.id.title);
        vp_main=findViewById(R.id.vp_main);
        add_bt_main=findViewById(R.id.add_bt_main);
        add_bt_main.setOnClickListener(this);

        //红点初始化
        chat_red_point=findViewById(R.id.chat_red_point);
    }

    //设置布局的监听事件  根据点击切换颜色
    @Override
    public void onClick(View v) {
        if(v==cl_message){
            vp_main.setCurrentItem(0);
            chat_red_point.setVisibility(View.GONE);
        }
        if(v==cl_contact){
            vp_main.setCurrentItem(1);
        }
        if (v==cl_mine){
            vp_main.setCurrentItem(2);
        }if(v==add_bt_main){
            Intent intent =new Intent(MainActivity.this,AddFriendActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position==0){
            iv_message.setImageResource(R.drawable.message1);
            iv_contact.setImageResource(R.drawable.contact);
            iv_mine.setImageResource(R.drawable.mine);
            tv_message.setTextColor(Color.parseColor("#FFFFFF"));
            tv_contact.setTextColor(Color.parseColor("#8A8A8A"));
            tv_mine.setTextColor(Color.parseColor("#8A8A8A"));
            add_bt_main.setVisibility(View.GONE);
            title.setText("消息");
        }
        if(position==1){
            iv_message.setImageResource(R.drawable.message);
            iv_contact.setImageResource(R.drawable.contact1);
            iv_mine.setImageResource(R.drawable.mine);
            tv_message.setTextColor(Color.parseColor("#8A8A8A"));
            tv_contact.setTextColor(Color.parseColor("#FFFFFF"));
            tv_mine.setTextColor(Color.parseColor("#8A8A8A"));
            add_bt_main.setVisibility(View.VISIBLE);
            title.setText("联系人");
        }
        if(position==2){
            iv_message.setImageResource(R.drawable.message);
            iv_contact.setImageResource(R.drawable.contact);
            iv_mine.setImageResource(R.drawable.mine1);
            tv_message.setTextColor(Color.parseColor("#8A8A8A"));
            tv_contact.setTextColor(Color.parseColor("#8A8A8A"));
            tv_mine.setTextColor(Color.parseColor("#FFFFFF"));
            add_bt_main.setVisibility(View.GONE);
            title.setText("我的");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //销毁主页面

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private class NewMessageBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()==Constant.IS_NEW_MESSAGE) {
                if (vp_main.getCurrentItem() == 0) {

                } else {
                    chat_red_point.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
