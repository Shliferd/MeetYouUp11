package com.example.andrei.meetyouupv11;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextLoginEmail, editTextLoginPassword;
    Button btnLogin;
    TextView btnForgotPassword, btnRegister;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        //Init firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Check already session
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnForgotPassword) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            //finish();
        } else if (v.getId() == R.id.btnRegister) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            //finish();
        } else if (v.getId() == R.id.btnLogin) {
            loginUser(editTextLoginEmail.getText().toString(), editTextLoginPassword.getText().toString());
        }
    }

    private void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email !", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password !", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Could not login !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
