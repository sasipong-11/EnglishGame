package com.example.GameEnglish.flash_card.st_flash_card_adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.example.GameEnglish.R;

import java.util.ArrayList;
import java.util.Objects;

public class st_flash_card_select_image_update extends AppCompatActivity {

    Dialog dialog;
    public static Activity close_activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_card_st_main_select_image);

        close_activity = this;  //ปิด activity จากหน้าอื่น

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ImageView show_menu = toolbar.findViewById(R.id.show_menu);
        show_menu.setVisibility(View.GONE);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("เลือกคำถาม");
        Title.setTextSize(20);

        ImageView back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog = new Dialog(this);
        Button button = findViewById(R.id.cf_char);
        ArrayList<Item_st_flash_card> set_image_char = new ArrayList<>();
        ArrayList<String> Check_Select = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> get_char = bundle.getStringArrayList("Character");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        String get_Group = bundle.getString("Groupname");
        ArrayList<String> GroupName = databaseHelper.ex2_char_inGroup(get_Group);

        Check_Select = databaseHelper.st_ex2_get_ImageChar(get_Group);

        for (int i=0;i<get_char.size();i++){
            set_image_char.add(databaseHelper.item_st_ex2(get_char.get(i)));
        }

        RecyclerView mRecyclerView = findViewById(R.id.selct_image_st2);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new adapter_flash_card_st(set_image_char,this,button,Check_Select,get_Group);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
