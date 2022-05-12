package com.example.GameEnglish.flash_card.st_flash_card_adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.R;
import com.example.GameEnglish.word_builder.GridListAdapter_selectWord;

import java.util.ArrayList;
import java.util.Arrays;

public class st_flash_card_selct_char_update extends AppCompatActivity {

    private GridListAdapter_selectWord adapter;
    private ArrayList<String> arrayList;
    private DatabaseHelper databaseHelper;
    Bundle group;
    String GroupName;
    public static Activity close_activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_flash_card_select_char);

        close_activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView show_menu = toolbar.findViewById(R.id.show_menu);
        show_menu.setVisibility(View.GONE);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("แก้ไขแบบฝึกหัด Flash card");
        Title.setTextSize(20);

        ImageView back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        group = getIntent().getExtras();
        GroupName = group.getString("Groupname");

        loadGridView();
        onClickEvent();
    }

    private void loadGridView() {
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        databaseHelper = new DatabaseHelper(this);
        arrayList = new ArrayList<>();

        String[] Char = {"bat","bear","bird","cat","chicken","cow","crab","deer","dog","dolphin","elephant","fish","fox","horse","kangaroo","penguin","pig","rabbit","sheep","wolf"};
        arrayList.addAll(Arrays.asList(Char));
        ArrayList<String> get_Group = databaseHelper.ex2_char_inGroup(GroupName);

        adapter = new GridListAdapter_selectWord(this, arrayList, false,26);
        for(int i=0;i<arrayList.size();i++){
            for (int j=0;j<get_Group.size();j++){
                if (get_Group.get(j).equals(arrayList.get(i))){
                    adapter.checkCheckBox(i,true);
                }
            }
        }

        gridView.setAdapter(adapter);
    }

    private void onClickEvent() {
        findViewById(R.id.go_Select_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                ArrayList<String> Character = new ArrayList<>();
                if (selectedRows.size() > 0) {
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {
                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            Character.add(arrayList.get(selectedRows.keyAt(i)));
                        }
                    }
                    if(selectedRows.size() != 5 || selectedRows.size() > 5){
                        Toast.makeText(getApplicationContext(), "กรุณาเลือก 5 คำ", Toast.LENGTH_SHORT).show();
                    }  else {

                        Intent intent = new Intent(st_flash_card_selct_char_update.this, st_flash_card_select_image_update.class);
                        intent.putExtra("Character",Character);
                        intent.putExtra("Groupname", GroupName);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
