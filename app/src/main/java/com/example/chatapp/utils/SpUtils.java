package com.example.chatapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.chatapp.ChatApplication;
//保存数据
//获取数据
public class SpUtils {
    public static final String IS_NEW_INVITE ="is_new_invite";
    private static SharedPreferences sp;
    private static SpUtils spUtils=new SpUtils();
    private SpUtils() {

    }
    //获取工具类的实例对象
    public static SpUtils getInstance(){
        if(sp==null) {
            sp = ChatApplication.getGlobalApplication().getSharedPreferences("chatApp", Context.MODE_PRIVATE);
        }
        return spUtils;
    }
    //保存数据
    public void save(String key,Object value){
        if(value instanceof String ){
            sp.edit().putString(key, (String) value).commit();
        }else if(value instanceof Boolean){
            sp.edit().putBoolean(key, (Boolean) value).commit();
        }else if(value instanceof Integer){
            sp.edit().putInt(key, (Integer) value).commit();
        }
    }
    //获取String类型的数据
    public String getString(String key,String defValue){
        return sp.getString(key,defValue);

    }
    //获取boolean类型数据
    public Boolean getBoolean(String key,boolean defValue){
        return sp.getBoolean(key,defValue);
    }
    //获取int类型的数据
    public Integer getInt(String key,int defValue){
        return sp.getInt(key,defValue);
    }
}
