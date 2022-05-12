package com.example.GameEnglish.Edit_User_From_Admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.R;

import java.util.ArrayList;
import java.util.Objects;

public class Call_GridAdapter_User extends AppCompatActivity {

    GridView gridView;
    public static Activity close_activity;//ปิดหน้าเมนูในหน้าแก้ไขโปรไฟล์

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_gridview_users);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("User Information");
        Title.setTextSize(20);

        ImageView back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView show_menu = toolbar.findViewById(R.id.show_menu);
        show_menu.setVisibility(View.GONE);

        close_activity = this;

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ArrayList<String> values = databaseHelper.getAll_User("Fullname");
        ArrayList<String> UserID = databaseHelper.getAll_User("UserID");
        Log.d("Userid",UserID.toString());
        ArrayList<String> images = databaseHelper.getAll_User_Picture();

        values.remove(0);
        UserID.remove(0);
        try {
            images.remove(0);
        } catch (Exception e){

        }

        gridView = (GridView) findViewById(R.id.griview);
        GridAdapter_User gridAdapter = new GridAdapter_User(this, values, images,UserID);
        gridView.setAdapter(gridAdapter);
    }
}