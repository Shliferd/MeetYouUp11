package com.example.andrei.meetyouupv11;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.andrei.meetyouupv11.model.Profile;
import com.example.andrei.meetyouupv11.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SetUpProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewSetUpDateOfBirth, textViewSetUpWelcome;
    private Button btnUploadPhoto, btnFinishSetUp;
    private int mYear, mMonth, mDay;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private ImageView imageViewSetUpProfilePic;
    private Uri downlaodUri;
    private FirebaseAuth firebaseAuth;
    private EditText editTextSetUpSurname, editTextSetUpForename, editTextSetUpDescription, editTextSetUpKeyWords;
    private DatabaseReference databaseReferenceProfiles;
    private String mEmail, mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        mProgressDialog = new ProgressDialog(this);


        imageViewSetUpProfilePic = findViewById(R.id.imageViewSetUpProfilePic);
        textViewSetUpWelcome = findViewById(R.id.textViewSetUpWelcome);
        editTextSetUpSurname = findViewById(R.id.editTextSetUpSurname);
        editTextSetUpForename = findViewById(R.id.editTextSetUpForename);
        editTextSetUpDescription = findViewById(R.id.editTextSetUpDescription);
        editTextSetUpKeyWords = findViewById(R.id.editTextSetUpKeyWords);
        btnFinishSetUp = findViewById(R.id.btnFinishSetUp);

        mStorage = FirebaseStorage.getInstance().getReference();

        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);

        textViewSetUpDateOfBirth = findViewById(R.id.textViewSetUpDateOfBirth);
        textViewSetUpDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SetUpProfileActivity.this,
                        AlertDialog.THEME_HOLO_DARK,
                        onDateSetListener, mYear, mMonth, mDay);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = "You was borned on: " + dayOfMonth + "/" + month + "/" + year;
                textViewSetUpDateOfBirth.setText(date);
            }
        };

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btnFinishSetUp.setOnClickListener(this);

        Intent newIntent = getIntent();
        String userId = newIntent.getStringExtra(RegisterActivity.USER_ID);
        mEmail = newIntent.getStringExtra(RegisterActivity.USER_EMAIL);
        mPassword = newIntent.getStringExtra(RegisterActivity.USER_PASSWORD);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceProfiles = FirebaseDatabase.getInstance().getReference().child("profiles");

        //Check already session
        if (firebaseAuth.getCurrentUser() != null) {
            textViewSetUpWelcome.setText("Welcome , " + firebaseAuth.getCurrentUser().getEmail() + ", now you have to setup you profile:");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    downlaodUri = taskSnapshot.getDownloadUrl();
                    Picasso.get().load(downlaodUri).fit().centerCrop().into(imageViewSetUpProfilePic);
                    Toast.makeText(SetUpProfileActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFinishSetUp) {

            if (firebaseAuth.getCurrentUser() != null) {

                Intent newIntent = getIntent();
                String userId = newIntent.getStringExtra(RegisterActivity.USER_ID);

                //String profileId = databaseReferenceProfiles.push().getKey();
                String dateOfBirth = mDay + "/" + mMonth + "/" + mYear;
                Profile newProfile = new Profile(userId, editTextSetUpSurname.getText().toString(), editTextSetUpForename.getText().toString(),
                        editTextSetUpKeyWords.getText().toString(), dateOfBirth, editTextSetUpDescription.getText().toString(), downlaodUri);

//                Intent newIntent = getIntent();
//                String userId = newIntent.getStringExtra(RegisterActivity.USER_ID);
                databaseReferenceProfiles.child(userId).setValue(newProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(new Intent(SetUpProfileActivity.this, ForgotPasswordActivity.class));
                        //finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetUpProfileActivity.this, "Couldn't add profile to database!", Toast.LENGTH_SHORT).show();
                    }
                });

//                startActivity(new Intent(SetUpProfileActivity.this, DashboardActivity.class));
//                finish();
            }
        }
    }
}
