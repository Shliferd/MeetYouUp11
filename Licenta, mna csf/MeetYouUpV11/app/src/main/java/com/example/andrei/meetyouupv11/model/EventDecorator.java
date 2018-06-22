package com.example.andrei.meetyouupv11.model;

public class EventDecorator implements Event {

    protected BasicEvent basicEvent;

    public EventDecorator(BasicEvent basicEvent) {
        this.basicEvent = basicEvent;
    }

    public BasicEvent getBasicEvent() {
        return basicEvent;
    }

    @Override
    public void wantToParticipate(String userId) {
        this.basicEvent.wantToParticipate(userId);
    }

    @Override
    public void setIsShareable() {
        this.basicEvent.setIsShareable();
    }

    @Override
    public void setNumberLimit(int numberLimit) {
        this.basicEvent.setNumberOfParticipants(numberLimit);
    }
}
