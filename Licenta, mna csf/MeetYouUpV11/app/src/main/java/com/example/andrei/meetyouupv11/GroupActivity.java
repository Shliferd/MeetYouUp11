package com.example.andrei.meetyouupv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.andrei.meetyouupv11.model.Group;
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
    private RecyclerView recyclerViewGroup;
    private GroupAdapter groupAdapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Group> groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        btnAddGroup = findViewById(R.id.btnAddGroup);
        btnSeeYourGroups = findViewById(R.id.btnSeeYourGroups);

        recyclerViewGroup = findViewById(R.id.recyclerViewGroup);
        recyclerViewGroup.setHasFixedSize(true);
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("groups");

        btnAddGroup.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot groupSnaphot : dataSnapshot.getChildren()) {
                    Group group = groupSnaphot.getValue(Group.class);
                    groups.add(group);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupAdapter = new GroupAdapter(this, groups);
        recyclerViewGroup.setAdapter(groupAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddGroup) {
            startActivity(new Intent(GroupActivity.this, CreateGroupActivity.class));
        } else if (v.getId() == R.id.btnSeeYourGroups) {

        }
    }
}
