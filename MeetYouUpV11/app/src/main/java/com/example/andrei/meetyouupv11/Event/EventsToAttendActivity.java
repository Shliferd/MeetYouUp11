package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.Utils.Adapters.EventAdapter;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Event;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsToAttendActivity extends AppCompatActivity {

    //widgets
    private Button btnSeePendingEvents, btnWaitingForResponseEvents;
    private RecyclerView recyclerViewEventsToAttend;

    //vars
    private DatabaseReference profilesReference;
    private DatabaseReference eventsReference;
    private FirebaseAuth firebaseAuth;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_to_attend);

        firebaseAuth = FirebaseAuth.getInstance();
        profilesReference = FirebaseDatabase.getInstance().getReference().child("profiles").child(firebaseAuth.getCurrentUser().getUid());
        eventsReference = FirebaseDatabase.getInstance().getReference().child("events");

        btnSeePendingEvents = findViewById(R.id.btnSeePendingEvents);
        btnWaitingForResponseEvents = findViewById(R.id.btnWaitingForResponseEvents);
        recyclerViewEventsToAttend = findViewById(R.id.recyclerViewEventsToAttend);
        recyclerViewEventsToAttend.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter();
        recyclerViewEventsToAttend.setAdapter(eventAdapter);

        addEvents();

        btnSeePendingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsToAttendActivity.this, SeePendingEventsActivity.class));
            }
        });

        btnWaitingForResponseEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsToAttendActivity.this, WaitingForResponseActivity.class));
            }
        });
    }

    private void addEvents() {
        profilesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile userProfile = dataSnapshot.getValue(Profile.class);
                if (userProfile.getListOfEvents() != null)
                    if (!userProfile.getListOfEvents().contains("None")) {
                        for (String eventId : userProfile.getListOfEvents()) {
                            DatabaseReference myRef = eventsReference.child(eventId).child("basicEvent");
                            myRef.addValueEventListener(new ValueEventListener() {
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
