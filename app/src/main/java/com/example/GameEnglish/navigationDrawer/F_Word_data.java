package com.example.GameEnglish.navigationDrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.R;
import com.example.GameEnglish.word_builder.GridviewAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class F_Word_data extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    GridView gridView;
    GridviewAdapter gridviewAdapter;

    public static Activity f_word_data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_word_data);

        f_word_data = this;

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("Vocabulary information");
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

        databaseHelper = new DatabaseHelper(getApplicationContext());
        gridView = findViewById(R.id.GridView_Sentence);

        ArrayList<String> Word = databaseHelper.queryword("Word");
        gridviewAdapter = new GridviewAdapter(Word,F_Word_data.this,"Word_data",R.drawable.radius_button_color_ex2,null);
        gridView.setAdapter(gridviewAdapter);

        }
    }
