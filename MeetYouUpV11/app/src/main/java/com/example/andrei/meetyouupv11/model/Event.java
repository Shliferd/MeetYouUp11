package com.example.andrei.meetyouupv11.model;

public interface Event {

    public void wantToParticipate(String userId);

    public void setIsShareable();

    public void setIsByAdminAccept();

    public void setIsLimited(int nrParticipants);

}
