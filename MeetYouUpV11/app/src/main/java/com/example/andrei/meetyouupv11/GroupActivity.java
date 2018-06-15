package com.example.andrei.meetyouupv11;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.model.Group;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAddGroup, btnSeeYourGroups;
    private RecyclerView recyclerViewForGroup;
    private GroupAdapter groupAdapter;

    private DatabaseReference mDatabase;
    private DatabaseReference getmDatabaseProfile;
    private FirebaseAuth firebaseAuth;
    private List<Group> myGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups");
        getmDatabaseProfile = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseAuth.getCurrentUser().getUid());

        btnAddGroup = findViewById(R.id.btnAddGroup);
        btnSeeYourGroups = findViewById(R.id.btnSeeYourGroups);

//        recyclerViewForGroup = findViewById(R.id.recyclerViewForGroup);
//        recyclerViewForGroup.setLayoutManager(new LinearLayoutManager(this));

        this.addGroups();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupActivity.this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerViewForGroup = (RecyclerView) findViewById(R.id.recyclerViewForGroup);
        recyclerViewForGroup.setLayoutManager(linearLayoutManager);
        recyclerViewForGroup.setHasFixedSize(false);
        GroupAdapter groupAdapter = new GroupAdapter(this, myGroups);
        recyclerViewForGroup.setAdapter(groupAdapter);

//        GroupAdapter groupAdapter = new GroupAdapter(GroupActivity.this, myGroups);
//        recyclerViewForGroup.setAdapter(groupAdapter);

        btnAddGroup.setOnClickListener(this);
    }

    private void addGroups() {
        getmDatabaseProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile newProfile = dataSnapshot.getValue(Profile.class);
                for (String groupId : newProfile.getListOfGroups()) {

                    DatabaseReference myReference = mDatabase.child(groupId);
                    myReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Group group = dataSnapshot.getValue(Group.class);
                            myGroups.add(group);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddGroup) {
            startActivity(new Intent(GroupActivity.this, CreateGroupActivity.class));
        } else if (v.getId() == R.id.btnSeeYourGroups) {

        }
    }
}
