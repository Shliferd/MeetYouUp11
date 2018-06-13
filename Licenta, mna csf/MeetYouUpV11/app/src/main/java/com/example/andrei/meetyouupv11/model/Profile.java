package com.example.andrei.meetyouupv11.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Profile {

    //private String userId;
    private String name;
    private String keyWords;
    private String dateOfBirth;
    private String profileDescription;
    private String profilePictureUrl;
    private List<String> friendsId = new ArrayList<>();
    private List<String> pendingFriendsId = new ArrayList<>();
    private float rating;
    private List<String> listOfGroups = new ArrayList<>();
    private List<String> listOfEvents = new ArrayList<>();

    public Profile() {
    }

    public Profile(String name, String keyWords, String dateOfBirth, String profileDescription,
                   String profilePictureUrl, List<String> friendsId, float rating, List<String> listOfGroups,
                   List<String> listOfEvents, List<String> pendingFriendsId) {

        this.name = name;
        this.keyWords = keyWords;
        this.dateOfBirth = dateOfBirth;
        this.profileDescription = profileDescription;
        this.profilePictureUrl = profilePictureUrl;
        this.friendsId = friendsId;
        this.rating = rating;
        this.listOfGroups = listOfGroups;
        this.listOfEvents = listOfEvents;
        this.pendingFriendsId = pendingFriendsId;
    }

    public Profile(String name, String keyWords, String dateOfBirth, String profileDescription, String profilePictureUrl) {

        this.name = name;
        this.keyWords = keyWords;
        this.dateOfBirth = dateOfBirth;
        this.profileDescription = profileDescription;
        this.profilePictureUrl = profilePictureUrl;
        this.addToFriendList("None");
        this.rating = 0;
        this.addGroup("None");
        this.addEventsAttendance("None");
        this.addToPendingFriends("None");
    }

    public Profile(Profile other) {

        this.name = other.getName();
        this.keyWords = other.getKeyWords();
        this.dateOfBirth = other.getDateOfBirth();
        this.profileDescription = other.getProfileDescription();
        this.profilePictureUrl = other.getProfilePictureUrl();
        this.friendsId.clear();
        this.friendsId.addAll(other.getFriendsId());
        this.rating = other.getRating();
        this.listOfGroups.clear();
        this.listOfGroups.addAll(other.getListOfGroups());
        this.listOfEvents.clear();
        this.listOfEvents.addAll(other.getListOfEvents());
        this.pendingFriendsId.clear();
        this.pendingFriendsId.addAll(other.getPendingFriendsId());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<String> getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(List<String> friendsId) {
        this.friendsId = friendsId;
    }

    public List<String> getListOfEvents() {
        return listOfEvents;
    }

    public void setListOfEvents(List<String> listOfEvents) {
        this.listOfEvents = listOfEvents;
    }

    public List<String> getListOfGroups() {
        return listOfGroups;
    }

    public void setListOfGroups(List<String> listOfGroups) {
        this.listOfGroups = listOfGroups;
    }

    public List<String> getPendingFriendsId() {
        return pendingFriendsId;
    }

    public void setPendingFriendsId(List<String> pendingFriendsId) {
        this.pendingFriendsId = pendingFriendsId;
    }

    private void addToFriendList(String newUserId) {
        if (newUserId.equals("None") && this.friendsId.size() == 0)
            this.friendsId.add(newUserId);
        else if (this.friendsId.get(0).equals("None") && this.friendsId.size() == 1) {
            this.friendsId.clear();
            this.friendsId.add(newUserId);
        } else if (!this.friendsId.get(0).equals("None")) {
            this.friendsId.add(newUserId);
        }
    }

    private void addGroup(String groupId) {
        if (groupId.equals("None") && this.listOfGroups.size() == 0) {
            this.listOfGroups.add(groupId);
        } else if (this.listOfGroups.get(0).equals("None") && this.listOfGroups.size() == 1) {
            this.listOfGroups.clear();
            this.listOfGroups.add(groupId);
        } else if (!this.listOfGroups.get(0).equals("None")) {
            this.listOfGroups.add(groupId);
        }
    }

    private void addEventsAttendance(String eventId) {
        if (eventId.equals("None") && this.listOfEvents.size() == 0) {
            this.listOfEvents.add(eventId);
        } else if (this.listOfEvents.get(0).equals("None") && this.listOfEvents.size() == 1) {
            this.listOfEvents.clear();
            this.listOfEvents.add(eventId);
        } else if (!this.listOfEvents.get(0).equals("None")) {
            this.listOfEvents.add(eventId);
        }
    }

    private void addToPendingFriends(String newUserId) {
        if (newUserId.equals("None") && this.pendingFriendsId.size() == 0)
            this.pendingFriendsId.add(newUserId);
        else if (this.pendingFriendsId.get(0).equals("None") && this.pendingFriendsId.size() == 1) {
            this.pendingFriendsId.clear();
            this.pendingFriendsId.add(newUserId);
        } else if (!this.pendingFriendsId.get(0).equals("None")) {
            this.pendingFriendsId.add(newUserId);
        }
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
