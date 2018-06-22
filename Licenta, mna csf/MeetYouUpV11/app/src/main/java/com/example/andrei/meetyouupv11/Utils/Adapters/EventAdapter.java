package com.example.andrei.meetyouupv11.Utils.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.BasicEvent;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<BasicEvent> eventList = new ArrayList<>();

    public EventAdapter(List<BasicEvent> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_layout_events, null);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewEvent;
        TextView textViewEventName, textViewEventAdmin, textViewEventDate;

        public EventViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = itemView.findViewById(R.id.imageViewEvent);
            textViewEventName = itemView.findViewById(R.id.textViewEventName);
            textViewEventAdmin = itemView.findViewById(R.id.textViewEventAdmin);
            textViewEventDate = itemView.findViewById(R.id.textViewEventDate);
        }
    }
}
