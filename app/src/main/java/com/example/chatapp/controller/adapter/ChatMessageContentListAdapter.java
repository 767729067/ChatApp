package com.example.chatapp.controller.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.model.bean.ChatMessageContentInfoBean;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageContentListAdapter extends BaseAdapter {
    public Context context;
    public List<ChatMessageContentInfoBean> chatMessageContentInfoList=new ArrayList<>();
    public ChatMessageContentListAdapter(Context context){
        this.context=context;
    }
    public void chatMessageRefresh(List<ChatMessageContentInfoBean> chatMessageContentInfoList){
        this.chatMessageContentInfoList.clear();
        //添加数据
        this.chatMessageContentInfoList.addAll(chatMessageContentInfoList);
        //刷新数据
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return chatMessageContentInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessageContentInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessageContentInfoBean chatMessageContentInfo=chatMessageContentInfoList.get(position);
        ViewHolder viewHolder=null;
        if(convertView==null){
            //创建视图
            if(chatMessageContentInfo.isMyMessage()){
                convertView= LayoutInflater.from(context).inflate(R.layout.chat_message_list_right,parent,false);
            }else {
                convertView= LayoutInflater.from(context).inflate(R.layout.chat_message_list_left,parent,false);
            }
            //创建viewHolder对象
            viewHolder=new ViewHolder();
            viewHolder.chat_content_list_content=convertView.findViewById(R.id.chat_content_list_content);
            //设置标记
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.chat_content_list_content.setText(chatMessageContentInfo.getMessageContent());
        return convertView;
    }
    //创建一个viewHolder
    private class ViewHolder{
        private TextView chat_content_list_content;
    }
}
