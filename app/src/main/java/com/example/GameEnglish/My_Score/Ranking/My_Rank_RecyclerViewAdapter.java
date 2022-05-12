package com.example.GameEnglish.My_Score.Ranking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.GameEnglish.R;
import com.example.GameEnglish.recyclerView_Ranking.Ranking_Item;

import java.util.ArrayList;

public class My_Rank_RecyclerViewAdapter extends RecyclerView.Adapter<My_Rank_RecyclerViewAdapter.RankViewHolder> {

    Context context;
    ArrayList<Ranking_Item> arrayList;

    public My_Rank_RecyclerViewAdapter (Context context,ArrayList<Ranking_Item> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myscore_ranking_item,viewGroup,false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int positon) {
        final Ranking_Item ranking_item =arrayList.get(positon);
        holder.rank.setText(ranking_item.getRank());
        holder.name.setText(ranking_item.getName());
        holder.score.setText(ranking_item.getScore());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {

        TextView rank,name,score;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.sumScore);
        }
    }
}
