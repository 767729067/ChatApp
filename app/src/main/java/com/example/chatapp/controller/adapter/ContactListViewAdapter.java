package com.example.chatapp.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.model.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

public class ContactListViewAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfoBean> userInfoBeans=new ArrayList<>();
    private UserInfoBean userInfoBean;
    public ContactListViewAdapter(Context context){
        this.context=context;

    }
    public void refresh(List<UserInfoBean> userInfoBeans){
        if(userInfoBeans!=null&&userInfoBeans.size()>=0){
            //清空上一次的数据
            this.userInfoBeans.clear();
            //设置数据
            this.userInfoBeans.addAll(userInfoBeans);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return userInfoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        userInfoBean=userInfoBeans.get(position);
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_contact_list,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.contact_item_name=convertView.findViewById(R.id.contact_item_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //设置数据
        viewHolder.contact_item_name.setText(userInfoBean.getName());
        return convertView;
    }
    //创建一个viewHolder类
    private class ViewHolder{
        private TextView contact_item_name;
    }
}
