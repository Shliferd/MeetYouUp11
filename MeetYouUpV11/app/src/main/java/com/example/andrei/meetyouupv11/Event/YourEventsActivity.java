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

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class YourEventsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddEvent;
    RecyclerView recyclerViewYourEvents;

    //vars
    private FirebaseAuth firebaseAuth;
    private DatabaseReference eventsRef;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_events);

        firebaseAuth = FirebaseAuth.getInstance();
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        btnAddEvent = findViewById(R.id.btnAddEvent);
        recyclerViewYourEvents = findViewById(R.id.recyclerViewYourEvents);
        recyclerViewYourEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        recyclerViewYourEvents.setAdapter(eventAdapter);

        populateListOfEvents();

        btnAddEvent.setOnClickListener(this);
    }

    private void populateListOfEvents() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    BasicEvent basicEvent = eventSnapshot.child("basicEvent").getValue(BasicEvent.class);
                    if (basicEvent.getUserCreatorId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        eventAdapter.addEvent(basicEvent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddEvent) {
            startActivity(new Intent(YourEventsActivity.this, AddEventActivity.class));
        }
    }
}
