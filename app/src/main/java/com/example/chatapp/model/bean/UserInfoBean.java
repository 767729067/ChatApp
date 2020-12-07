package com.example.chatapp.model.bean;
//用户信息的bean
public class UserInfoBean {
    private String name;//用户名称
    private String hxid;//环信的id
    private String nick;//用户昵称
    private String photo;//头像
    public UserInfoBean(){}
    public UserInfoBean(String name){
        this.name=name;
        this.hxid=name;
        this.nick=name;
    }

    public String getName() {
        return name;
    }

    public String getHxid() {
        return hxid;
    }

    public String getNick() {
        return nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
