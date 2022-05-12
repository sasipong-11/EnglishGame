package com.example.GameEnglish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.GameEnglish.word_builder.exercise3_menu;

public class F_menu_home extends Fragment {

    CardView exercise1,exercise2,exercise3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main_menu,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        exercise1 =  view.findViewById(R.id.easy);
        exercise2 =  view.findViewById(R.id.normal);
        exercise3 =  view.findViewById(R.id.hard);

        exercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),exercise3_menu.class);
                v.getContext().startActivity(intent);
            }
        });

        exercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ยังไม่เปิดใช้งาน", Toast.LENGTH_SHORT).show();
            }
        });

        exercise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ยังไม่เปิดใช้งาน", Toast.LENGTH_SHORT).show();
            }
        });
    }
}