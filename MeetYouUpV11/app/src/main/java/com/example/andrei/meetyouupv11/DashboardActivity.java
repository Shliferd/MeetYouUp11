package com.example.andrei.meetyouupv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogout;
    TextView textViewDashboard;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLogout = findViewById(R.id.btnLogout);
        textViewDashboard = findViewById(R.id.textViewDashboard);

        btnLogout.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //Check already session
        if (firebaseAuth.getCurrentUser() != null) {
            textViewDashboard.setText("Welcome , " + firebaseAuth.getCurrentUser().getEmail());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogout) {
            logoutUser();
        }
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }
}
