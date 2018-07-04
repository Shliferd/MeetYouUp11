package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.Utils.Adapters.EventAdapter;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class SeePendingEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSeePendingEvents;

    private DatabaseReference profileRef, eventsRef;
    private FirebaseAuth firebaseAuth;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_pending_events);

        firebaseAuth = FirebaseAuth.getInstance();
        profileRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(firebaseAuth.getCurrentUser().getUid());
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        recyclerViewSeePendingEvents = findViewById(R.id.recyclerViewSeePendingEvents);
        recyclerViewSeePendingEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        recyclerViewSeePendingEvents.setAdapter(eventAdapter);

        addEventsToPendingList();
    }

    private void addEventsToPendingList() {
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile userProfile = dataSnapshot.getValue(Profile.class);
                if (!userProfile.getPendingEvents().isEmpty())
                    if (!userProfile.getPendingEvents().contains("None")) {
                        for (String eventId : userProfile.getPendingEvents()) {
                            DatabaseReference myRef = eventsRef.child(eventId).child("basicEvent");
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    BasicEvent basicEvent = dataSnapshot.getValue(BasicEvent.class);
                                    eventAdapter.addEvent(basicEvent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
