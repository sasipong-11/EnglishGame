package com.example.GameEnglish.My_Score.Score;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.GameEnglish.R;

import java.util.ArrayList;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.VerticalViewHolder> {

    Context context;
    ArrayList<VerticalModel> arrayList;

    public VerticalRecyclerViewAdapter(Context context, ArrayList<VerticalModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myscore_item_vertical,viewGroup,false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        VerticalModel verticalModel = arrayList.get(position);
        String title = verticalModel.getTitle();
        String avg = verticalModel.getAvgScore();
        ArrayList<HorizontalModel> singleItem = verticalModel.getArrayList();

        holder.title.setText(title);
        HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(context,singleItem);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        holder.recyclerView.setAdapter(horizontalRecyclerViewAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class VerticalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView title;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerview1);
            title = itemView.findViewById(R.id.title);
        }
    }

}
