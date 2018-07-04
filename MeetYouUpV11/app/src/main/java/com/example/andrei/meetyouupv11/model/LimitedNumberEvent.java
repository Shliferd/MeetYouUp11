package com.example.andrei.meetyouupv11.model;

public class LimitedNumberEvent extends EventDecorator {
    public LimitedNumberEvent(BasicEvent basicEvent) {
        super(basicEvent);
    }

    @Override
    public void setIsLimited(int nrParticipants) {
        this.basicEvent.setLimited(true);
        this.basicEvent.setNumberOfParticipants(nrParticipants);
    }

    @Override
    public void wantToParticipate(String userId) {
        if (this.basicEvent.getGoingUsers().size() + 1 <= this.basicEvent.getNumberOfParticipants()) {
            this.basicEvent.addToGoingUsers(userId);
        }
    }
}
