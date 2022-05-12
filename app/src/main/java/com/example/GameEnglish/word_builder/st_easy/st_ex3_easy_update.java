package com.example.GameEnglish.word_builder.st_easy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.word_builder.GridListAdapter_selectWord;
import com.example.GameEnglish.R;

import com.example.GameEnglish.word_builder.word;

import java.util.ArrayList;

public class st_ex3_easy_update extends AppCompatActivity {

    private GridListAdapter_selectWord adapter;
    private ArrayList<String> arrayList;
    private DatabaseHelper databaseHelper;
    ArrayList<word> query_word;
    ArrayList<String> old_Group_Check = new ArrayList<>();
    Dialog dialog;
    ArrayList<String> old_IdWord = new ArrayList<>();
    String old_GroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view__st_word_builder_selectword);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("แก้ไขแบบฝึกซ้อมจัดอันดับ");
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

        databaseHelper = new DatabaseHelper(this);

        Bundle group = getIntent().getExtras();
        old_GroupName = String.valueOf(group.getString("Groupname")); //รับค่า position มาจากหน้า Gridviewadpater
        old_Group_Check = databaseHelper.group_st_easy(old_GroupName);

        loadGridView();
        dialog = new Dialog(this);

        Button button = findViewById(R.id.set_Groupanme);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });
    }

    private void loadGridView() {
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        arrayList = new ArrayList<>();
        ArrayList<Integer> word_index = new ArrayList<>();
        query_word = databaseHelper.query_id_word("Word");
        String word = null;

            for (int j=0;j<query_word.size();j++) { // นำคำศัพท์ทั้งเพิ่มในอาเรย์ เพื่อที่จะแสดง gridview
                word = query_word.get(j).getWord();
            for (int i=0;i<old_Group_Check.size();i++){ //หา position คำศัพท์ใน gridview เพื่อ set check ให้เป็น true
                if (word.equals(old_Group_Check.get(i))){
                    word_index.add(j);
                    old_IdWord.add(query_word.get(j).getId()); //เก็บ wordID ของคำศัพท์ที่ check เป็น true
                }
            }
            arrayList.add(word);
        }

        adapter = new GridListAdapter_selectWord(this, arrayList, false,14);

        for (int k=0;k<word_index.size();k++){ //วนรูป set check true ใน gridview
            adapter.checkCheckBox(word_index.get(k),true);
        }

        gridView.setAdapter(adapter);
    }

    private void onClickEvent() {

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
        dialog.setContentView(R.layout.st_confirm_popup);
        dialog.setCanceledOnTouchOutside(false);
        final EditText Groupname = dialog.findViewById(R.id.Groupname);
        Button button = dialog.findViewById(R.id.CF);
        ImageView close = dialog.findViewById(R.id.this_back);

        final SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter

        if(selectedRows.size() != 5 || selectedRows.size() > 5 ) {
            Groupname.setHint("กรุณาเลือก 5 คำ");
            Groupname.setHintTextColor(Color.parseColor("#EF5350"));
            Groupname.setText("");
            Toast.makeText(st_ex3_easy_update.this, "กรุณาเลือก 5 คำ", Toast.LENGTH_SHORT).show();
        } else {

            dialog.show();
            Groupname.setText(old_GroupName);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Check if item is selected or not via size
                    ArrayList<String> wordid_group = new ArrayList<>();
                    if (selectedRows.size() > 0) {
                        //Loop to all the selected rows array
                        for (int i = 0; i < selectedRows.size(); i++) {

                            //Check if selected rows have value i.e. checked item
                            if (selectedRows.valueAt(i)) {
                                //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                                wordid_group.add(arrayList.get(selectedRows.keyAt(i)));
                            }
                        }

                        if (Groupname.getText().toString().length() < 3){
                            Groupname.setHint("ชื่อสั้นเกินไป");
                            Groupname.setHintTextColor(Color.parseColor("#EF5350"));
                            Groupname.setText("");
                            //Toast.makeText(grid_view_ex3_st_easy_selectWord.this, "ชื่อสั้นเกินไป", Toast.LENGTH_SHORT).show();
                        } else if (Groupname.getText().toString().length() > 8){
                            Groupname.setHint("ชื่อยาวเกินไป");
                            Groupname.setHintTextColor(Color.parseColor("#EF5350"));
                            Groupname.setText("");
                            //Toast.makeText(grid_view_ex3_st_easy_selectWord.this, "ชื่อยาวเกินไป", Toast.LENGTH_SHORT).show();
                        } else {
                            // เช็คว่าชื่อ Groupname ซ่ำกันหรือไม่
                            String GroupName =  String.valueOf(Groupname.getText()); //ตั้งชื่อให้กับ Group
                            String Check_Group = databaseHelper.check_groupname_import(GroupName,"Setting_ex3_easy");
                            if (Check_Group == null || GroupName.equals(old_GroupName)) {
                                databaseHelper.delete_st_ex3(old_GroupName);
                                for (int i = 0; i < wordid_group.size(); i++) {
                                    String wordID = CheckWordID(wordid_group.get(i), query_word); //หา word ว่าเท่ากับ wordid ที่เท่าไหร
                                    //databaseHelper.update_ex3_easy_st(wordID, GroupName, old_GroupName, old_IdWord.get(i));
                                    databaseHelper.insert_group(wordID,GroupName,"Setting_ex3_easy","wordID");
                                }
                                databaseHelper.insert_score_easy(GroupName);
                                st_ex3_easy_inMenu.close_activity.finish();
                                st_ex3_easy_menu.close_activity.finish();
                                dialog.dismiss();
                                Intent intent = new Intent(st_ex3_easy_update.this, st_ex3_easy_menu.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(st_ex3_easy_update.this,"ชื่อแบบฝึกจัดอันดับซ่ำกัน กรุณาเปลี่ยนชื่อแบบฝึกจัดอันดับ",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
        }
    }

    public String CheckWordID(String word,ArrayList<word> word_id){ // หา word ว่าเท่ากับ wordid ที่เท่าไหร
        String id = null;
        for(int i=0;i<word_id.size();i++){
            if(word.equals(word_id.get(i).getWord())) {
                id = word_id.get(i).getId();
            }
        }
        return id;
    }
}
