package com.example.andrei.meetyouupv11.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private String userId;
    private String name;
    private String keyWords;
    private String dateOfBirth;
    private String profileDescription;
    private String profilePictureUrl;
    private List<String> pendingEvents = new ArrayList<>();
    private List<String> listOfGroups = new ArrayList<>();
    private List<String> listOfEvents = new ArrayList<>();

    public Profile() {
    }

    public Profile(String userId, String name, String keyWords, String dateOfBirth, String profileDescription,
                   String profilePictureUrl, List<String> pendingEvents, List<String> listOfGroups,
                   List<String> listOfEvents) {

        this.userId = userId;
        this.name = name;
        this.keyWords = keyWords;
        this.dateOfBirth = dateOfBirth;
        this.profileDescription = profileDescription;
        this.profilePictureUrl = profilePictureUrl;
        this.pendingEvents = pendingEvents;
        this.listOfGroups = listOfGroups;
        this.listOfEvents = listOfEvents;
    }

    public Profile(String userId, String name, String keyWords, String dateOfBirth, String profileDescription, String profilePictureUrl) {

        this.userId = userId;
        this.name = name;
        this.keyWords = keyWords;
        this.dateOfBirth = dateOfBirth;
        this.profileDescription = profileDescription;
        this.profilePictureUrl = profilePictureUrl;
        this.addGroup("None");
        this.addEventsAttendance("None");
        this.addToPendingEventsList("None");

    }

    public Profile(Profile other) {

        this.userId = other.getUserId();
        this.name = other.getName();
        this.keyWords = other.getKeyWords();
        this.dateOfBirth = other.getDateOfBirth();
        this.profileDescription = other.getProfileDescription();
        this.profilePictureUrl = other.getProfilePictureUrl();
        this.pendingEvents.clear();
        this.pendingEvents.addAll(other.getPendingEvents());
        this.listOfGroups.clear();
        this.listOfGroups.addAll(other.getListOfGroups());
        this.listOfEvents.clear();
        this.listOfEvents.addAll(other.getListOfEvents());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<String> getPendingEvents() {
        return pendingEvents;
    }

    public void setPendingEvents(List<String> pendingEvents) {
        this.pendingEvents = pendingEvents;
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

    public void addToPendingEventsList(String eventId) {
        if (eventId.equals("None") && this.pendingEvents.size() == 0)
            this.pendingEvents.add(eventId);
        else if (this.pendingEvents.get(0).equals("None") && !eventId.equals("None")) {
            this.pendingEvents.clear();
            this.pendingEvents.add(eventId);
        } else if (!this.pendingEvents.get(0).equals("None") && !this.pendingEvents.contains(eventId)) {
            this.pendingEvents.add(eventId);
        }
    }

    public void addGroup(String groupId) {
        if (groupId.equals("None") && this.listOfGroups.size() == 0) {
            this.listOfGroups.add(groupId);
        } else if (this.listOfGroups.get(0).equals("None") && !groupId.equals("None")) {
            this.listOfGroups.clear();
            this.listOfGroups.add(groupId);
        } else if (!this.listOfGroups.get(0).equals("None") && !listOfGroups.contains(groupId)) {
            this.listOfGroups.add(groupId);
        }
    }

    public void addEventsAttendance(String eventId) {
        if (eventId.equals("None") && this.listOfEvents.size() == 0) {
            this.listOfEvents.add(eventId);
        } else if (this.listOfEvents.get(0).equals("None") && !eventId.equals("None")) {
            this.listOfEvents.clear();
            this.listOfEvents.add(eventId);
        } else if (!this.listOfEvents.get(0).equals("None") && !listOfEvents.contains(eventId)) {
            this.listOfEvents.add(eventId);
        }
    }
}
