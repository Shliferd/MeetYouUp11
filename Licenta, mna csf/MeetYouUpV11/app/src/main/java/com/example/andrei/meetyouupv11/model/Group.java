package com.example.andrei.meetyouupv11.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String adminId;
    private String groupName;
    private String groupDescription;
    private int numberOfMembers;
    private List<String> membersId = new ArrayList<>();
    private List<String> listOfEventsInGroup = new ArrayList<>();
    private String groupPicture;

    public Group() {
    }

    public Group(String adminId, String name, String groupDescription, String groupPicture,
                 List<String> membersId, List<String> listOfEventsInGroup) {
        this.adminId = adminId;
        this.groupName = name;
        this.groupDescription = groupDescription;
        this.numberOfMembers = 1;
        this.groupPicture = groupPicture;
        this.membersId = membersId;
        this.listOfEventsInGroup = listOfEventsInGroup;
    }

    public Group(String adminId, String name, String groupDescription, String groupPicture) {
        this.adminId = adminId;
        this.groupName = name;
        this.groupDescription = groupDescription;
        this.numberOfMembers = 1;
        this.groupPicture = groupPicture;
        this.addMembersToGroup("None");
        this.addEventToListGroup("None");

    }

    public Group(Group other) {
        this.adminId = other.getAdminId();
        this.groupName = other.getGroupName();
        this.groupDescription = other.getGroupDescription();
        this.numberOfMembers = other.getNumberOfMembers();
        this.groupPicture = other.getGroupPicture();
        this.membersId.clear();
        this.membersId.addAll(other.getMembersId());
        this.listOfEventsInGroup.clear();
        this.listOfEventsInGroup.addAll(other.getListOfEventsInGroup());
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

    public List<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<String> membersId) {
        this.membersId = membersId;
    }

    public List<String> getListOfEventsInGroup() {
        return listOfEventsInGroup;
    }

    public void setListOfEventsInGroup(List<String> listOfEventsInGroup) {
        this.listOfEventsInGroup = listOfEventsInGroup;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    private void addMembersToGroup(String newUserId) {
        if (newUserId.equals("None") && this.membersId.size() == 0)
            this.membersId.add(newUserId);
        else if (this.membersId.get(0).equals("None") && this.membersId.size() == 1) {
            this.membersId.clear();
            this.membersId.add(newUserId);
        } else if (!this.membersId.get(0).equals("None")) {
            this.membersId.add(newUserId);
        }
    }

    private void addEventToListGroup(String eventId) {
        if (eventId.equals("None") && this.listOfEventsInGroup.size() == 0) {
            this.listOfEventsInGroup.add(eventId);
        } else if (this.listOfEventsInGroup.get(0).equals("None") && this.listOfEventsInGroup.size() == 1) {
            this.listOfEventsInGroup.clear();
            this.listOfEventsInGroup.add(eventId);
        } else if (!this.listOfEventsInGroup.get(0).equals("None")) {
            this.listOfEventsInGroup.add(eventId);
        }
    }
}
