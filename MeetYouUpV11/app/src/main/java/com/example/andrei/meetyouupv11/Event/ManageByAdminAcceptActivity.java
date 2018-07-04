package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.Utils.Adapters.AcceptDeclineProfileAdapter;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Profile;
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

public class ManageByAdminAcceptActivity extends AppCompatActivity {

    //widgets
    private RecyclerView manageAccRecyclerView;
    private AcceptDeclineProfileAdapter adapter;

    //vars
    private DatabaseReference eventsRef, profileRef;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_by_admin_accept);

        Intent newIntent = getIntent();
        eventId = newIntent.getStringExtra("eventId");
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("basicEvent");
        profileRef = FirebaseDatabase.getInstance().getReference().child("profiles");

        manageAccRecyclerView = findViewById(R.id.manageAccRecyclerView);
        manageAccRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AcceptDeclineProfileAdapter(eventId);
        manageAccRecyclerView.setAdapter(adapter);

        addProfiles();
    }

    private void addProfiles() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BasicEvent basicEvent = dataSnapshot.getValue(BasicEvent.class);
                for (String profileId : basicEvent.getPendingAdminAccept()) {
                    if (!profileId.equals("None")) {
                        DatabaseReference myRef = profileRef.child(profileId);
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Profile profile = dataSnapshot.getValue(Profile.class);
                                adapter.addProfile(profile);
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
