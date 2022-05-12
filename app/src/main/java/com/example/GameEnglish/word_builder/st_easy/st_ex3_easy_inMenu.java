package com.example.GameEnglish.word_builder.st_easy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.word_builder.MyRecyclerViewAdapter;
import com.example.GameEnglish.R;

import java.util.ArrayList;

public class st_ex3_easy_inMenu extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    Bundle group;
    String GroupName;
    Button delete,update;

    public static Activity close_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_minigame_inmenu);

        close_activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("รายการคำศัพท์");
        Title.setTextSize(20);

        ImageView back = toolbar.findViewById(R.id.back);
        ImageView show_menu = toolbar.findViewById(R.id.show_menu);
        show_menu.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<String> st = new ArrayList<>();
        group = getIntent().getExtras();

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        // data to populate the RecyclerView with
        GroupName = String.valueOf(group.getString("Groupname")); //รับค่า GroupName มาจากหน้า Gridviewadpater
        st = databaseHelper.group_st_easy(GroupName);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, st);
        adapter.setClickListener(this);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getApplication(), // เส้นแบ่ง ------
//                DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);

        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), st_ex3_easy_update.class);
                intent.putExtra("Groupname",GroupName);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.delete_st_ex3(GroupName);

                st_ex3_easy_menu.close_activity.finish();

                Intent intent = new Intent(getApplicationContext(),st_ex3_easy_menu.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onItemClick(View view, int position) {

        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
