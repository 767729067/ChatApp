package com.example.chatapp.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapp.R;
import com.example.chatapp.controller.activity.LoginActivity;
import com.example.chatapp.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class MineFragment extends Fragment implements View.OnClickListener {
    private Button bt_logout;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.from(getActivity()).inflate(R.layout.mine_fragment_view,container,false);
        initView();
        return view;
    }

    private void initView() {
        bt_logout=view.findViewById(R.id.bt_logout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        bt_logout.setText("退出登录"+"("+EMClient.getInstance().getCurrentUser()+")");
        bt_logout.setOnClickListener(this);
    }
    //点击按钮 退出登录
    @Override
    public void onClick(View v) {
        if(v==bt_logout){
            Model.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    EMClient.getInstance().logout(false, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            //退出成功关闭数据库
                            Model.getInstance().getDbManager().close();
                            //退出成功跳转页面
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                                    //跳转页面
                                    Intent intent=new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    //结束当前页面
                                    getActivity().finish();

                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"退出失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
            });
        }
    }
}
