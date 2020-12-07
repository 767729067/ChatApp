package com.example.chatapp.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatapp.R;
import com.example.chatapp.controller.activity.AddFriendResponseActivity;
import com.example.chatapp.controller.activity.ChatActivity;
import com.example.chatapp.controller.adapter.ContactListViewAdapter;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.UserInfoBean;
import com.example.chatapp.utils.Constant;
import com.example.chatapp.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    private View view;
    private View headView;
    private ListView lv_contact_view;
    private List<UserInfoBean> userInfoBeans;
    private ConstraintLayout add_contact;
    private ConstraintLayout group_contact;
    private ImageView add_head_red_point;//申请好友的红点
    private ImageView group_chat_red_point;//群聊的红点
    private LocalBroadcastManager lbm;//本地广播管理
    private ContactListViewAdapter contactListViewAdapter;

    private UserInfoBean userInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.from(getContext()).inflate(R.layout.contact_fragment_view,container,false);
        initView();//初始化view
        initData();//初始化数据
        initListener();//初始化监听
        updateView();
        return view;
    }
    //关闭广播

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(new ContactBroadCastReceiver());
        lbm.unregisterReceiver(new ContactChangedBroadCastReceiver());
    }

    private void updateView() {
        //初始化红点的显示
        Boolean is_new_invite= SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        add_head_red_point.setVisibility(is_new_invite? View.VISIBLE:View.GONE);

        //注册广播
        lbm=LocalBroadcastManager.getInstance(getActivity());
        //邀请信息变化的广播
        lbm.registerReceiver(new ContactBroadCastReceiver(),new IntentFilter(Constant.INVITE_CHANGED));
        //联系人变化的广播
        lbm.registerReceiver(new ContactChangedBroadCastReceiver(),new IntentFilter(Constant.CONTACT_CHANGED));
        //通过环信id获取所有联系人
        getContacts();

        //绑定ListView和contextMenu
        registerForContextMenu(lv_contact_view);
    }
    //创建ContextMenu 长按弹出列表
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取选中长按目录的信息
        //安全校验
        if(menuInfo==null){
            return;
        }
        userInfo= (UserInfoBean) lv_contact_view.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        //添加布局
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);

    }
    //弹出的列表选中事件
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.contact_delect){
            //执行删除选中的联系人
            deleteContact();
            return true;//消费事件
        }
        return super.onContextItemSelected(item);
    }
    //删除联系人
    private void deleteContact() {
        //环信服务器删除联系人
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //环信服务器端删除联系人
                    EMClient.getInstance().contactManager().deleteContact(userInfo.getHxid());
                    //更新数据库
                    Model.getInstance().getDbManager().getContactDatabaseDao().deleteContactByHxid(userInfo.getHxid());
                    //弹出提示
                    //在fragment中调用getActivity可能会为空
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //刷新页面
                            refreshContact();
                            Toast.makeText(getActivity(),"删除好友成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"删除好友失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    //通过环信id获取所有联系人
    private void getContacts() {
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取所有好友的hxid
                    List<String> hxids = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    //非空校验
                    if(hxids!=null&&hxids.size()>=0){
                        //转换
                        List<UserInfoBean> userInfoList=new ArrayList<>();
                        for(String hxid:hxids){
                            UserInfoBean userInfo = new UserInfoBean(hxid);
                            userInfoList.add(userInfo);
                        }
                        //保存数据库
                        Model.getInstance().getDbManager().getContactDatabaseDao().saveContacts(userInfoList,true);
                        //可能会报空指针异常 画面切换的过程中
                        if(getActivity()==null){
                            return;
                        }
                        //刷新页面
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新联系人
                                refreshContact();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //刷新联系人
    private void refreshContact() {
        //获取数据
        List<UserInfoBean> contactInfoBeanList=Model.getInstance().getDbManager().getContactDatabaseDao().getContacts();
        //校验
        if(contactInfoBeanList!=null&&contactInfoBeanList.size()>=0){
            //设置数据并且刷新
            contactListViewAdapter.refresh(contactInfoBeanList);
        }
    }
    //联系人变化的广播
    class ContactChangedBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新数据
            refreshContact();
        }
    }
    //邀请信息变化的广播
    class ContactBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点的显示
            add_head_red_point.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    }
    private void initListener() {
        //listView的点击事件
        lv_contact_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv= (ListView) parent;
                UserInfoBean userInfo= (UserInfoBean) lv.getAdapter().getItem(position);
                //跳转到聊天窗口页面
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constant.USER_HXID,userInfo.getHxid());
                startActivity(intent);
            }
        });
        //点击好友申请
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_head_red_point.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,false);

                //跳转到申请好友的列表页面
                startActivity(new Intent(getActivity(), AddFriendResponseActivity.class));
            }
        });
        //点击群聊
        group_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        userInfoBeans=new ArrayList<>();
        contactListViewAdapter=new ContactListViewAdapter(getContext());
        lv_contact_view.setAdapter(contactListViewAdapter);
    }

    private void initView() {
        lv_contact_view=view.findViewById(R.id.lv_content_view);
        headView=View.inflate(getContext(),R.layout.head_view_contact_list,null);
        add_contact=headView.findViewById(R.id.add_contact);
        group_contact=headView.findViewById(R.id.group_contact);
        lv_contact_view.addHeaderView(headView);
        add_head_red_point=headView.findViewById(R.id.add_head_red_point);
        group_chat_red_point=headView.findViewById(R.id.group_chat_red_point);
    }
}
