package com.example.chatapp.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.chatapp.R;
import com.example.chatapp.model.bean.InvitationInfoBean;
import com.example.chatapp.model.bean.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

public class AddFriendRequestListAdapter extends BaseAdapter {
    private Context context;
    private List<InvitationInfoBean> invitationInfoBeanList=new ArrayList<>();
    private OnInviteListener onInviteListener;
    private InvitationInfoBean invitationInfo;

    public AddFriendRequestListAdapter(Context context,OnInviteListener onInviteListener){
        this.context=context;
        this.onInviteListener=onInviteListener;
    }
    //刷新数据的方法
    public void refresh(List<InvitationInfoBean> invitationInfoBeanList){
        //安全校验
        if(invitationInfoBeanList!=null&& invitationInfoBeanList.size()>=0){
            //清空数据 将上一次的数据清除
            this.invitationInfoBeanList.clear();
            //添加数据
            this.invitationInfoBeanList.addAll(invitationInfoBeanList);
            notifyDataSetChanged();//刷新页面
        }
    }
    @Override
    public int getCount() {
        return invitationInfoBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return invitationInfoBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取创建一个viewHolder
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_lv_add_request,parent,false);
            viewHolder.item_request_tv_name=convertView.findViewById(R.id.item_request_tv_name);
            viewHolder.item_request_tv_reason=convertView.findViewById(R.id.item_request_tv_reason);
            viewHolder.item_request_bt_agree=convertView.findViewById(R.id.item_request_bt_agree);
            viewHolder.item_request_bt_refuse=convertView.findViewById(R.id.item_request_bt_refuse);
            //将这些保存到convertView中
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //获取当前item数据
        invitationInfo=invitationInfoBeanList.get(position);
        //显示item数据
        UserInfoBean userInfo=invitationInfo.getUserInfo();
        if(userInfo!=null){
            //联系人的邀请信息
            viewHolder.item_request_tv_name.setText(invitationInfo.getUserInfo().getName());//显示申请好友的名称

            viewHolder.item_request_bt_agree.setVisibility(View.GONE);
            viewHolder.item_request_bt_refuse.setVisibility(View.GONE);
            if(invitationInfo.getStatus()== InvitationInfoBean.InvitationStatus.NEW_INVITE) {
                //新的邀请
                if(invitationInfo.getReason()==null){
                    viewHolder.item_request_tv_reason.setText("添加好友");//申请好友的原因
                }else {
                    viewHolder.item_request_tv_reason.setText(invitationInfo.getReason());//申请好友的原因
                }
                //设置按钮可以点击
                viewHolder.item_request_bt_agree.setVisibility(View.VISIBLE);
                viewHolder.item_request_bt_refuse.setVisibility(View.VISIBLE);


            }else if(invitationInfo.getStatus()== InvitationInfoBean.InvitationStatus.INVITE_ACCEPT){
                //接收邀请
                if(invitationInfo.getReason()==null){
                    viewHolder.item_request_tv_reason.setText("接收邀请");//申请好友的原因
                }else {
                    viewHolder.item_request_tv_reason.setText(invitationInfo.getReason());//申请好友的原因
                }
            }else if(invitationInfo.getStatus()== InvitationInfoBean.InvitationStatus.INVITE_ACCEPT_BY_PEER){
                //邀请被接受
                if(invitationInfo.getReason()==null){
                    viewHolder.item_request_tv_reason.setText("邀请被接受");//申请好友的原因
                }else {
                    viewHolder.item_request_tv_reason.setText(invitationInfo.getReason());//申请好友的原因
                }
            }

            //点击接受好友邀请
            viewHolder.item_request_bt_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInviteListener.onAgree(invitationInfo);
                }
            });
            //点击拒绝好友邀请
            viewHolder.item_request_bt_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInviteListener.onRefuse(invitationInfo);
                }
            });

        }else {
            //群组的邀请信息
        }


        //返回view
        return convertView;
    }
    //创建一个viewHolder
    private class ViewHolder{
        private TextView item_request_tv_name;
        private TextView item_request_tv_reason;
        private Button item_request_bt_agree;
        private Button item_request_bt_refuse;
    }

    public interface OnInviteListener{
        //接受联系人的按钮点击事件
        void onAgree(InvitationInfoBean invitationInfo);
        //拒绝联系人的按钮点击事件
        void onRefuse(InvitationInfoBean invitationInfo);
    }
}
