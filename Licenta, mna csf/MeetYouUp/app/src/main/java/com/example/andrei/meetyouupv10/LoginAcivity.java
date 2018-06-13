package com.example.andrei.meetyouupv10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginAcivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTexttUsername;
    private EditText editTextPassowrd;
    private Button btnLogin;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);

        editTexttUsername = findViewById(R.id.editTextUsername);
        editTextPassowrd = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        btnLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            //login
        } else if (v == textViewRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
