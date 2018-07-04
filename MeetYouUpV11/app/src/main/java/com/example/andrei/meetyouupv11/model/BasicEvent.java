package com.example.andrei.meetyouupv11.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class BasicEvent implements Event {

    private String userCreatorId;
    private String eventName;
    private List<String> goingUsers = new ArrayList<>();
    private List<String> declinedUsers = new ArrayList<>();
    private List<String> pendingAdminAccept = new ArrayList<>();
    private String eventKeyWords;
    private String eventDate;
    private String eventDescription;
    private Double latitude;
    private Double longitude;
    private String locationTitle;
    private String eventPicture;
    private boolean isShareable;
    private boolean isByAdminAccept;
    private boolean isLimited;
    private int numberOfParticipants;
    private String eventId;

    public BasicEvent() {
    }

    public BasicEvent(String userCreatorId, String eventName, String eventKeyWords,
                      String eventDate, String eventDescription, String eventPicture,
                      String locationTitle, Double latitude, Double longitude) {
        this.userCreatorId = userCreatorId;
        this.eventName = eventName;
        this.eventKeyWords = eventKeyWords;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationTitle = locationTitle;
        this.eventPicture = eventPicture;
        this.addToGoingUsers("None");
        this.addToDeclinedUers("None");
        this.addToPendingAdminAccept("None");
        eventId = "None";
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public boolean isByAdminAccept() {
        return isByAdminAccept;
    }

    public void setByAdminAccept(boolean byAdminAccept) {
        isByAdminAccept = byAdminAccept;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public void setShareable(boolean shareable) {
        isShareable = shareable;
    }

    public boolean isShareable() {
        return isShareable;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(String userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<String> getGoingUsers() {
        return goingUsers;
    }

    public void setGoingUsers(List<String> goingUsers) {
        this.goingUsers = goingUsers;
    }

    public List<String> getDeclinedUsers() {
        return declinedUsers;
    }

    public void setDeclinedUsers(List<String> declinedUsers) {
        this.declinedUsers = declinedUsers;
    }

    public List<String> getPendingAdminAccept() {
        return pendingAdminAccept;
    }

    public void setPendingAdminAccept(List<String> pendingAdminAccept) {
        this.pendingAdminAccept = pendingAdminAccept;
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

    public String getEventPicture() {
        return eventPicture;
    }

    public void setEventPicture(String eventPicture) {
        this.eventPicture = eventPicture;
    }

    @Override
    public void wantToParticipate(String userId) {
        addToGoingUsers(userId);
    }

    @Override
    public void setIsShareable() {
        this.setShareable(false);
    }

    @Override
    public void setIsByAdminAccept() {
        this.setByAdminAccept(false);
    }

    @Override
    public void setIsLimited(int numberOfParticipants) {
        this.setLimited(false);
        this.setNumberOfParticipants(1000);
    }

    public void addToGoingUsers(String userId) {
        if (userId.equals("None") && this.goingUsers.size() == 0)
            this.goingUsers.add(userId);
        else if (this.goingUsers.get(0).equals("None") && !userId.equals("None")) {
            this.goingUsers.clear();
            this.goingUsers.add(userId);
        } else if (!this.goingUsers.get(0).equals("None") && !this.goingUsers.contains(userId)) {
            this.goingUsers.add(userId);
        }
    }

    public void addToDeclinedUers(String userId) {
        if (userId.equals("None") && this.declinedUsers.size() == 0)
            this.declinedUsers.add(userId);
        else if (this.declinedUsers.get(0).equals("None") && !userId.equals("None")) {
            this.declinedUsers.clear();
            this.declinedUsers.add(userId);
        } else if (!this.declinedUsers.get(0).equals("None") && !this.declinedUsers.contains(userId)) {
            this.declinedUsers.add(userId);
        }
    }

    public void addToPendingAdminAccept(String userId) {
        if (userId.equals("None") && this.pendingAdminAccept.size() == 0)
            this.pendingAdminAccept.add(userId);
        else if (this.pendingAdminAccept.get(0).equals("None") && !userId.equals("None")) {
            this.pendingAdminAccept.clear();
            this.pendingAdminAccept.add(userId);
        } else if (!this.pendingAdminAccept.get(0).equals("None") && !this.pendingAdminAccept.contains(userId)) {
            this.pendingAdminAccept.add(userId);
        }
    }

}
