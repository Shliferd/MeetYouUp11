package com.example.andrei.meetyouupv11;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        String currentRowString = dashboardRowNames[position];
        holder.rowDescTextView.setText(currentRowString);
        if (position == 0)
            holder.thumbnailImage.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.planner));
        if (position == 1)
            holder.thumbnailImage.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.groupsdashboard));
        if (position == 2)
            holder.thumbnailImage.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.myevents));
        if (position == 3)
            holder.thumbnailImage.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.searchevent));
    }

    @Override
    public int getItemCount() {
        return this.dashboardRowNames.length;
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImage;
        TextView rowDescTextView;

        public DashboardViewHolder(View itemView) {
            super(itemView);

            thumbnailImage = itemView.findViewById(R.id.thumbnailImage);
            rowDescTextView = itemView.findViewById(R.id.rowDescTextView);
        }
    }
}
