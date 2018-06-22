package com.example.andrei.meetyouupv11.Utils.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.Group;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context mCtx;
    private List<Group> myGroups;

    public GroupAdapter(List<Group> groups) {
        //this.mCtx = mCtx;
        this.myGroups = new ArrayList<>(groups);
    }

    public void addGroup(Group group) {
        this.myGroups.add(group);
        notifyItemInserted(myGroups.size());
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("My tag", "onCreateViewHolder called");
        mCtx = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.list_layout_group, null);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Uri uri = Uri.parse(myGroups.get(position).getGroupPicture());
        Picasso.get().load(uri).fit().centerCrop().into(holder.groupProfileImageView);

        holder.groupNameTextView.setText(myGroups.get(position).getGroupName());

        holder.parentLayoutGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "Not implemented yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.myGroups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView groupNameTextView;
        ImageView groupProfileImageView;
        RelativeLayout parentLayoutGroup;

        public GroupViewHolder(View itemView) {
            super(itemView);

            groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
            groupProfileImageView = itemView.findViewById(R.id.groupProfileImageView);
            parentLayoutGroup = itemView.findViewById(R.id.parentLayoutGroup);
        }
    }
}
