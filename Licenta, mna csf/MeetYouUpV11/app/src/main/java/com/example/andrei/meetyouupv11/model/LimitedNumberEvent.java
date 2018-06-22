package com.example.andrei.meetyouupv11.model;

public class LimitedNumberEvent extends EventDecorator {
    public LimitedNumberEvent(BasicEvent basicEvent) {
        super(basicEvent);
    }

    @Override
    public void setNumberLimit(int numberLimit) {
        this.basicEvent.setNumberOfParticipants(numberLimit);
    }

    @Override
    public void wantToParticipate(String userId) {
        if (this.basicEvent.getGoingUsers().size() + 1 <= this.basicEvent.getNumberOfParticipants()) {
            this.basicEvent.addToGoingUsers(userId);
        }
    }
}
