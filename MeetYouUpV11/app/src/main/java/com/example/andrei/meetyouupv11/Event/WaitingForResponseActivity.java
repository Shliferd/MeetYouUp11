package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.Utils.Adapters.EventAdapter;
import com.example.andrei.meetyouupv11.model.BasicEvent;
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

public class WaitingForResponseActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWaitForResponseEvents;

    private DatabaseReference eventsRef;
    private FirebaseAuth firebaseAuth;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_response);

        firebaseAuth = FirebaseAuth.getInstance();
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        recyclerViewWaitForResponseEvents = findViewById(R.id.recyclerViewWaitForResponseEvents);
        recyclerViewWaitForResponseEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        recyclerViewWaitForResponseEvents.setAdapter(eventAdapter);

        populateRecycler();
    }

    private void populateRecycler() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    BasicEvent basicEvent = event.child("basicEvent").getValue(BasicEvent.class);
                    if (basicEvent.getPendingAdminAccept().contains(firebaseAuth.getCurrentUser().getUid())) {
                        eventAdapter.addEvent(basicEvent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
