package com.example.andrei.meetyouupv11.model;

public class ShareableEvent extends EventDecorator {
    public ShareableEvent(BasicEvent basicEvent) {
        super(basicEvent);
    }

    @Override
    public void setIsShareable() {
        this.basicEvent.setShareable(true);
    }
}
