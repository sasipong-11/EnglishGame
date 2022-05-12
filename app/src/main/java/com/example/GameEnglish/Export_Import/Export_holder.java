package com.example.GameEnglish.Export_Import;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.GameEnglish.R;

public class Export_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView Groupname;
    CheckBox checkBox;
    RelativeLayout relativeLayout;
    ItemClickListener item_clickListener;

    public Export_holder(@NonNull View itemView) {
        super(itemView);

        Groupname = itemView.findViewById(R.id.GroupName);
        checkBox = itemView.findViewById(R.id.Check_Group);
        relativeLayout = itemView.findViewById(R.id.Check_Group_layout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.performClick(); //สั้งให้ checkbox click
            }
        });

        checkBox.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener export_click){
        this.item_clickListener = export_click;
    }

    @Override
    public void onClick(View v) {
        this.item_clickListener.onItemClick(v,getLayoutPosition());
    }
}
