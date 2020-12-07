package com.example.chatapp.controller.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatapp.R;
import com.example.chatapp.model.bean.MessageListInfoBean;
import com.example.chatapp.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private Context context;
    private List<MessageListInfoBean> messageListInfoBeanList=new ArrayList<>();
    private MessageListInfoBean messageListInfoBean;
    public MessageListAdapter(Context context){
        this.context=context;
    }
    public void messageListRefresh(List<MessageListInfoBean> messageListInfoBeanList){
        //清除数据
        this.messageListInfoBeanList.clear();
        //添加数据
        this.messageListInfoBeanList.addAll(messageListInfoBeanList);
        //刷新数据
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return messageListInfoBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageListInfoBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        messageListInfoBean=messageListInfoBeanList.get(position);
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_message_list,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.tv_message_name=convertView.findViewById(R.id.tv_message_name);
            viewHolder.tv_message_content=convertView.findViewById(R.id.tv_message_content);
            viewHolder.tv_message_date=convertView.findViewById(R.id.tv_message_date);
            viewHolder.message_list_red_point=convertView.findViewById(R.id.message_list_red_point);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_message_name.setText(messageListInfoBean.getName());
        viewHolder.tv_message_content.setText(messageListInfoBean.getMessageContent());
        Date date=new Date(messageListInfoBean.getMessageDate());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getData=sdf.format(date);
        viewHolder.tv_message_date.setText(getData);
        if(messageListInfoBean.isMyMessage()){
            viewHolder.message_list_red_point.setVisibility(View.GONE);
        }else {
            viewHolder.message_list_red_point.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_message_name;
        private TextView tv_message_content;
        private TextView tv_message_date;
        private ImageView message_list_red_point;
    }
}
