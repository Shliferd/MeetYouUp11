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

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.UserProfile.UserProfileActivity;
import com.example.andrei.meetyouupv11.model.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<Profile> myProfiles;
    private Context mCtx;
    private String groupCreatorId;
    private String eventCreatorId;

    public ProfileAdapter() {
        myProfiles = new ArrayList<>();
    }

    public void setGroupCreatorId(String groupCreatorId) {
        this.groupCreatorId = groupCreatorId;
    }

    public void setEventCreatorId(String eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public void addProfile(Profile profile) {
        myProfiles.add(profile);
        notifyItemInserted(myProfiles.size());
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_layout_profile, null);
        mCtx = parent.getContext();
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, final int position) {
        Uri uri = Uri.parse(myProfiles.get(position).getProfilePictureUrl());
        Picasso.get().load(uri).fit().centerCrop().into(holder.profilePicViewMembers);
        holder.nameViewMembers.setText(myProfiles.get(position).getName());

        if (myProfiles.get(position).getUserId().equals(groupCreatorId) ||
                myProfiles.get(position).getUserId().equals(eventCreatorId)) {
            holder.showAdmin.setVisibility(View.VISIBLE);
        }

        holder.relLayoutProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(mCtx, UserProfileActivity.class);
                newIntent.putExtra("profileId", myProfiles.get(position).getUserId());
                mCtx.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myProfiles.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicViewMembers;
        TextView nameViewMembers, showAdmin;
        RelativeLayout relLayoutProfiles;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            profilePicViewMembers = itemView.findViewById(R.id.profilePicViewMembers);
            nameViewMembers = itemView.findViewById(R.id.nameViewMembers);
            relLayoutProfiles = itemView.findViewById(R.id.relLayoutProfiles);
            showAdmin = itemView.findViewById(R.id.showAdmin);
        }
    }
}
