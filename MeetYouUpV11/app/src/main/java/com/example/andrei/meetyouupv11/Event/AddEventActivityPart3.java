package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.Dashboard.DashboardActivity;
import com.example.andrei.meetyouupv11.Group.AddPeopleToGroupActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Event;
import com.example.andrei.meetyouupv11.model.Profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

import java.util.ArrayList;
import java.util.List;

public class AddEventActivityPart3 extends AppCompatActivity {

    //widgets
    private Button btnFinishAddPeopleToEvent;
    private EditText searchPeopleToEvent;
    private ImageButton imageButtonSearchAddPeopleEvent;
    private RecyclerView recyclerViewAddPeopleToEvent;

    //vars
    private String eventId;
    private DatabaseReference profilesReference;
    private DatabaseReference eventsReferences;
    private FirebaseAuth firebaseAuth;
    private List<String> listOfPeopleToBeAdded = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_part3);

        Intent newIntent = getIntent();
        eventId = newIntent.getStringExtra(AddEventActivity.EVENT_ID);

        //firebase
        profilesReference = FirebaseDatabase.getInstance().getReference().child("profiles");
        eventsReferences = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("basicEvent");
        firebaseAuth = FirebaseAuth.getInstance();

        btnFinishAddPeopleToEvent = findViewById(R.id.btnFinishAddPeopleToEvent);
        searchPeopleToEvent = findViewById(R.id.searchPeopleToEvent);
        imageButtonSearchAddPeopleEvent = findViewById(R.id.imageButtonSearchAddPeopleEvent);
        recyclerViewAddPeopleToEvent = findViewById(R.id.recyclerViewAddPeopleToEvent);
        recyclerViewAddPeopleToEvent.setHasFixedSize(true);
        recyclerViewAddPeopleToEvent.setLayoutManager(new LinearLayoutManager(this));

        imageButtonSearchAddPeopleEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseSearch(searchPeopleToEvent.getText().toString());
            }
        });

        btnFinishAddPeopleToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCreatingEvent();
            }
        });

    }

    private void finishCreatingEvent() {

        eventsReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BasicEvent basicEvent = dataSnapshot.getValue(BasicEvent.class);
                basicEvent.wantToParticipate(firebaseAuth.getCurrentUser().getUid());

                eventsReferences.setValue(basicEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listOfPeopleToBeAdded.add(firebaseAuth.getCurrentUser().getUid());
                        for (String userId : listOfPeopleToBeAdded) {
                            final DatabaseReference myRef = profilesReference.child(userId);
                            if (userId.equals(firebaseAuth.getCurrentUser().getUid())) {
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Profile adminProfile = dataSnapshot.getValue(Profile.class);
                                        adminProfile.addEventsAttendance(eventId);
                                        myRef.setValue(adminProfile);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Profile userProfile = dataSnapshot.getValue(Profile.class);
                                        userProfile.addToPendingEventsList(eventId);
                                        myRef.setValue(userProfile);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        startActivity(new Intent(AddEventActivityPart3.this, DashboardActivity.class));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void firebaseSearch(String searchText) {
        Query firebaseSearchQuery = profilesReference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        final FirebaseRecyclerOptions<Profile> options = new FirebaseRecyclerOptions.Builder<Profile>().setQuery(firebaseSearchQuery, Profile.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProfileViewHolder holder, int position, @NonNull final Profile model) {
                holder.nameAddPeopleToGroup.setText(model.getName());
                Uri myUri = Uri.parse(model.getProfilePictureUrl());
                Picasso.get().load(myUri).fit().centerCrop().into(holder.profilePicAddToGroup);

                if (model.getUserId().equals(firebaseAuth.getCurrentUser().getUid())) {
                    holder.iconAddPeopleToGroup.setVisibility(holder.itemView.INVISIBLE);
                }

                if (listOfPeopleToBeAdded.contains(model.getUserId())) {
                    holder.iconAddPeopleToGroup.setVisibility(holder.itemView.INVISIBLE);
                }

                if (model.getPendingEvents().contains(eventId)) {
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

            @NonNull
            @Override
            public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_add_people_to_group, null);
                return new AddEventActivityPart3.ProfileViewHolder(view);
            }
        };

        recyclerViewAddPeopleToEvent.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView nameAddPeopleToGroup;
        ImageView profilePicAddToGroup;
        ImageView iconAddPeopleToGroup;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameAddPeopleToGroup = (TextView) itemView.findViewById(R.id.nameAddPeopleToGroup);
            profilePicAddToGroup = (ImageView) itemView.findViewById(R.id.profilePicAddToGroup);
            iconAddPeopleToGroup = (ImageView) itemView.findViewById(R.id.iconAddPeopleToGroup);

        }
    }
}
