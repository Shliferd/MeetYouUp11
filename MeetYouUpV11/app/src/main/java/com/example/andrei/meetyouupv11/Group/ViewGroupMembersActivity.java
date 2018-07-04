package com.example.andrei.meetyouupv11.Group;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.Utils.Adapters.ProfileAdapter;
import com.example.andrei.meetyouupv11.model.Group;
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

public class ViewGroupMembersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGroupMembers;
    private ProfileAdapter profileAdapter;
    private String groupId;

    //vars
    private DatabaseReference profilesRef, groupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_members);

        recyclerViewGroupMembers = findViewById(R.id.recyclerViewGroupMembers);
        recyclerViewGroupMembers.setLayoutManager(new LinearLayoutManager(this));
        profileAdapter = new ProfileAdapter();
        recyclerViewGroupMembers.setAdapter(profileAdapter);

        Intent newIntent = getIntent();
        groupId = newIntent.getStringExtra("groupId");

        profilesRef = FirebaseDatabase.getInstance().getReference().child("profiles");
        groupRef = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId);

        populateWithMembers();
    }

    private void populateWithMembers() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                profileAdapter.setGroupCreatorId(group.getAdminId());
                if (group.getMembersId() != null && !group.getMembersId().get(0).equals("None")) {
                    for (String profileId : group.getMembersId()) {
                        DatabaseReference myRef = profilesRef.child(profileId);
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Profile profile = dataSnapshot.getValue(Profile.class);
                                profileAdapter.addProfile(profile);
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
