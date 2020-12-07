package com.example.chatapp.model.bean;
//邀请信息的bean类
public class InvitationInfoBean {
    private UserInfoBean userInfo;//用户信息
    private GroupInfoBean groupInfo;//群信息

    private String reason;
    private InvitationStatus status;

    public InvitationInfoBean() {
    }

    public InvitationInfoBean(UserInfoBean userinfo, GroupInfoBean groupInfo, String reason, InvitationStatus status) {
        this.userInfo = userinfo;
        this.groupInfo = groupInfo;
        this.reason = reason;
        this.status = status;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public GroupInfoBean getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfoBean groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    //枚举类
    public enum InvitationStatus{
        //邀请状态
        NEW_INVITE,//新邀请
        INVITE_ACCEPT,//接受邀请
        INVITE_ACCEPT_BY_PEER//邀请信息被接受
    }

    @Override
    public String toString() {
        return "InvitationInfoBean{" +
                "userInfo=" + userInfo +
                ", groupInfo=" + groupInfo +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }
}
