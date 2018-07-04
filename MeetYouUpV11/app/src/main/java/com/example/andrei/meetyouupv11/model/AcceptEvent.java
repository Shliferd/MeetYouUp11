package com.example.andrei.meetyouupv11.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AcceptEvent extends EventDecorator {

    public AcceptEvent(BasicEvent basicEvent) {
        super(basicEvent);
    }

    @Override
    public void setIsByAdminAccept() {
        this.basicEvent.setByAdminAccept(true);
    }


    @Override
    public void wantToParticipate(String userId) {
        this.basicEvent.addToPendingAdminAccept(userId);
    }
}
