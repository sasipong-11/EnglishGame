package com.example.GameEnglish.flash_card.st_flash_card_adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.R;

import java.util.ArrayList;

public class adapter_flash_card extends RecyclerView.Adapter<adapter_flash_card.ex2_ViewHolder> {
    private ArrayList<Item_st_flash_card> mExampleList;
    private SparseBooleanArray mSelectedItemsIds;
    Context context;
    Button button;

    public static class ex2_ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView1;
        ImageView mImageView1,mImageView2,mImageView3;
        CheckBox checkBox1,checkBox2,checkBox3;

        public ex2_ViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textchar);
            mImageView1 = itemView.findViewById(R.id.image_char1);
            mImageView2 = itemView.findViewById(R.id.image_char2);
            mImageView3 = itemView.findViewById(R.id.image_char3);

            checkBox1 = itemView.findViewById(R.id.checkbox1);
            checkBox2 = itemView.findViewById(R.id.checkbox2);
            checkBox3 = itemView.findViewById(R.id.checkbox3);

        }
    }

    public adapter_flash_card(ArrayList<Item_st_flash_card> exampleList, Context context, Button button) {
        this.mExampleList = exampleList;
        this.context = context;
        this.button = button;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public ex2_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_st_flash_card, parent, false);
        ex2_ViewHolder evh = new ex2_ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ex2_ViewHolder holder, final int position) {

        final Item_st_flash_card currentItem = mExampleList.get(position);

        // แปลงชื่อไฟล์รูปเป็น int
        int char1 = context.getResources().getIdentifier(currentItem.getName_image1() , "drawable", context.getPackageName());
        int char2 = context.getResources().getIdentifier(currentItem.getName_image2() , "drawable", context.getPackageName());
        int char3 = context.getResources().getIdentifier(currentItem.getName_image3() , "drawable", context.getPackageName());


        holder.mTextView1.setText(currentItem.getName_char());

        holder.mImageView1.setImageResource(char1);
        holder.mImageView2.setImageResource(char2);
        holder.mImageView3.setImageResource(char3);


        holder.checkBox1.setChecked(mSelectedItemsIds.get(position));
        holder.checkBox2.setChecked(mSelectedItemsIds.get(position));
        holder.checkBox3.setChecked(mSelectedItemsIds.get(position));


        holder.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox1.isChecked()){
                    currentItem.setCheck(currentItem.getCheck()+1);
                    String image = String.valueOf(currentItem.getName_image1());
                    currentItem.setCheck1(image);
                } else {
                    currentItem.setCheck(currentItem.getCheck()-1);
                    currentItem.setCheck1(null);
                }
            }
        });

        holder.checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox2.isChecked()){
                    currentItem.setCheck(currentItem.getCheck()+1);
                    String image = String.valueOf(currentItem.getName_image2());
                    currentItem.setCheck2(image);
                } else {
                    currentItem.setCheck(currentItem.getCheck()-1);
                    currentItem.setCheck2(null);
                }
            }
        });

        holder.checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox3.isChecked()){
                    currentItem.setCheck(currentItem.getCheck()+1);
                    String image = String.valueOf(currentItem.getName_image3());
                    currentItem.setCheck3(image);
                } else {
                    currentItem.setCheck(currentItem.getCheck()-1);
                    currentItem.setCheck3(null);
                }
            }
        });



        holder.mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox1.setChecked(!holder.checkBox1.isChecked());
                if (holder.checkBox1.isChecked()){
                    currentItem.setCheck(currentItem.getCheck()+1);
                    String image = String.valueOf(currentItem.getName_image1());
                    currentItem.setCheck1(image);
                } else {
                    currentItem.setCheck(currentItem.getCheck()-1);
                    currentItem.setCheck1(null);
                }
            }
        });

        holder.mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox2.setChecked(!holder.checkBox2.isChecked());
                if (holder.checkBox2.isChecked()){
                    currentItem.setCheck(currentItem.getCheck()+1);
                    String image = String.valueOf(currentItem.getName_image2());
                    currentItem.setCheck2(image);
                } else {
                    currentItem.setCheck(currentItem.getCheck()-1);
                    currentItem.setCheck2(null);
                }
            }
        });

        holder.mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox3.setChecked(!holder.checkBox3.isChecked());
                if (holder.checkBox3.isChecked()){
                    currentItem.setCheck(currentItem.getCheck()+1);
                    String image = String.valueOf(currentItem.getName_image3());
                    currentItem.setCheck3(image);
                } else {
                    currentItem.setCheck(currentItem.getCheck()-1);
                    currentItem.setCheck3(null);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExampleList.get(0).getCheck() == 2 && mExampleList.get(1).getCheck() == 2 &&
                mExampleList.get(2).getCheck() == 2 && mExampleList.get(3).getCheck() == 2 &&
                mExampleList.get(4).getCheck() == 2) {

                    //insert db
                    final ArrayList<String> setChar = new ArrayList<>();

                    if (mExampleList.get(0).getCheck1() != null){
                        setChar.add(mExampleList.get(0).getCheck1());
                    }
                    if (mExampleList.get(0).getCheck2() != null){
                        setChar.add(mExampleList.get(0).getCheck2());
                    }
                    if (mExampleList.get(0).getCheck3() != null){
                        setChar.add(mExampleList.get(0).getCheck3());
                    }

                    if (mExampleList.get(1).getCheck1() != null){
                        setChar.add(mExampleList.get(1).getCheck1());
                    }
                    if (mExampleList.get(1).getCheck2() != null){
                        setChar.add(mExampleList.get(1).getCheck2());
                    }
                    if (mExampleList.get(1).getCheck3() != null){
                        setChar.add(mExampleList.get(1).getCheck3());
                    }


                    if (mExampleList.get(2).getCheck1() != null){
                        setChar.add(mExampleList.get(2).getCheck1());
                    }
                    if (mExampleList.get(2).getCheck2() != null){
                        setChar.add(mExampleList.get(2).getCheck2());
                    }
                    if (mExampleList.get(2).getCheck3() != null){
                        setChar.add(mExampleList.get(2).getCheck3());
                    }

                    if (mExampleList.get(3).getCheck1() != null){
                        setChar.add(mExampleList.get(3).getCheck1());
                    }
                    if (mExampleList.get(3).getCheck2() != null){
                        setChar.add(mExampleList.get(3).getCheck2());
                    }
                    if (mExampleList.get(3).getCheck3() != null){
                        setChar.add(mExampleList.get(3).getCheck3());
                    }

                    if (mExampleList.get(4).getCheck1() != null){
                        setChar.add(mExampleList.get(4).getCheck1());
                    }
                    if (mExampleList.get(4).getCheck2() != null){
                        setChar.add(mExampleList.get(4).getCheck2());
                    }
                    if (mExampleList.get(4).getCheck3() != null){
                        setChar.add(mExampleList.get(4).getCheck3());
                    }

                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                    dialog.setContentView(R.layout.st_confirm_popup);
                    dialog.setCanceledOnTouchOutside(false);
                    final EditText Groupname = dialog.findViewById(R.id.Groupname);
                    Button button = dialog.findViewById(R.id.CF);
                    ImageView close = dialog.findViewById(R.id.this_back);
                    dialog.show();

                    final DatabaseHelper databaseHelper = new DatabaseHelper(context);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Check_Group = databaseHelper.check_groupname_import(String.valueOf(Groupname.getText()),"Setting_ex2");
                            if (Groupname.getText().toString().length() < 3){
                                Toast.makeText(context, "ชื่อสั้นเกินไป", Toast.LENGTH_SHORT).show();
                            } else if (Groupname.getText().toString().length() > 8){
                                Toast.makeText(context, "ชื่อยาวเกินไป", Toast.LENGTH_SHORT).show();
                            } else {
                                if (Check_Group == null) {
                                    for (int i = 0; i < setChar.size(); i++) {
                                        databaseHelper.insert_char(setChar.get(i), String.valueOf(Groupname.getText()));
                                    }
                                    databaseHelper.insert_score_ex2(String.valueOf(Groupname.getText()));
                                    dialog.dismiss();

                                    gridview_flash_card_select_char.close_activity.finish();
                                    st_flash_card_menu.close_activity.finish();
                                    flash_card_select_image.close_activity.finish();
                                    Intent intent = new Intent(context, st_flash_card_menu.class);
                                    context.startActivity(intent);

                                    Toast.makeText(context, "เพิ่มข้อมูลแล้ว", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context,"ชื่อแบบทดสอบซ่ำกัน กรุณาเปลี่ยนชื่อแบบทดสอบ",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(context,"กรุณาเลือก 2 คำถามในแต่ละข้อ",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }
    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}