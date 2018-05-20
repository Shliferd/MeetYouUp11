package com.example.andrei.meetyouupv11.model;

public class Group {

    private String groupId;
    private String groupName;
    private String groupDescription;
    private int numberOfMembers;
    private String[] membersId;

    public Group(){}

    public Group(String groupId, String name, String groupDescription, int numberOfMembers, String[] membersId) {
        this.groupId = groupId;
        this.groupName = name;
        this.groupDescription = groupDescription;
        this.numberOfMembers = numberOfMembers;
        this.membersId = membersId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public String[] getMembersId() {
        return membersId;
    }

    public void setMembersId(String[] membersId) {
        this.membersId = membersId;
    }
}
