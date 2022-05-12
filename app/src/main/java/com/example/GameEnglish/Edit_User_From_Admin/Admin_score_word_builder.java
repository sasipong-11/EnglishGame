package com.example.GameEnglish.Edit_User_From_Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.My_Score.Ranking.Vertical_Ranking_Adapter;
import com.example.GameEnglish.My_Score.Ranking.Vertical_Ranking_Model;
import com.example.GameEnglish.My_Score.Score.HorizontalModel;
import com.example.GameEnglish.My_Score.Score.VerticalModel;
import com.example.GameEnglish.My_Score.Score.VerticalRecyclerViewAdapter;
import com.example.GameEnglish.R;
import com.example.GameEnglish.recyclerView_Ranking.Ranking_Item;

import java.util.ArrayList;
import java.util.Objects;

public class Admin_score_word_builder extends AppCompatActivity {

    //score
    RecyclerView ScoreRecyclerView;
    VerticalRecyclerViewAdapter verticalRecyclerViewAdapter;
    ArrayList<VerticalModel> verticalModels;

    //rank
    RecyclerView RankRecyclerView;
    Vertical_Ranking_Adapter vertical_ranking_adapter;
    ArrayList<Vertical_Ranking_Model> vertical_ranking_models;

    String Get_UserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscore_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("คะแนนของผู้ใช้งาน");
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

        Get_UserID = getIntent().getExtras().getString("UserID");

        //Score
        verticalModels = new ArrayList<>();
        ScoreRecyclerView = findViewById(R.id.recyclerview);
        ScoreRecyclerView.setHasFixedSize(true);
        ScoreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        verticalRecyclerViewAdapter = new VerticalRecyclerViewAdapter(this,verticalModels);
        ScoreRecyclerView.setAdapter(verticalRecyclerViewAdapter);
        setData_Score();

        // Rank
        vertical_ranking_models = new ArrayList<>();
        RankRecyclerView = findViewById(R.id.recyclerview_rank);
        RankRecyclerView.setHasFixedSize(true);
        RankRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        vertical_ranking_adapter = new Vertical_Ranking_Adapter(this,vertical_ranking_models);
        RankRecyclerView.setAdapter(vertical_ranking_adapter);
        setData_Rank();

    }

    public void setData_Score(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList Groupname = databaseHelper.SearchGroupName_easy(Get_UserID);

        for (int i=0;i<Groupname.size();i++){
            VerticalModel verticalModel = new VerticalModel();
            verticalModel.setTitle(String.valueOf(Groupname.get(i)));
            verticalModel.setAvgScore(String.valueOf(i));

            ArrayList<HorizontalModel> horizontalModels = databaseHelper.item_word_Ranking_easy
                    (Get_UserID, String.valueOf(Groupname.get(i)));
            verticalModel.setArrayList(horizontalModels);
            verticalModels.add(verticalModel);
        }
        verticalRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void setData_Rank(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList Groupname = databaseHelper.SearchGroupName_easy(Get_UserID);

        for (int i=0;i<Groupname.size();i++){
            Vertical_Ranking_Model verticalModel = new Vertical_Ranking_Model();
            ArrayList<Ranking_Item> horizontalModels = databaseHelper.rank_ex3_easy(String.valueOf(Groupname.get(i)));
            verticalModel.setArrayList(horizontalModels);
            vertical_ranking_models.add(verticalModel);
        }
        vertical_ranking_adapter.notifyDataSetChanged();
    }
}
