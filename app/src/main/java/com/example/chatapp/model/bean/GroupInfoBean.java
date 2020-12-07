package com.example.chatapp.model.bean;
//群信息的bean类
public class GroupInfoBean  {
    private String groupName;//群名称
    private String groupId;//群id
    private String invatePerson;//群主

    public GroupInfoBean() {
    }

    public GroupInfoBean(String groupName, String groupId, String invatePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invatePerson = invatePerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInvatePerson() {
        return invatePerson;
    }

    public void setInvatePerson(String invatePerson) {
        this.invatePerson = invatePerson;
    }

    @Override
    public String toString() {
        return "GroupInfoBean{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", invatePerson='" + invatePerson + '\'' +
                '}';
    }
}
