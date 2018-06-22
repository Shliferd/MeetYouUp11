package com.example.andrei.meetyouupv11.Register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.LoginAndForgotPassword.LoginActivity;
import com.example.andrei.meetyouupv11.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextRegisterEmail, editTextRegisterPassword;
    Button btnContinueToRegistation;
    TextView btnAlreadyRegistered;

    private FirebaseAuth firebaseAuth;
    //private DatabaseReference databaseReferenceUsers;
    public static final String USER_ID = "USER_ID";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "USER_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //View
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        btnContinueToRegistation = findViewById(R.id.btnContinueToRegistation);
        btnAlreadyRegistered = findViewById(R.id.btnAlreadyRegistered);

        //Init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");

        btnContinueToRegistation.setOnClickListener(this);
        btnAlreadyRegistered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAlreadyRegistered) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            //finish();
        } else if (v.getId() == R.id.btnContinueToRegistation) {
            signUpUser(editTextRegisterEmail.getText().toString(), editTextRegisterPassword.getText().toString());
        }
    }

    private void signUpUser(final String email, final String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email !", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password !", Toast.LENGTH_SHORT).show();
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Please enter a password that has at least 6 characters!", Toast.LENGTH_SHORT).show();
        }
        //startActivity(new Intent(RegisterActivity.this, SetUpProfileActivity.class));

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registered! Please set up your profile!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Could not register the user !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent newIntent = new Intent(RegisterActivity.this, SetUpProfileActivity.class);
                    newIntent.putExtra(USER_ID, firebaseAuth.getCurrentUser().getUid());
                    newIntent.putExtra(USER_EMAIL, editTextRegisterEmail.getText().toString());
                    newIntent.putExtra(USER_PASSWORD, editTextRegisterPassword.getText().toString());
                    startActivity(newIntent);

                } else {
                    Toast.makeText(RegisterActivity.this, "Could not login !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
