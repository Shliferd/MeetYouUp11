package com.example.andrei.meetyouupv11.model;

import android.net.Uri;

import java.util.List;

public class Profile {

    private String userId;
    private String surname;
    private String forename;
    private String keyWords;
    private String dateOfBirth;
    private String profileDescription;
    private Uri profilePictureUrl;
    private List<String> friendsId;
    private float rating;

    public Profile() {
    }

    public Profile(String userId, String surname, String forename,
                   String keyWords, String dateOfBirth, String profileDescription,
                   Uri profilePictureUrl, List<String> friendsId, float rating) {
        this.userId = userId;
        this.surname = surname;
        this.forename = forename;
        this.keyWords = keyWords;
        this.dateOfBirth = dateOfBirth;
        this.profileDescription = profileDescription;
        this.profilePictureUrl = profilePictureUrl;
        this.friendsId = friendsId;
        this.rating = rating;
    }

    public Profile(String userId, String surname, String forename,
                   String keyWords, String dateOfBirth, String profileDescription, Uri profilePictureUrl) {
        this.userId = userId;
        this.surname = surname;
        this.forename = forename;
        this.keyWords = keyWords;
        this.dateOfBirth = dateOfBirth;
        this.profileDescription = profileDescription;
        this.profilePictureUrl = profilePictureUrl;
        this.friendsId = null;
        this.rating = 0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String profileId) {
        this.userId = profileId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
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

    public Uri getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(Uri profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<String> getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(List<String> friendsId) {
        this.friendsId = friendsId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
