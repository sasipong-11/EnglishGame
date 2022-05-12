package com.example.GameEnglish.word_builder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.R;
import com.example.GameEnglish.TTS;
import com.example.GameEnglish.flash_card.flash_card_game_st;
import com.example.GameEnglish.flash_card.flash_card_game;
import com.example.GameEnglish.flash_card.st_flash_card_adapter.st_flash_card_inMenu;
import com.example.GameEnglish.word_builder.easy.ex3_easy_game;
import com.example.GameEnglish.word_builder.easy.ex3_easy_game_st;

import com.example.GameEnglish.word_builder.st_easy.st_ex3_easy_inMenu;

import com.example.GameEnglish.modify_word;
import com.example.GameEnglish.word_Image_object;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;

public class GridviewAdapter extends BaseAdapter {
    List<String> lstSource;
    Context context;
    ArrayList<String> wordset = new ArrayList<>();
    ArrayList<String> tagset = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String mode;
    Intent intent;
    int color;
    ArrayList<String> tag = new ArrayList<>();

    public GridviewAdapter(List<String> lstSource, Context context,String mode,int color,ArrayList<String> tag) {
        this.lstSource = lstSource;
        this.context = context;
        this.mode = mode;
        this.color = color;
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return lstSource.size();
    }

    @Override
    public Object getItem(int position) {
        return lstSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        Button button = null;

        final TTS tts = new TTS(context);

        button = new Button(context);
        button.setLayoutParams(new GridView.LayoutParams(220, 220));
        button.setPadding(0, 0, 0, 0);
        button.setBackgroundResource(color);
        button.setTextColor(parseColor("#ffffff"));

//        if (tag != null) {
//            button.setTag(tag.get(position));
//        }
        button.setText(lstSource.get(position));
        button.setTag(position);
        final Button finalButton = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context); // ลบค่า I ในหน้าต่อไป
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("key").apply();
                editor.remove("first").apply();
                editor.remove("array").apply();
                editor.remove("Charecter_Score").apply();
                editor.remove("Score").apply();

                final int count = (int) finalButton.getTag(); // count array wordset for next and back
                wordset = (ArrayList<String>) lstSource;
                tagset = (ArrayList<String>) tag;

                switch (mode){
                    case "easy" :

                        PopupMenu popupMenu_ex3 = new PopupMenu(context,v);
                        popupMenu_ex3.getMenuInflater().inflate(R.menu.game_menu,popupMenu_ex3.getMenu());
                        popupMenu_ex3.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.play:
                                        intent = new Intent(context, ex3_easy_game.class);
                                        intent.putExtra("countarray",count);
                                        intent.putExtra("wordset", tagset); //ส่งค่าไปอีก activity
                                        context.startActivity(intent);
                                        return true;
                                    case  R.id.voice :
                                        tts.speak(String.valueOf(tag.get(count)));
                                        return true;
                                }
                                return true;
                            }
                        });

                        popupMenu_ex3.show();
                        break;



                    case "flash_card_game" :

                        PopupMenu popupMenu_ex2 = new PopupMenu(context,v);
                        popupMenu_ex2.getMenuInflater().inflate(R.menu.game_menu,popupMenu_ex2.getMenu());
                        popupMenu_ex2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.play:
                                        intent = new Intent(context, flash_card_game.class);
                                        intent.putExtra("wordset", tagset); //ส่งค่าไปอีก activity
                                        intent.putExtra("countarray",count);
                                        context.startActivity(intent);
                                        return true;
                                    case  R.id.voice :
                                        tts.speak(String.valueOf(tag.get(count)));
                                        return true;

                                }
                                return true;
                            }
                        });

                        popupMenu_ex2.show();
                        break;

                    case "st_ex2" :
                        intent = new Intent(context, st_flash_card_inMenu.class);
                        intent.putExtra("Groupname",finalButton.getText());
                        intent.putExtra("wordset", wordset); //ส่งค่าไปอีก activity
                        context.startActivity(intent);
                        break;
                    case "st_easy" :
                        intent = new Intent(context, st_ex3_easy_inMenu.class);
                        intent.putExtra("Groupname",finalButton.getText());
                        intent.putExtra("wordset", wordset); //ส่งค่าไปอีก activity
                        context.startActivity(intent);
                        break;


                    case "ex3_easy_game_st" :
                        intent = new Intent(context, ex3_easy_game_st.class);
                        intent.putExtra("Groupname",finalButton.getText());
                        intent.putExtra("countarray",count);
                        intent.putExtra("wordset", wordset); //ส่งค่าไปอีก activity
                        context.startActivity(intent);
                        break;


                    case "Sentence_Data" :
                        tts.speak(String.valueOf(finalButton.getText()));
                        break;
                    case "Word_data" :
                        final Dialog meaning = new Dialog(context);
                        meaning.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                        meaning.setContentView(R.layout.meaning_popup);
                        ImageView close = meaning.findViewById(R.id.this_back);
                        ImageView word_Image = meaning.findViewById(R.id.word_Image);
                        ImageView voice_word = meaning.findViewById(R.id.voice_word);
                        ImageView voice_mean = meaning.findViewById(R.id.voice_mean);
                        TextView word = meaning.findViewById(R.id.word);
                        TextView mean = meaning.findViewById(R.id.meaning);

                        final TTS tts = new TTS(context);
                        DatabaseHelper db = new DatabaseHelper(context);


                        final String get_word = String.valueOf(finalButton.getText());
                        word.setText(get_word);


                        ArrayList<word_Image_object> path_image = db.get_Image_word(get_word);

                        path_image.get(0).setDefualt_Image(null); //เอารูปออกชั่วคราว

                        if (path_image.get(0).getDefualt_Image() != null) {
                            int set_image = context.getResources().getIdentifier(path_image.get(0).getDefualt_Image(), "drawable", context.getPackageName());
                            word_Image.setImageResource(set_image);

                            if (path_image.get(0).getDefualt_Image().equals("null")){
                                File file = new File(path_image.get(0).getPath_Image());
                                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                word_Image.setImageBitmap(myBitmap);
                            }

                        } else if (path_image.get(0).getPath_Image() != null) {
                            File file = new File(path_image.get(0).getPath_Image());
                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            word_Image.setImageBitmap(myBitmap);
                        } else {
                            word_Image.setVisibility(View.GONE);
                        }

                        voice_word.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tts.speak(get_word);
                            }
                        });



                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                meaning.dismiss();
                            }
                        });

                        meaning.show();
                        break;

                    case "Delete_Mod_Word" :
                        intent = new Intent(context, modify_word.class);
                        intent.putExtra("word",finalButton.getText());
                        context.startActivity(intent);
                        break;


                    case "exercise2_game_st" :
                        intent = new Intent(context, flash_card_game_st.class);
                        intent.putExtra("Groupname",finalButton.getText());
                        intent.putExtra("countarray",count);
                        context.startActivity(intent);
                        break;

                }
            }
        }); return button;
    }

}
