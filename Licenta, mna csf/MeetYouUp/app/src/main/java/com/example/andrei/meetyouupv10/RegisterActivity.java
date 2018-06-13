package com.example.andrei.meetyouupv10;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnRegister;
    private TextView textEditLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnRegister);
        textEditLogin = findViewById(R.id.textViewLogin);

        btnRegister.setOnClickListener(this);
        textEditLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == textEditLogin) {
            Intent intent = new Intent(this, LoginAcivity.class);
            startActivity(intent);
            finish();
        } else if (v == btnRegister) {
            registerUser();
        }
    }

    private void registerUser() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //start profile activity
                    Toast.makeText(RegisterActivity.this, "Succesfully registered!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Could not register!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
