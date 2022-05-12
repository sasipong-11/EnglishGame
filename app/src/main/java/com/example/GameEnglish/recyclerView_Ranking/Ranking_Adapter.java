package com.example.GameEnglish.recyclerView_Ranking;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.GameEnglish.R;

import java.util.ArrayList;

public class Ranking_Adapter extends RecyclerView.Adapter<Ranking_Adapter.Ranking_Holder> {

    private ArrayList<Ranking_Item> ranking_items;

    public static class Ranking_Holder extends RecyclerView.ViewHolder{

        public TextView rank,name,score;

        public Ranking_Holder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.sumScore);
        }
    }

    public Ranking_Adapter(ArrayList<Ranking_Item> ranking_items) {
        this.ranking_items = ranking_items;
    }

    @NonNull
    @Override
    public Ranking_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myscore_ranking_item,viewGroup, false);
        Ranking_Holder view = new Ranking_Holder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull Ranking_Holder ranking_holder, int i) {
        Ranking_Item currentItem = ranking_items.get(i);

        ranking_holder.rank.setText(currentItem.getRank());
        ranking_holder.name.setText(currentItem.getName());
        ranking_holder.score.setText(currentItem.getScore());
    }

    @Override
    public int getItemCount() {
        return ranking_items.size();
    }

}
