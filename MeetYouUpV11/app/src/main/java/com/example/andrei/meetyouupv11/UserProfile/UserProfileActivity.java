package com.example.andrei.meetyouupv11.UserProfile;

import com.example.andrei.meetyouupv11.Dashboard.DashboardActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    //widgets
    private Button userProfileGoToDashboard;
    private ImageView userProfileImage;
    private TextView userProfileName, userProfileDesc, userProfileDatOfBirth, userProfileKeyWords;

    //vars
    private DatabaseReference profileRef;
    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent newIntent = getIntent();
        profileId = newIntent.getStringExtra("profileId");
        profileRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(profileId);

        userProfileGoToDashboard = findViewById(R.id.userProfileGoToDashboard);
        userProfileImage = findViewById(R.id.userProfileImage);
        userProfileName = findViewById(R.id.userProfileName);
        userProfileDesc = findViewById(R.id.userProfileDesc);
        userProfileDatOfBirth = findViewById(R.id.userProfileDateOfBirth);
        userProfileKeyWords = findViewById(R.id.userProfileKeyWords);

        init();

        userProfileGoToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
            }
        });
    }

    private void init() {
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                Uri uri = Uri.parse(profile.getProfilePictureUrl());
                Picasso.get().load(uri).fit().centerCrop().into(userProfileImage);
                userProfileName.setText(profile.getName());
                userProfileDesc.setText(profile.getProfileDescription());
                userProfileDatOfBirth.setText("Born on:  " + profile.getDateOfBirth());
                userProfileKeyWords.setText(profile.getKeyWords());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
