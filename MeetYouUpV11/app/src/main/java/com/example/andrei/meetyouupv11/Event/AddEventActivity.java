package com.example.andrei.meetyouupv11.Event;

import com.example.andrei.meetyouupv11.Dashboard.EditProfileActivity;
import com.example.andrei.meetyouupv11.R;
import com.example.andrei.meetyouupv11.model.AcceptEvent;
import com.example.andrei.meetyouupv11.model.BasicEvent;
import com.example.andrei.meetyouupv11.model.Event;
import com.example.andrei.meetyouupv11.model.EventDecorator;
import com.example.andrei.meetyouupv11.model.LimitedNumberEvent;
import com.example.andrei.meetyouupv11.model.ShareableEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextAddEventPart1Name, editTextAddEventPart1Desc, editTextAddEventPart1KeyWords,
            editTextAddEventPart1NumberLimit;
    private TextView textViewAddEventPart1Date, textViewAddEventRadioPrivate;
    private Button btnAddEventPart1Photo, btnAddEventPart1ToMap;
    private RadioGroup radioGroupAddEvent;
    private RelativeLayout relativeLayoutAddEventCombo;
    private CheckBox checkBoxNumberLimit, checkBoxAdminAccept;
    private ImageView imageViewAddEventPart1Pic;

    private int mYear, mMonth, mDay;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private Uri downlaodUri;
    private String eventId;
    public static final String EVENT_ID = "EVENT_ID";
    private static final String TAG = "AddEventActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        //initialize

        mProgressDialog = new ProgressDialog(this);

        //editText
        editTextAddEventPart1Name = findViewById(R.id.editTextAddEventPart1Name);
        editTextAddEventPart1Desc = findViewById(R.id.editTextAddEventPart1Desc);
        editTextAddEventPart1KeyWords = findViewById(R.id.editTextAddEventPart1KeyWords);
        editTextAddEventPart1NumberLimit = findViewById(R.id.editTextAddEventPart1NumberLimit);
        //textView
        textViewAddEventPart1Date = findViewById(R.id.textViewAddEventPart1Date);
        textViewAddEventRadioPrivate = findViewById(R.id.textViewAddEventRadioPrivate);
        //buttons
        btnAddEventPart1Photo = findViewById(R.id.btnAddEventPart1Photo);
        btnAddEventPart1ToMap = findViewById(R.id.btnAddEventPart1ToMap);
        //radioGroup
        radioGroupAddEvent = findViewById(R.id.radioGroupAddEvent);
        //relativeLayout
        relativeLayoutAddEventCombo = findViewById(R.id.relativeLayoutAddEventCombo);
        //checkBox
        checkBoxNumberLimit = findViewById(R.id.checkBoxNumberLimit);
        checkBoxAdminAccept = findViewById(R.id.checkBoxAdminAccept);
        //imageView
        imageViewAddEventPart1Pic = findViewById(R.id.imageViewAddEventPart1Pic);

        textViewAddEventPart1Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddEventActivity.this,
                        AlertDialog.THEME_HOLO_DARK,
                        onDateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = "Event scheduled on: " + dayOfMonth + "/" + month + "/" + year;
                textViewAddEventPart1Date.setText(date);
            }
        };

        btnAddEventPart1Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btnAddEventPart1ToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventAndGoToMap();
            }
        });

        //firebase
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("events");
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddEventActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is ok
            Log.d(TAG, "isServicesOK: Google play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can fix it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddEventActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void saveEventAndGoToMap() {
        if (isServicesOK()) {
            String userCreator = firebaseAuth.getCurrentUser().getUid();
            String eventName = editTextAddEventPart1Name.getText().toString();
            String eventDesc = editTextAddEventPart1Desc.getText().toString();
            String eventKeyWords = editTextAddEventPart1KeyWords.getText().toString();
            String eventDate = mDay + "/" + mMonth + "/" + mYear;
            String eventPicture = downlaodUri.toString();
            BasicEvent basicEvent = new BasicEvent(userCreator, eventName, eventKeyWords, eventDate, eventDesc, eventPicture,
                    "None", 1.0, 1.0);
            Event event;

            if (radioGroupAddEvent.getCheckedRadioButtonId() == R.id.radioButtonPrivate) {


                eventId = firebaseDatabase.push().getKey();

                basicEvent.setEventId(eventId);
                event = new EventDecorator(basicEvent);
                event.setIsByAdminAccept();
                event.setIsLimited(0);
                event.setIsShareable();

                firebaseDatabase.child(eventId).setValue(event);
                Intent newIntent = new Intent(AddEventActivity.this, AddEventActivityPart2.class);
                newIntent.putExtra(EVENT_ID, eventId);
                startActivity(newIntent);

            } else if (radioGroupAddEvent.getCheckedRadioButtonId() == R.id.radioButtonPublic) {
                if (checkBoxNumberLimit.isChecked() && checkBoxAdminAccept.isChecked()) {
                    int nrParticipants = Integer.parseInt(editTextAddEventPart1NumberLimit.getText().toString());

                    eventId = firebaseDatabase.push().getKey();
                    basicEvent.setEventId(eventId);
                    event = new ShareableEvent(basicEvent);
                    event.setIsShareable();
                    event = new LimitedNumberEvent(basicEvent);
                    event.setIsLimited(nrParticipants);
                    event = new AcceptEvent(basicEvent);
                    event.setIsByAdminAccept();

                    firebaseDatabase.child(eventId).setValue(event);
                    Intent newIntent = new Intent(AddEventActivity.this, AddEventActivityPart2.class);
                    newIntent.putExtra(EVENT_ID, eventId);
                    startActivity(newIntent);

                } else if (checkBoxNumberLimit.isChecked() && !checkBoxAdminAccept.isChecked()) {
                    int nrPart = Integer.parseInt(editTextAddEventPart1NumberLimit.getText().toString());


                    eventId = firebaseDatabase.push().getKey();
                    basicEvent.setEventId(eventId);

                    event = new ShareableEvent(basicEvent);
                    event.setIsShareable();
                    event = new LimitedNumberEvent(basicEvent);
                    event.setIsLimited(nrPart);
                    event.setIsByAdminAccept();

                    firebaseDatabase.child(eventId).setValue(event);
                    Intent newIntent = new Intent(AddEventActivity.this, AddEventActivityPart2.class);
                    newIntent.putExtra(EVENT_ID, eventId);
                    startActivity(newIntent);

                } else if (!checkBoxNumberLimit.isChecked() && checkBoxAdminAccept.isChecked()) {

                    eventId = firebaseDatabase.push().getKey();
                    basicEvent.setEventId(eventId);
                    event = new ShareableEvent(basicEvent);
                    event.setIsShareable();
                    event = new AcceptEvent(basicEvent);
                    event.setIsByAdminAccept();
                    event.setIsLimited(0);
                    firebaseDatabase.child(eventId).setValue(event);
                    Intent newIntent = new Intent(AddEventActivity.this, AddEventActivityPart2.class);
                    newIntent.putExtra(EVENT_ID, eventId);
                    startActivity(newIntent);

                } else if (!checkBoxAdminAccept.isChecked() && !checkBoxNumberLimit.isChecked()) {

                    eventId = firebaseDatabase.push().getKey();
                    basicEvent.setEventId(eventId);
                    event = new ShareableEvent(basicEvent);
                    event.setIsShareable();
                    event.setIsByAdminAccept();
                    event.setIsLimited(0);
                    firebaseDatabase.child(eventId).setValue(event);
                    Intent newIntent = new Intent(AddEventActivity.this, AddEventActivityPart2.class);
                    newIntent.putExtra(EVENT_ID, eventId);
                    startActivity(newIntent);
                }
            }
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
                            Picasso.get().load(downlaodUri).fit().centerCrop().into(imageViewAddEventPart1Pic);
                            Toast.makeText(AddEventActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void checkPrivate(View view) {
        textViewAddEventRadioPrivate.setVisibility(View.VISIBLE);
        relativeLayoutAddEventCombo.setVisibility(View.GONE);
    }

    public void checkPublic(View view) {
        textViewAddEventRadioPrivate.setVisibility(View.GONE);
        relativeLayoutAddEventCombo.setVisibility(View.VISIBLE);
    }

    public void showEditTextNumberOfParticipants(View view) {
        if (checkBoxNumberLimit.isChecked()) {
            editTextAddEventPart1NumberLimit.setVisibility(View.VISIBLE);
        } else if (!checkBoxNumberLimit.isChecked()) {
            editTextAddEventPart1NumberLimit.setVisibility(View.GONE);
        }
    }
}
