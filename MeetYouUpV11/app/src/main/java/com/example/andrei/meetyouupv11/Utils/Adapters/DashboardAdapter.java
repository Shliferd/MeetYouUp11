package com.example.andrei.meetyouupv11.Utils.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.Event.AddEventActivity;
import com.example.andrei.meetyouupv11.Event.AddEventActivityPart2;
import com.example.andrei.meetyouupv11.Event.EventsToAttendActivity;
import com.example.andrei.meetyouupv11.Event.YourEventsActivity;
import com.example.andrei.meetyouupv11.Group.GroupActivity;
import com.example.andrei.meetyouupv11.R;
import com.squareup.picasso.Picasso;

/*
RecyclerView.Adapter
RecyclerView.ViewHolder
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private Context mCtx;
    private String[] dashboardRowNames;

    public DashboardAdapter(Context mCtx, String[] dashboardRowNames) {
        this.mCtx = mCtx;
        this.dashboardRowNames = dashboardRowNames;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.list_layout_dashboard, null);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardViewHolder holder, final int position) {
        String currentRowString = dashboardRowNames[position];
        holder.rowDescTextView.setText(currentRowString);

        switch (position) {
            case 0:
                Picasso.get().load(R.drawable.planner).fit().centerCrop().into(holder.thumbnailImage);
                break;
            case 1:
                Picasso.get().load(R.drawable.groupdashboard).fit().centerCrop().into(holder.thumbnailImage);
                break;
            case 2:
                Picasso.get().load(R.drawable.myevents).fit().centerCrop().into(holder.thumbnailImage);
                break;
            case 3:
                Picasso.get().load(R.drawable.searchevent).fit().centerCrop().into(holder.thumbnailImage);
                break;
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        mCtx.startActivity(new Intent(mCtx, EventsToAttendActivity.class));
                        break;
                    case 1:
                        mCtx.startActivity(new Intent(mCtx, GroupActivity.class));
                        break;
                    case 2:
                        mCtx.startActivity(new Intent(mCtx, YourEventsActivity.class));
                        break;
                    case 3:
                        Toast.makeText(mCtx, "Not implemeted yet!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dashboardRowNames.length;
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImage;
        TextView rowDescTextView;
        RelativeLayout parentLayout;

        public DashboardViewHolder(View itemView) {
            super(itemView);

            thumbnailImage = itemView.findViewById(R.id.thumbnailImage);
            rowDescTextView = itemView.findViewById(R.id.rowDescTextView);
            parentLayout = itemView.findViewById(R.id.parentLayoutDashboard);
        }
    }
}
