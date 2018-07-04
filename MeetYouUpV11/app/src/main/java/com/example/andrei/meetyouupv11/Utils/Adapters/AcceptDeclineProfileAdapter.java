package com.example.andrei.meetyouupv11.Utils.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Profile;
import com.example.andrei.meetyouupv11.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AcceptDeclineProfileAdapter extends RecyclerView.Adapter<AcceptDeclineProfileAdapter.AccDeclineProfileViewHolder> {

    private Context mCtx;
    private List<Profile> myProfiles = new ArrayList<>();
    private DatabaseReference eventsRef, profileRef;
    private String eventId;

    public AcceptDeclineProfileAdapter(String eventId) {
        this.eventId = eventId;
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
        profileRef = FirebaseDatabase.getInstance().getReference().child("profiles");
    }

    public void addProfile(Profile profile) {
        myProfiles.add(profile);
        notifyItemInserted(myProfiles.size());
    }

    private void deleteProfile(int position) {
        myProfiles.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public AccDeclineProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        mCtx = layoutInflater.getContext();
        View view = layoutInflater.inflate(R.layout.list_layout_manage_people_to_event, null);
        return new AccDeclineProfileViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AccDeclineProfileViewHolder holder, final int position) {
        if (!myProfiles.isEmpty()) {

            Uri uri = Uri.parse(myProfiles.get(position).getProfilePictureUrl());
            Picasso.get().load(uri).fit().centerCrop().into(holder.profilePicAccOrNot);

            holder.nameAccDecline.setText(myProfiles.get(position).getName());

            holder.iconAccOrNotAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptClicked(position);
                }
            });

            holder.iconAccOrNotDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declinedClicked(position);
                }
            });
        }
    }

    private void declinedClicked(final int position) {
        eventsRef.child("basicEvent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BasicEvent basicEvent = dataSnapshot.getValue(BasicEvent.class);
                List<String> waitingAdminAccOrDecline = new ArrayList<>(basicEvent.getPendingAdminAccept());
                waitingAdminAccOrDecline.remove(myProfiles.get(position).getUserId());
                if (waitingAdminAccOrDecline.isEmpty()) {
                    waitingAdminAccOrDecline.add("None");
                }
                basicEvent.setPendingAdminAccept(waitingAdminAccOrDecline);
                basicEvent.addToDeclinedUers(myProfiles.get(position).getUserId());

                eventsRef.child("basicEvent").setValue(basicEvent);
                deleteProfile(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void acceptClicked(final int position) {

        eventsRef.child("basicEvent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BasicEvent basicEvent = dataSnapshot.getValue(BasicEvent.class);
                List<String> waitingAdminAccOrDecline = new ArrayList<>(basicEvent.getPendingAdminAccept());
                waitingAdminAccOrDecline.remove(myProfiles.get(position).getUserId());
                if (waitingAdminAccOrDecline.isEmpty()) {
                    waitingAdminAccOrDecline.add("None");
                }
                basicEvent.setPendingAdminAccept(waitingAdminAccOrDecline);
                basicEvent.wantToParticipate(myProfiles.get(position).getUserId());

                profileRef.child(myProfiles.get(position).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        profile.addEventsAttendance(eventId);
                        profileRef.child(profile.getUserId()).setValue(profile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                eventsRef.child("basicEvent").setValue(basicEvent);

                deleteProfile(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return myProfiles.size();
    }

    class AccDeclineProfileViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicAccOrNot, iconAccOrNotAccept, iconAccOrNotDecline;
        TextView nameAccDecline;

        public AccDeclineProfileViewHolder(View itemView) {
            super(itemView);
            profilePicAccOrNot = itemView.findViewById(R.id.profilePicAccOrNot);
            iconAccOrNotAccept = itemView.findViewById(R.id.iconAccOrNotAccept);
            iconAccOrNotDecline = itemView.findViewById(R.id.iconAccOrNotDecline);
            nameAccDecline = itemView.findViewById(R.id.nameAccOrNot);
        }
    }
}
