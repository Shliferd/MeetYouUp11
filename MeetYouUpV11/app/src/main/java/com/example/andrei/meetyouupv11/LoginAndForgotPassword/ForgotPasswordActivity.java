package com.example.andrei.meetyouupv11.LoginAndForgotPassword;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.meetyouupv11.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextForgotPasswordMessage;
    Button btnChangePassword;
    TextView btnForgotPasswordBack;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextForgotPasswordMessage = findViewById(R.id.editTextForgotPasswordMessage);
        btnChangePassword = findViewById(R.id.btnChagePassword);
        btnForgotPasswordBack = findViewById(R.id.btnForgotPasswordBack);

        firebaseAuth = FirebaseAuth.getInstance();

        btnChangePassword.setOnClickListener(this);
        btnForgotPasswordBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnForgotPasswordBack) {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        } else if (v.getId() == R.id.btnChagePassword) {
            restPassword(editTextForgotPasswordMessage.getText().toString());
        }
    }

    private void restPassword(final String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "We've sent an email to reset the password to: " + email, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Could not sent email to:  " + email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
