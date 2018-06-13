package com.example.andrei.meetyouupv11.model;

public class LimitedEvent extends EventDecorator {

    private int nrCrt, max_Participants;


    public LimitedEvent(BasicEvent basicEvent, int max_Participants) {
        super(basicEvent);
        this.max_Participants = max_Participants;
        this.nrCrt = 0;
    }

    @Override
    public void addToParticipants(String userId) {
        if (this.nrCrt++ < this.max_Participants) {
            super.addToParticipants(userId);
        }
    }

}
