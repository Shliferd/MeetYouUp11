package com.example.andrei.meetyouupv11.Group;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.meetyouupv11.Dashboard.DashboardActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.Group;
import com.example.andrei.meetyouupv11.model.Profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddPeopleToGroupActivity extends AppCompatActivity {

    private EditText searchPeopleToGroup;
    private ImageButton imageButtonSearchAddPeople;
    private RecyclerView recyclerViewAddPeopleToGroup;
    private Button btnFinishAddPeopleToGroup;

    private static String groupId;
    private static String loggedUserId;
    private List<String> listOfPeopleToBeAdded = new ArrayList<>();

    private DatabaseReference databaseReferenceProfile;
    private DatabaseReference databaseReferenceGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people_to_group);

        Intent getIntent = getIntent();
        groupId = getIntent.getStringExtra("groupId");
        loggedUserId = getIntent.getStringExtra("loggedUserId");

        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference("profiles");
        databaseReferenceGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupId);

        searchPeopleToGroup = findViewById(R.id.searchPeopleToGroup);
        imageButtonSearchAddPeople = findViewById(R.id.imageButtonSearchAddPeople);
        btnFinishAddPeopleToGroup = findViewById(R.id.btnFinishAddPeopleToGroup);
        recyclerViewAddPeopleToGroup = findViewById(R.id.recyclerViewAddPeopleToGroup);
        recyclerViewAddPeopleToGroup.setHasFixedSize(true);
        recyclerViewAddPeopleToGroup.setLayoutManager(new LinearLayoutManager(this));


        imageButtonSearchAddPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = searchPeopleToGroup.getText().toString();
                firebaseProfileSearch(searchText);
            }
        });

        btnFinishAddPeopleToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSettingUpTheGroup();
            }
        });


    }

    private void finishSettingUpTheGroup() {
        databaseReferenceGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group newGroup = dataSnapshot.getValue(Group.class);
                newGroup.addMembersToGroup(loggedUserId);
                for (String userId : listOfPeopleToBeAdded) {
                    newGroup.addMembersToGroup(userId);
                }

                newGroup.setNumberOfMembers(listOfPeopleToBeAdded.size() + 1);

                databaseReferenceGroup.setValue(newGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        listOfPeopleToBeAdded.add(loggedUserId);
                        for (String userId : listOfPeopleToBeAdded) {
                            final DatabaseReference myRef = databaseReferenceProfile.child(userId);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Profile newProfile = dataSnapshot.getValue(Profile.class);
                                    newProfile.addGroup(groupId);
                                    myRef.setValue(newProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        startActivity(new Intent(AddPeopleToGroupActivity.this, DashboardActivity.class));


                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void firebaseProfileSearch(String searchText) {

        Query firebaseSearchQuery = databaseReferenceProfile.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        final FirebaseRecyclerOptions<Profile> options = new FirebaseRecyclerOptions.Builder<Profile>().setQuery(firebaseSearchQuery, Profile.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(options) {
            @NonNull
            @Override
            public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_add_people_to_group, null);
                return new ProfileViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProfileViewHolder holder, int position, @NonNull final Profile model) {
                holder.nameAddPeopleToGroup.setText(model.getName());
                Uri myUri = Uri.parse(model.getProfilePictureUrl());
                Picasso.get().load(myUri).fit().centerCrop().into(holder.profilePicAddToGroup);

                if (model.getUserId().equals(loggedUserId)) {
                    holder.iconAddPeopleToGroup.setVisibility(holder.itemView.INVISIBLE);
                }

                if (listOfPeopleToBeAdded.contains(model.getUserId())) {
                    holder.iconAddPeopleToGroup.setVisibility(holder.itemView.INVISIBLE);
                }

                holder.iconAddPeopleToGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.iconAddPeopleToGroup.setVisibility(v.INVISIBLE);
                        listOfPeopleToBeAdded.add(model.getUserId());
                    }
                });
            }
        };

        recyclerViewAddPeopleToGroup.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView nameAddPeopleToGroup;
        ImageView profilePicAddToGroup;
        ImageButton iconAddPeopleToGroup;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameAddPeopleToGroup = (TextView) itemView.findViewById(R.id.nameAddPeopleToGroup);
            profilePicAddToGroup = (ImageView) itemView.findViewById(R.id.profilePicAddToGroup);
            iconAddPeopleToGroup = (ImageButton) itemView.findViewById(R.id.iconAddPeopleToGroup);

        }
    }
}
