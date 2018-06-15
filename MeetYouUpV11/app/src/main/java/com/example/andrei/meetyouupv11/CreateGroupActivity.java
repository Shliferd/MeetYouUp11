package com.example.andrei.meetyouupv11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.model.Group;
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

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText createGroupName, createGroupDescription;
    private Button btnUploadGroupPhoto, btnContinueGroup;
    private ImageView imageViewCreateGroup;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private static final int GALLERY_INTENT = 2;
    private static String groupUID;
    private ProgressDialog mProgressDialog;
    private Uri downlaodUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        mProgressDialog = new ProgressDialog(this);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");

        //view
        createGroupName = findViewById(R.id.createGroupName);
        createGroupDescription = findViewById(R.id.createGroupDescription);
        btnUploadGroupPhoto = findViewById(R.id.btnUploadGroupPhoto);
        btnContinueGroup = findViewById(R.id.btnContinueGroup);
        imageViewCreateGroup = findViewById(R.id.imageViewCreateGroup);

        btnUploadGroupPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btnContinueGroup.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            final StorageReference filePath = storageReference.child("Photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downlaodUri = uri;
                            Picasso.get().load(downlaodUri).fit().centerCrop().into(imageViewCreateGroup);
                            Toast.makeText(CreateGroupActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnContinueGroup) {
            if (firebaseAuth.getCurrentUser() != null) {
                Group group = new Group(firebaseAuth.getCurrentUser().getUid(), createGroupName.getText().toString(),
                        createGroupDescription.getText().toString(), downlaodUri.toString());

                groupUID = databaseReference.push().getKey();
                databaseReference.child(groupUID).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CreateGroupActivity.this, "All right, one more step!", Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(CreateGroupActivity.this, AddPeopleToGroupActivity.class);
                        newIntent.putExtra("groupId", groupUID);
                        newIntent.putExtra("loggedUserId", firebaseAuth.getCurrentUser().getUid());
                        startActivity(newIntent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateGroupActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
