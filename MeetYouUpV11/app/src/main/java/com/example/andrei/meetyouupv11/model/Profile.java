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
    private List<String> listOfGroups = new ArrayList<>();
    private List<String> listOfEvents = new ArrayList<>();
    private List<String> pendingEvents = new ArrayList<>();
    private List<String> declinedEvents = new ArrayList<>();

    public Profile() {
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
        this.addDeclinedEvent("None");
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

    public List<String> getListOfGroups() {
        return listOfGroups;
    }

    public void setListOfGroups(List<String> listOfGroups) {
        this.listOfGroups = listOfGroups;
    }

    public List<String> getListOfEvents() {
        return listOfEvents;
    }

    public void setListOfEvents(List<String> listOfEvents) {
        this.listOfEvents = listOfEvents;
    }

    public List<String> getPendingEvents() {
        return pendingEvents;
    }

    public void setPendingEvents(List<String> pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    public List<String> getDeclinedEvents() {
        return declinedEvents;
    }

    public void setDeclinedEvents(List<String> declinedEvents) {
        this.declinedEvents = declinedEvents;
    }

    public void addEventsAttendance(String eventId) {
        if (this.listOfEvents.isEmpty()) {
            this.listOfEvents.add(eventId);
        } else if (this.listOfEvents.get(0).equals("None")) {
            this.listOfEvents.clear();
            this.listOfEvents.add(eventId);
        } else if (!eventId.equals("None")) {
            this.listOfEvents.add(eventId);
        }
    }

    public void addToPendingEventsList(String eventId) {
        if (this.pendingEvents.isEmpty()) {
            this.pendingEvents.add(eventId);
        } else if (this.pendingEvents.get(0).equals("None")) {
            this.pendingEvents.clear();
            this.pendingEvents.add(eventId);
        } else if (!eventId.equals("None")) {
            this.pendingEvents.add(eventId);
        }
    }

    public void addDeclinedEvent(String eventId) {
        if (this.declinedEvents.isEmpty()) {
            this.declinedEvents.add(eventId);
        } else if (this.declinedEvents.get(0).equals("None")) {
            this.declinedEvents.clear();
            this.declinedEvents.add(eventId);
        } else if (!eventId.equals("None")) {
            this.declinedEvents.add(eventId);
        }
    }

    public void addGroup(String groupId) {
        if (this.listOfGroups.isEmpty()) {
            this.listOfGroups.add(groupId);
        } else if (this.listOfGroups.get(0).equals("None")) {
            this.listOfGroups.clear();
            this.listOfGroups.add(groupId);
        } else if (!groupId.equals("None")) {
            this.listOfGroups.add(groupId);
        }
    }

    public void removeFromDeclinedEvents(String eventId) {
        if (this.getDeclinedEvents().contains(eventId)) {
            this.declinedEvents.remove(eventId);
            if (declinedEvents.isEmpty()) {
                this.declinedEvents.add("None");
            }
        }
    }

    public void removeFromPendingEvents(String eventId) {
        if (this.getPendingEvents().contains(eventId)) {
            this.pendingEvents.remove(eventId);
            if (pendingEvents.isEmpty()) {
                this.pendingEvents.add("None");
            }
        }
    }

    public void removeFromlistOfEvents(String eventId) {
        if (this.getPendingEvents().contains(eventId)) {
            this.listOfEvents.remove(eventId);
            if (listOfEvents.isEmpty()) {
                this.listOfEvents.add("None");
            }
        }
    }

    public void removeFromlistOfGroups(String eventId) {
        if (this.getListOfGroups().contains(eventId)) {
            this.listOfGroups.remove(eventId);
            if (listOfGroups.isEmpty()) {
                this.listOfGroups.add("None");
            }
        }
    }
}
