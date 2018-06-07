package com.example.andrei.meetyouupv11.model;

public class EventDecorator implements Event {

    protected BasicEvent basicEvent;

    public EventDecorator(BasicEvent basicEvent) {
        this.basicEvent = basicEvent;
    }


    @Override
    public void addToParticipants(String userId) {
        this.basicEvent.addToParticipants(userId);
    }

    public BasicEvent getBasicEvent() {
        return basicEvent;
    }
}
