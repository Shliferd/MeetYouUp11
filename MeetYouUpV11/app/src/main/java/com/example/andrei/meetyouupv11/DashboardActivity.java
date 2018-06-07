package com.example.andrei.meetyouupv11;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogout;
    TextView textViewDashboard;
    RecyclerView recyclerViewDashboard;
    DashboardAdapter dashboardAdapter;

    String[] dashboardRowNames = null;

    private FirebaseAuth firebaseAuth;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        resources = getResources();

        btnLogout = findViewById(R.id.btnLogout);
        textViewDashboard = findViewById(R.id.textViewDashboard);

        recyclerViewDashboard = findViewById(R.id.recyclerViewDashboard);
        recyclerViewDashboard.setHasFixedSize(true);
        recyclerViewDashboard.setLayoutManager(new LinearLayoutManager(this));

        dashboardRowNames = resources.getStringArray(R.array.dashboardRows);

        dashboardAdapter = new DashboardAdapter(this, dashboardRowNames);
        recyclerViewDashboard.setAdapter(dashboardAdapter);


        btnLogout.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();

        //Check already session
        if (firebaseAuth.getCurrentUser() != null) {
            textViewDashboard.setText("Welcome , " + firebaseAuth.getCurrentUser().getEmail());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(DashboardActivity.this, EditProfileActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
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
