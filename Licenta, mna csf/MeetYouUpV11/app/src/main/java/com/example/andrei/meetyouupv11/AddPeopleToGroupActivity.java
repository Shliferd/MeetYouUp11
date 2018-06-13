package com.example.andrei.meetyouupv11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

public class AddPeopleToGroupActivity extends AppCompatActivity {

    private EditText searchPeopleToGroup;
    private ImageView imageViewSearchAddPeople;
    private RecyclerView recyclerViewAddPeopleToGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people_to_group);

        searchPeopleToGroup = findViewById(R.id.searchPeopleToGroup);
        imageViewSearchAddPeople = findViewById(R.id.imageViewSearchAddPeople);
        recyclerViewAddPeopleToGroup = findViewById(R.id.recyclerViewAddPeopleToGroup);


    }
}
