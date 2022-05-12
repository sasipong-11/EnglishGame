package com.example.GameEnglish.Edit_User_From_Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.GameEnglish.R;

import java.io.File;
import java.util.ArrayList;

public class GridAdapter_User extends BaseAdapter {

    Context context;
    private final ArrayList<String> values;
    private final ArrayList<String> images;
    ArrayList<String> UserID;
    LayoutInflater layoutInflater;

    public GridAdapter_User(Context context, ArrayList<String> values, ArrayList<String> images,ArrayList<String> UserID) {
        this.context = context;
        this.values = values;
        this.images = images;
        this.UserID = UserID;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Holder holder = new Holder();
        final View rowView;

        rowView = layoutInflater.inflate(R.layout.custom_gridview_item_user, null);
        holder.tv =(TextView) rowView.findViewById(R.id.name);
        holder.img = (ImageView) rowView.findViewById(R.id.profile);
        holder.tv.setText(values.get(position));
        try{ //กรณีแอดไม่ได้ใส่รูป defual ไม่มีรูป
            File file = new File(images.get(position));
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            holder.img.setImageBitmap(myBitmap);
        } catch (Exception e){

        }


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Popup menu
                PopupMenu popupMenu = new PopupMenu(context,rowView);
                popupMenu.getMenuInflater().inflate(R.menu.admin_user_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case  R.id.edit_user :
                                Intent intent = new Intent(context,GridAdapter_User_Modifiled.class);
                                intent.putExtra("UserID", UserID.get(position));
                                context.startActivity(intent);
                                return true;
                            case R.id.admin_score_fc:
                                Intent intent_score2 = new Intent(context, Admin_score_flash_card.class);
                                intent_score2.putExtra("UserID", UserID.get(position));
                                context.startActivity(intent_score2);
                                return true;
                            case R.id.admin_score_ex3:
                                Intent intent_score3 = new Intent(context, Admin_score_word_builder.class);
                                intent_score3.putExtra("UserID", UserID.get(position));
                                context.startActivity(intent_score3);
                                return true;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        return rowView;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

}