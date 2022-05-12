package com.example.GameEnglish.My_Score.Ranking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.GameEnglish.R;
import com.example.GameEnglish.recyclerView_Ranking.Ranking_Item;

import java.util.ArrayList;

public class Vertical_Ranking_Adapter extends RecyclerView.Adapter<Vertical_Ranking_Adapter.VerticalViewHolder> {

    Context context;
    ArrayList<Vertical_Ranking_Model> arrayList;

    public Vertical_Ranking_Adapter(Context context, ArrayList<Vertical_Ranking_Model> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myscore_ranking,viewGroup,false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        Vertical_Ranking_Model verticalModel = arrayList.get(position);
        ArrayList<Ranking_Item> singleItem = verticalModel.getArrayList();


        My_Rank_RecyclerViewAdapter my_rank_recyclerViewAdapter = new My_Rank_RecyclerViewAdapter(context,singleItem);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        holder.recyclerView.setAdapter(my_rank_recyclerViewAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class VerticalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerview_vertical_rank);
        }
    }

}