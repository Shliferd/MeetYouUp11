package com.example.andrei.meetyouupv11.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AcceptEvent extends EventDecorator {

    private List<String> pendingUsers;
    private List<String> declinedUsers;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public AcceptEvent(BasicEvent basicEvent) {
        super(basicEvent);
    }

    @Override
    public void addToParticipants(String userId) {
        if (userId != firebaseAuth.getCurrentUser().getUid()) {
            //if(basicEvent.getUserCreatorId())

            pendingUsers.add(userId);
            // daca adminul a acceptat acest user atunci il adaug in lista de participants;\
            // daca nu il adaug in lista petnru declinedUsers;

        }
    }

    private void accceptByAdmin(String userId, boolean acc) {
        if (acc) {
            super.addToParticipants(userId);
        } else {
            pendingUsers.remove(userId);
            declinedUsers.add(userId);
        }
    }
}
