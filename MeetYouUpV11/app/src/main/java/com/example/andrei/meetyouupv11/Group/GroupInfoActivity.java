package com.example.andrei.meetyouupv11.Group;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.Utils.Adapters.EventAdapter;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupInfoActivity extends AppCompatActivity {

    //widgets
    private ImageView groupInfoImageView;
    private TextView groupInfoName, groupInfoDesc, groupInfoNumberOfMembers, groupInfoSeeMembers;
    private RecyclerView recyclerViewGroupInfoWithEvents;
    private EventAdapter eventAdapter;
    private String groupId;

    //vars
    private FirebaseAuth firebaseAuth;
    private DatabaseReference groupRef, eventsRef, profilesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent newIntent = getIntent();
        groupId = newIntent.getStringExtra("groupId");
        firebaseAuth = FirebaseAuth.getInstance();
        groupRef = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId);
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        profilesRef = FirebaseDatabase.getInstance().getReference().child("profiles");

        groupInfoImageView = findViewById(R.id.groupInfoImageView);
        groupInfoName = findViewById(R.id.groupInfoName);
        groupInfoDesc = findViewById(R.id.groupInfoDesc);
        groupInfoNumberOfMembers = findViewById(R.id.groupInfoNumberOfMembers);
        groupInfoSeeMembers = findViewById(R.id.groupInfoSeeMembers);
        recyclerViewGroupInfoWithEvents = findViewById(R.id.recyclerViewGroupInfoWithEvents);
        recyclerViewGroupInfoWithEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        recyclerViewGroupInfoWithEvents.setAdapter(eventAdapter);

        init();

        populateGroupWithEvents();

        groupInfoSeeMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(GroupInfoActivity.this, ViewGroupMembersActivity.class);
                newIntent.putExtra("groupId", groupId);
                startActivity(newIntent);
            }
        });


    }

    private void init() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);

                Uri uri = Uri.parse(group.getGroupPicture());
                Picasso.get().load(uri).fit().centerCrop().into(groupInfoImageView);

                groupInfoName.setText(group.getGroupName());
                groupInfoDesc.setText(group.getGroupDescription());
                groupInfoNumberOfMembers.setText(group.getNumberOfMembers() + " members");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateGroupWithEvents() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                if (group.getListOfEventsInGroup() != null && !group.getListOfEventsInGroup().get(0).equals("None")) {
                    for (String eventId : group.getListOfEventsInGroup()) {
                        DatabaseReference myRef = eventsRef.child(eventId).child("basicEvent");
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
