package com.example.andrei.meetyouupv11.model;

import java.util.List;

public class BasicEvent implements Event {

    private String eventId;
    private String userCreatorId;
    private String eventName;
    private List<String> usersToParticipate;
    private String eventKeyWords;
    private String eventDate;
    private String eventDescription;
    private String eventLocation;

    public BasicEvent(String eventId, String userCreatorId, String eventName, List<String> usersToParticipate,
                      String eventKeyWords, String eventDate, String eventDescription, String eventLocation) {
        this.eventId = eventId;
        this.userCreatorId = userCreatorId;
        this.eventName = eventName;
        this.usersToParticipate = usersToParticipate;
        this.eventKeyWords = eventKeyWords;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
    }


    @Override
    public void addToParticipants(String userId) {
        usersToParticipate.add(userId);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<String> getUsersToParticipate() {
        return usersToParticipate;
    }

    public void setUsersToParticipate(List<String> usersToParticipate) {
        this.usersToParticipate = usersToParticipate;
    }

    public String getEventKeyWords() {
        return eventKeyWords;
    }

    public void setEventKeyWords(String eventKeyWords) {
        this.eventKeyWords = eventKeyWords;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(String userCreatorId) {
        this.userCreatorId = userCreatorId;
    }
}
