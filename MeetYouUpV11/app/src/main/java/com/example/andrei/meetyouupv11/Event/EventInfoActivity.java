package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.Dashboard.DashboardActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.AcceptEvent;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Group;
import com.example.andrei.meetyouupv11.model.LimitedNumberEvent;
import com.example.andrei.meetyouupv11.model.Profile;
import com.example.andrei.meetyouupv11.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EventInfoActivity extends AppCompatActivity {

    //widgets
    private ImageView eventInfoCoverImage;
    private TextView eventInfoName, joinTextView, declineTextView, seeChoiceTextView,
            eventInfoDesc, eventInfoDate, eventInfoKeyWords, eventInfoLocation,
            eventInfoCurrentMemebers, eventInfoSeeMembers, eventInfoLimitNumber, inviteTextView, shareTextView, eventInfoManage;
    private LinearLayout linearLayInvite;
    private Button btnDeleteEvent;

    //vars
    private FirebaseAuth firebaseAuth;
    private DatabaseReference eventRef, profilesRef;
    private String eventId;
    private Profile profile;
    private BasicEvent basicEvent;
    private String userCreatorEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Intent newIntent = getIntent();
        eventId = newIntent.getStringExtra("eventId");
        firebaseAuth = FirebaseAuth.getInstance();
        eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
        profilesRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(firebaseAuth.getCurrentUser().getUid());

        eventInfoCoverImage = findViewById(R.id.eventInfoCoverImage);
        eventInfoName = findViewById(R.id.eventInfoName);
        eventInfoDesc = findViewById(R.id.eventInfoDesc);
        eventInfoDate = findViewById(R.id.eventInfoDate);
        eventInfoKeyWords = findViewById(R.id.eventInfoKeyWords);
        eventInfoLocation = findViewById(R.id.eventInfoLocation);
        eventInfoCurrentMemebers = findViewById(R.id.eventInfoCurrentMemebers);
        eventInfoSeeMembers = findViewById(R.id.eventInfoSeeMembers);
        eventInfoLimitNumber = findViewById(R.id.eventInfoLimitNumber);
        eventInfoManage = findViewById(R.id.eventInfoManage);
        inviteTextView = findViewById(R.id.inviteTextView);
        shareTextView = findViewById(R.id.shareTextView);
        joinTextView = findViewById(R.id.joinTextView);
        declineTextView = findViewById(R.id.declineTextView);
        seeChoiceTextView = findViewById(R.id.seeChoiceTextView);
        linearLayInvite = findViewById(R.id.linearLayInvite);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        init();

        if (profile.getUserId().equals(userCreatorEvent)) {
            btnDeleteEvent.setVisibility(View.VISIBLE);
        } else {
            btnDeleteEvent.setVisibility(View.GONE);
        }

        declineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineClicked();
            }
        });

        joinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClicked();
            }
        });

        eventInfoManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventInfoActivity.this, ManageByAdminAcceptActivity.class);
                newIntent.putExtra("eventId", eventId);
                startActivity(newIntent);
            }
        });

        btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("events");
                myRef.child(eventId).removeValue();

                myRef = FirebaseDatabase.getInstance().getReference().child("profiles");
                final DatabaseReference finalMyRef1 = myRef;

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Profile profile = dataSnapshot1.getValue(Profile.class);

                            if (profile.getDeclinedEvents().contains(eventId))
                                profile.removeFromDeclinedEvents(eventId);
                            if (profile.getPendingEvents().contains(eventId))
                                profile.removeFromPendingEvents(eventId);
                            if (profile.getListOfEvents().contains(eventId))
                                profile.removeFromlistOfEvents(eventId);

                            finalMyRef1.child(profile.getUserId()).setValue(profile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                myRef = FirebaseDatabase.getInstance().getReference().child("groups");
                final DatabaseReference finalMyRef2 = myRef;

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Group group = dataSnapshot1.getValue(Group.class);

                            if (group.getListOfEventsInGroup().contains(eventId))
                                group.removeFromListOfGroups(eventId);

                            finalMyRef2.child(group.getGroupId()).setValue(group);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                startActivity(new Intent(EventInfoActivity.this, DashboardActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void joinClicked() {
        if (!basicEvent.getGoingUsers().contains(profile.getUserId())) {
            if (profile.getPendingEvents().contains(eventId)) {
                List<String> pendingEvents = new ArrayList<>(profile.getPendingEvents());
                pendingEvents.remove(eventId);
                if (pendingEvents.isEmpty()) {
                    pendingEvents.add("None");
                }
                profile.setPendingEvents(pendingEvents);
                profilesRef.setValue(profile);
            }

            if (!basicEvent.isShareable()) {
                joinTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                seeChoiceTextView.setVisibility(View.VISIBLE);
                seeChoiceTextView.setText("GOING");
                seeChoiceTextView.setBackgroundColor(Color.parseColor("#588fe8"));
                linearLayInvite.setVisibility(View.VISIBLE);

                profile.addEventsAttendance(eventId);
                profilesRef.setValue(profile);
                basicEvent.wantToParticipate(profile.getUserId());
                eventRef.child("basicEvent").setValue(basicEvent);
            } else {
                if (basicEvent.isByAdminAccept()) {
                    joinTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                    seeChoiceTextView.setVisibility(View.VISIBLE);
                    seeChoiceTextView.setText("PENDING");
                    seeChoiceTextView.setBackgroundColor(Color.parseColor("#588fe8"));
                    linearLayInvite.setVisibility(View.VISIBLE);

                    AcceptEvent acceptEvent = new AcceptEvent(basicEvent);
                    acceptEvent.wantToParticipate(profile.getUserId());
                    eventRef.child("basicEvent").setValue(acceptEvent.getBasicEvent());
                } else if (basicEvent.isLimited()) {
                    if (basicEvent.getGoingUsers().size() + 1 <= basicEvent.getNumberOfParticipants()) {
                        joinTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                        seeChoiceTextView.setVisibility(View.VISIBLE);
                        seeChoiceTextView.setText("GOING");
                        seeChoiceTextView.setBackgroundColor(Color.parseColor("#588fe8"));
                        linearLayInvite.setVisibility(View.VISIBLE);

                        profile.addEventsAttendance(eventId);
                        profilesRef.setValue(profile);
                        LimitedNumberEvent limitedNumberEvent = new LimitedNumberEvent(basicEvent);
                        limitedNumberEvent.wantToParticipate(profile.getUserId());
                        eventRef.child("basicEvent").setValue(limitedNumberEvent.getBasicEvent());
                    } else {
                        seeChoiceTextView.setVisibility(View.GONE);
                        linearLayInvite.setVisibility(View.GONE);
                        Toast.makeText(EventInfoActivity.this, "The maximum limit of users to participate in this event has reached!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    joinTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                    seeChoiceTextView.setVisibility(View.VISIBLE);
                    seeChoiceTextView.setText("GOING");
                    seeChoiceTextView.setBackgroundColor(Color.parseColor("#588fe8"));
                    linearLayInvite.setVisibility(View.VISIBLE);

                    profile.addEventsAttendance(eventId);
                    profilesRef.setValue(profile);
                    basicEvent.wantToParticipate(profile.getUserId());
                    eventRef.child("basicEvent").setValue(basicEvent);
                }
            }
        }
    }

    private void declineClicked() {
        if (!profile.getDeclinedEvents().contains(eventId)) {
            declineTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
            seeChoiceTextView.setVisibility(View.VISIBLE);
            seeChoiceTextView.setText("NOT GOING");
            seeChoiceTextView.setBackgroundColor(Color.parseColor("#e52020"));
            linearLayInvite.setVisibility(View.GONE);

            if (profile.getPendingEvents().contains(eventId)) {
                List<String> pendingEvents = new ArrayList<>(profile.getPendingEvents());
                pendingEvents.remove(eventId);
                if (pendingEvents.isEmpty()) {
                    pendingEvents.add("None");
                }
                profile.setPendingEvents(pendingEvents);
                profile.addDeclinedEvent(eventId);

                profilesRef.setValue(profile);

            } else if (profile.getListOfEvents().contains(eventId)) {
                List<String> goignEvents = new ArrayList<>(profile.getListOfEvents());
                goignEvents.remove(eventId);
                if (goignEvents.isEmpty()) {
                    goignEvents.add("None");
                }
                profile.setListOfEvents(goignEvents);
                profile.addDeclinedEvent(eventId);

                profilesRef.setValue(profile);

            } else {
                profile.addDeclinedEvent(eventId);
                profilesRef.setValue(profile);
            }
        }
    }

    private void init() {
        profilesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);

                if (profile.getPendingEvents().contains(eventId)) {
                    seeChoiceTextView.setVisibility(View.GONE);
                    linearLayInvite.setVisibility(View.GONE);
                } else if (profile.getDeclinedEvents().contains(eventId)) {
                    linearLayInvite.setVisibility(View.GONE);
                    declineTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                    seeChoiceTextView.setVisibility(View.VISIBLE);
                    seeChoiceTextView.setText("NOT GOING");
                    seeChoiceTextView.setBackgroundColor(Color.parseColor("#e52020"));
                } else if (profile.getListOfEvents().contains(eventId)) {
                    linearLayInvite.setVisibility(View.VISIBLE);
                    joinTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                    seeChoiceTextView.setVisibility(View.VISIBLE);
                    seeChoiceTextView.setText("GOING");
                    seeChoiceTextView.setBackgroundColor(Color.parseColor("#588fe8"));
                } else {

                    eventRef.child("basicEvent").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BasicEvent basicEvent = dataSnapshot.getValue(BasicEvent.class);

                            userCreatorEvent = basicEvent.getUserCreatorId();

                            if (basicEvent.getPendingAdminAccept().contains(profile.getUserId())) {
                                joinTextView.setBackgroundColor(Color.parseColor("#eaf3f9"));
                                seeChoiceTextView.setVisibility(View.VISIBLE);
                                seeChoiceTextView.setText("PENDING");
                                seeChoiceTextView.setBackgroundColor(Color.parseColor("#588fe8"));
                                linearLayInvite.setVisibility(View.VISIBLE);
                            } else {
                                linearLayInvite.setVisibility(View.GONE);
                                seeChoiceTextView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                eventRef.child("basicEvent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        basicEvent = dataSnapshot.getValue(BasicEvent.class);

                        if (firebaseAuth.getCurrentUser().getUid().equals(basicEvent.getUserCreatorId()) &&
                                basicEvent.isByAdminAccept()) {
                            eventInfoManage.setVisibility(View.VISIBLE);
                        }

                        eventInfoName.setText(basicEvent.getEventName());
                        Uri uri = Uri.parse(basicEvent.getEventPicture());
                        Picasso.get().load(uri).fit().centerCrop().into(eventInfoCoverImage);
                        eventInfoDesc.setText(basicEvent.getEventDescription());
                        eventInfoDate.setText("Due to:  " + basicEvent.getEventDate());
                        eventInfoKeyWords.setText(basicEvent.getEventKeyWords());
                        eventInfoLocation.setText(basicEvent.getLocationTitle());
                        eventInfoCurrentMemebers.setText(basicEvent.getGoingUsers().size() + " going members");

                        if (basicEvent.isLimited()) {
                            eventInfoLimitNumber.setVisibility(View.VISIBLE);
                            eventInfoLimitNumber.setText("For this event the maximum limit number of" +
                                    " participants is:  " + basicEvent.getNumberOfParticipants());
                        } else {
                            eventInfoLimitNumber.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
