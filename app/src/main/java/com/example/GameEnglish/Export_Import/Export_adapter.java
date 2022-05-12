package com.example.GameEnglish.Export_Import;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.GameEnglish.R;

import java.util.ArrayList;

public class Export_adapter extends RecyclerView.Adapter<Export_holder> {

    Context context;
    ArrayList<String> Groupname;
    public ArrayList<String> Check_Group = new ArrayList<>();

    public Export_adapter(Context context,ArrayList<String> Groupname){
        this.context = context;
        this.Groupname = Groupname;
    }

    @NonNull
    @Override
    public Export_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_export,null);
        Export_holder export_holder = new Export_holder(view);
        return export_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Export_holder export_holder, int i) {
        export_holder.Groupname.setText(Groupname.get(i));

        export_holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()){
                    Check_Group.add(Groupname.get(position));
                } else if (!checkBox.isChecked()){
                    Check_Group.remove(Groupname.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Groupname.size();
    }
}
