package com.example.GameEnglish.word_builder.st_easy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.word_builder.GridviewAdapter;
import com.example.GameEnglish.R;

import java.util.ArrayList;

public class st_ex3_easy_menu extends AppCompatActivity {

    GridView gridView;
    DatabaseHelper dbHelper;

    public static Activity close_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_minigame_menu);

        close_activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("สร้างแบบฝึกซ้อมจัดอันดับ");
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

        dbHelper = new DatabaseHelper(getApplicationContext());
        gridView = findViewById(R.id.GridViewnomal);
        ArrayList<String> Groupname = dbHelper.GetGroupname("Setting_ex3_easy","st_ex3_easy_id");

        //create Girdview
        GridviewAdapter gridviewAdapter = new GridviewAdapter(Groupname,this,"st_easy",R.drawable.radius_button_color1,null);
        gridView.setAdapter(gridviewAdapter);

        TextView textHide = findViewById(R.id.noEX);

        if (Groupname.size() != 0){
            textHide.setVisibility(View.INVISIBLE);
        }

        FloatingActionButton Add = findViewById(R.id.Add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(st_ex3_easy_menu.this, grid_view_ex3_st_easy_selectWord.class);
                startActivity(intent);
            }
        });

    }
}
