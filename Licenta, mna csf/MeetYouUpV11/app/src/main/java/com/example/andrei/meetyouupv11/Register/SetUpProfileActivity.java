package com.example.andrei.meetyouupv11.Register;

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

import com.example.andrei.meetyouupv11.Dashboard.DashboardActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    private EditText editTextSetUpName, editTextSetUpDescription, editTextSetUpKeyWords;
    private DatabaseReference databaseReferenceProfiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        mProgressDialog = new ProgressDialog(this);


        imageViewSetUpProfilePic = findViewById(R.id.imageViewSetUpProfilePic);
        textViewSetUpWelcome = findViewById(R.id.textViewSetUpWelcome);
        editTextSetUpName = findViewById(R.id.editTextSetUpName);
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
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = "You were borned on: " + dayOfMonth + "/" + month + "/" + year;
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
            final StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downlaodUri = uri;
                            Picasso.get().load(downlaodUri).fit().centerCrop().into(imageViewSetUpProfilePic);
                            Toast.makeText(SetUpProfileActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();
                        }
                    });
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

                String dateOfBirth = mDay + "/" + mMonth + "/" + mYear;
                Profile newProfile = new Profile(firebaseAuth.getCurrentUser().getUid(), editTextSetUpName.getText().toString(),
                        editTextSetUpKeyWords.getText().toString(),
                        dateOfBirth, editTextSetUpDescription.getText().toString(),
                        downlaodUri.toString());

                databaseReferenceProfiles.child(userId).setValue(newProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(new Intent(SetUpProfileActivity.this, DashboardActivity.class));
                        Toast.makeText(SetUpProfileActivity.this, "Profile saved!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetUpProfileActivity.this, "Couldn't add profile to database!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
}
