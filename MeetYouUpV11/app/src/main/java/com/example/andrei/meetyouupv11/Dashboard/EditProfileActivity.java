package com.example.andrei.meetyouupv11.Dashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEditProfileName, editTextEditProfileDescription,
            editTextEditProfileKeyWords;
    private TextView textViewEditProfileDateOfBirth;
    private int mYear, mMonth, mDay;
    private Button btnUploadPhotoEditProfile, btnFinishEditProfile;
    private ImageView imageViewEditProfileProfilePic;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private Uri downlaodUri;
    private String userId;
    private Profile oldProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mProgressDialog = new ProgressDialog(this);

        editTextEditProfileName = findViewById(R.id.editTextEditProfileName);
        editTextEditProfileDescription = findViewById(R.id.editTextEditProfileDescription);
        editTextEditProfileKeyWords = findViewById(R.id.editTextEditProfileKeyWords);
        textViewEditProfileDateOfBirth = findViewById(R.id.textViewEditProfileDateOfBirth);
        btnUploadPhotoEditProfile = findViewById(R.id.btnUploadPhotoEditProfile);
        btnFinishEditProfile = findViewById(R.id.btnFinishEditProfile);
        imageViewEditProfileProfilePic = findViewById(R.id.imageViewEditProfileProfilePic);

        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        textViewEditProfileDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this,
                        AlertDialog.THEME_HOLO_DARK,
                        onDateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = "You were borned on: " + dayOfMonth + "/" + month + "/" + year;
                textViewEditProfileDateOfBirth.setText(date);
            }
        };

        btnUploadPhotoEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btnFinishEditProfile.setOnClickListener(this);

        Intent newIntent = getIntent();
        userId = newIntent.getStringExtra(DashboardActivity.USER_ID);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("profiles").child(userId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //aici trebuie sa iau profilul din baza de date si sa setez toate edit-texturile si data.

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oldProfile = dataSnapshot.getValue(Profile.class);

                editTextEditProfileName.setText(oldProfile.getName());
                editTextEditProfileDescription.setText(oldProfile.getProfileDescription());
                textViewEditProfileDateOfBirth.setText(oldProfile.getDateOfBirth());
                editTextEditProfileKeyWords.setText(oldProfile.getKeyWords());
                Uri myUri = Uri.parse(oldProfile.getProfilePictureUrl());
                downlaodUri = myUri;
                Picasso.get().load(myUri).fit().centerCrop().into(imageViewEditProfileProfilePic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            final StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downlaodUri = uri;
                            Picasso.get().load(downlaodUri).fit().centerCrop().into(imageViewEditProfileProfilePic);
                            Toast.makeText(EditProfileActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFinishEditProfile) {
            String dateOfBirth;
            if (mDay != 0 || mMonth != 0 || mYear != 0) {
                dateOfBirth = mDay + "/" + mMonth + "/" + mYear;
            } else {
                dateOfBirth = oldProfile.getDateOfBirth();
            }
            Profile profile = new Profile(firebaseAuth.getCurrentUser().getUid(), editTextEditProfileName.getText().toString(),
                    editTextEditProfileKeyWords.getText().toString(), dateOfBirth,
                    editTextEditProfileDescription.getText().toString(), downlaodUri.toString());

            profile.setPendingEvents(oldProfile.getPendingEvents());
            profile.setListOfGroups(oldProfile.getListOfGroups());
            profile.setListOfEvents(oldProfile.getListOfEvents());
            profile.setPendingEvents(oldProfile.getPendingEvents());

            firebaseDatabase.setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(EditProfileActivity.this, DashboardActivity.class));
                    Toast.makeText(EditProfileActivity.this, "Profile saved!", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Couldn't add profile to database!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}