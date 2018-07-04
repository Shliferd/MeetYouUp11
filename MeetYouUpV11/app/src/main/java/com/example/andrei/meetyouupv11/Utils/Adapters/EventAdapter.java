package com.example.andrei.meetyouupv11.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.Event.EventInfoActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Event;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<BasicEvent> eventList;
    private Context mCtx;
    private DatabaseReference profileRef;
    private String eventAdministratorId;

    public EventAdapter() {
        this.eventList = new ArrayList<>();
    }

    public void addEvent(BasicEvent event) {
        eventList.add(event);
        notifyItemInserted(eventList.size());
    }

    public void setEventAdministratorId(String id) {
        this.eventAdministratorId = id;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_layout_events, null);
        mCtx = parent.getContext();
        return new EventViewHolder(view);
    }

    public void clear() {
        final int size = eventList.size();
        eventList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public List<BasicEvent> getListOfEvents() {
        return this.eventList;
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {
        Uri uri = Uri.parse(eventList.get(position).getEventPicture());
        Picasso.get().load(uri).fit().centerCrop().into(holder.imageViewEvent);

        holder.textViewEventName.setText(eventList.get(position).getEventName());
        String userCreatorId = eventList.get(position).getUserCreatorId();
        profileRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(userCreatorId);
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                holder.textViewEventAdmin.setText("Created by: " + profile.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.textViewEventDate.setText("On: " + eventList.get(position).getEventDate());

        holder.relLayoutEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(mCtx, EventInfoActivity.class);
                newIntent.putExtra("eventId", eventList.get(position).getEventId());
                mCtx.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewEvent;
        TextView textViewEventName, textViewEventAdmin, textViewEventDate;
        RelativeLayout relLayoutEvents;

        public EventViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = itemView.findViewById(R.id.imageViewEvent);
            textViewEventName = itemView.findViewById(R.id.textViewEventName);
            textViewEventAdmin = itemView.findViewById(R.id.textViewEventAdmin);
            textViewEventDate = itemView.findViewById(R.id.textViewEventDate);
            relLayoutEvents = itemView.findViewById(R.id.relLayoutEvents);
        }
    }
}
