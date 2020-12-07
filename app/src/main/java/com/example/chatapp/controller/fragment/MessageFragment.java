package com.example.chatapp.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatapp.R;
import com.example.chatapp.controller.activity.ChatActivity;
import com.example.chatapp.controller.adapter.MessageListAdapter;
import com.example.chatapp.model.Model;
import com.example.chatapp.model.bean.MessageListInfoBean;
import com.example.chatapp.utils.Constant;

import java.util.List;

public class MessageFragment extends Fragment {
    private View view;
    private ListView lv_message;
    private MessageListAdapter messageListAdapter;
    private List<MessageListInfoBean> messageList;
    private MessageListInfoBean getMessageListInfo;

    private LocalBroadcastManager lbm;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.from(getContext()).inflate(R.layout.message_fragment_view,container,false);
        initView();
        initListener();
        //注册ContextMenu;
        registerForContextMenu(lv_message);
        return view;
    }
    //


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取消息的信息
        getMessageListInfo= (MessageListInfoBean) lv_message.getItemAtPosition(((AdapterView.AdapterContextMenuInfo)menuInfo).position);
        //添加布局
        if(getActivity()==null){
            return;
        }
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }
    //ContextMenu点击事件


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.contact_delect){
            //删除消息信息
            delete();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void delete() {
        //数据库中删除数据
        Model.getInstance().getMessageListDatabaseDao().removeByName(getMessageListInfo.getName());
        //刷新页面
        messageListAdapterRefresh();
    }

    private void initListener() {
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv= (ListView) parent;
                MessageListInfoBean messageListInfo= (MessageListInfoBean) lv.getAdapter().getItem(position);
                //点击红点取消
                ImageView red_point=view.findViewById(R.id.message_list_red_point);
                red_point.setVisibility(View.GONE);
                //修改数据库
                Model.getInstance().getMessageListDatabaseDao().updateDataByName(messageListInfo.getName(),true);
                //跳转到聊天窗口页面
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constant.USER_HXID,messageListInfo.getName());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        lv_message=view.findViewById(R.id.lv_message);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取数据库里的数据
        if(getActivity()==null){
            return;
        }
        messageListAdapterRefresh();
        //注册一个有新消息的广播
        lbm=LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(new ChatMessageBroadCastReceiver(),new IntentFilter(Constant.IS_NEW_MESSAGE));
        lbm.registerReceiver(new ChatMessageBroadCastReceiver(),new IntentFilter(Constant.IS_MY_NEW_MESSAGE));//自己发消息的时候刷新数据
    }

    private void messageListAdapterRefresh() {
        messageList = Model.getInstance().getMessageListDatabaseDao().getMessageList();
        messageListAdapter=new MessageListAdapter(getActivity());
        lv_message.setAdapter(messageListAdapter);
        messageListAdapter.messageListRefresh(messageList);
    }

    private class ChatMessageBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            messageListAdapterRefresh();
            if(intent.getAction()==Constant.IS_NEW_MESSAGE) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        messageListAdapterRefresh();
    }
}
