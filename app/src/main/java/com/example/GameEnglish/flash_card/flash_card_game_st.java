package com.example.GameEnglish.flash_card;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.MusicBG.HomeWatcher;
import com.example.GameEnglish.MusicBG.MusicService;
import com.example.GameEnglish.R;
import com.example.GameEnglish.Score_word_builder_word;
import com.example.GameEnglish.TTS;
import com.example.GameEnglish.recyclerView_Ranking.Ranking_Adapter;
import com.example.GameEnglish.recyclerView_Ranking.Ranking_Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class flash_card_game_st extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ImageView imageView,ch1,ch2,ch3;
    ImageView next,back,voice;

    int first = 0 ; //เช็คการทำงานรอบแรก
    int count = 0; //ให้ index array ตัวแรกที่ 0

    SharedPreferences sharedPreferences,user; //เก็บค่า I ไม่ให้หาย
    String Groupname,ArraySet;

    String Array_choice1,Array_choice2,Array_char;

    ArrayList<Object_Choice> Char_set = new ArrayList<>();
    DatabaseHelper databaseHelper;

    Dialog dialog,dialog_rank,dialog_correct,dialog_setAnser,cf_back; //popup score

    private RecyclerView RecyclerView;
    private RecyclerView.LayoutManager LayoutManager;
    private RecyclerView.Adapter Adapter;

    private int Score = 100;

    ArrayList<String> cerrent_Char = new ArrayList<>();
    ArrayList<String> cerrent_Score = new ArrayList<>();

    HomeWatcher mHomeWatcher;

    MediaPlayer correct,incorrect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_card_game);

        ////service Music Start!
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

        ////service Music Start!
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
        correct= MediaPlayer.create(getApplicationContext(),R.raw.correct);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("เแบบฝึกจัดอันดับ Flash card");
        Title.setTextSize(20);

        ImageView back_toolbar = toolbar.findViewById(R.id.back);
        ImageView show_menu = toolbar.findViewById(R.id.show_menu);

        back_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cf_back.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                cf_back.setContentView(R.layout.game_cf_back);

                Button Back = cf_back.findViewById(R.id.this_back);
                Button CF = cf_back.findViewById(R.id.CF);

                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cf_back.dismiss();
                    }
                });

                CF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                cf_back.show();
            }
        });

        show_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                popupMenu.inflate(R.menu.menu_toolbar);
                popupMenu.setOnMenuItemClickListener(flash_card_game_st.this);
                Menu menu = popupMenu.getMenu();

                if (mServ.mPlayer == null) {
                    menu.getItem(0).setTitle("Turn on music");
                }
                popupMenu.show();
            }
        });

        dialog = new Dialog(this);
        dialog_rank = new Dialog(this);
        dialog_correct = new Dialog(this);
        dialog_setAnser = new Dialog(this);
        cf_back = new Dialog(this);

        final TTS tts = new TTS(this);
        databaseHelper = new DatabaseHelper(this);

        user = getSharedPreferences("User", Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        Groupname = bundle.getString("Groupname");

        LoadInt();

        if (first == 0){
            //count = bundle.getInt("countarray");
            Char_set = databaseHelper.test(Groupname,user.getString("UserID",null));
            first++;
            checkFIrst(first);

        } else {
            LoadArray();
            String[] Char = Array_char.split(",");
            String[] Choice1 = Array_choice1.split(",");
            String[] Choice2 = Array_choice2.split(",");

            ArrayList<String> Char_load = new ArrayList<>(Arrays.asList(Char));
            ArrayList<String> Choice1_load = new ArrayList<>(Arrays.asList(Choice1));
            ArrayList<String> Choice2_load = new ArrayList<>(Arrays.asList(Choice2));

            for(int i=0;i<Char_load.size();i++){
                Object_Choice object_choice = new Object_Choice(Choice1_load.get(i),Choice2_load.get(i),Char_load.get(i));
                Char_set.add(object_choice);
            }
            Load_Array_Score();
        }

        imageView = findViewById(R.id.picture);
        ch1 = findViewById(R.id.ch1);
        ch2 = findViewById(R.id.ch2);
        ch3 = findViewById(R.id.ch3);

        next = findViewById(R.id.next);
        back = findViewById(R.id.back_this);
        voice = findViewById(R.id.voice);

        if (count == Char_set.size()){ //เอาไว้เช็คเเมื่อข้ามไปตอบตัวสุดท้ายจะไม่ได้ส่งคำตอบเลย จะกลับมาตัวก่อนหน้าแทน
            count--;
            SaveInt(count);
        }

        ArrayList<String> Choice = new ArrayList<>();
        Choice.add(Char_set.get(count).getChoice1());
        Choice.add(Char_set.get(count).getChoice2());

        Character character = databaseHelper.get_choice_ex2_st(Choice);

        //set image
        String get_image = character.getImage();
        int set_image = getResources().getIdentifier(get_image , "drawable", getPackageName());

        String get_char1 = character.getCorrect();
        final int cha1 = getResources().getIdentifier(get_char1 , "drawable", getPackageName());

        String get_char2 = character.getChoice().get(0);
        int cha2 = getResources().getIdentifier(get_char2 , "drawable", getPackageName());

        String get_char3 = character.getChoice().get(1);
        int cha3 = getResources().getIdentifier(get_char3 , "drawable", getPackageName());

        final ArrayList<Integer> random = new ArrayList<>();
        random.add(cha1);
        random.add(cha2);
        random.add(cha3);

        //random in arraylist
        Collections.shuffle(random);

        imageView.setImageResource(set_image);

        ch1.setImageResource(random.get(0));
        ch2.setImageResource(random.get(1));
        ch3.setImageResource(random.get(2));

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (random.get(0) == cha1){
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        correct= MediaPlayer.create(getApplicationContext(),R.raw.correct);
                    }
                    correct.start();
                    Toast.makeText(getApplicationContext(),"คำตอบถูกต้อง",Toast.LENGTH_SHORT).show();
                    Popup_Correct();
                    cerrent_Score.add(String.valueOf(Score));
                    cerrent_Char.add(Char_set.get(count).getChar());
                    SaveArray_Score(cerrent_Char,cerrent_Score);

                    try { //ถ้าไม่ทำ try catch ไว้ กดถูกไปเรื่อยๆหน้าสุดท้ายจะ error เนื่องจาก index ของ count
                        Char_set.remove(count);
                    } catch (Exception e) {
                        Log.d("exception",e.toString());
                    }

                } else {
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
                    }
                    incorrect.start();
                    Score = Score - 5;
                    Toast.makeText(getApplicationContext(),"คำตอบผิด",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (random.get(1) == cha1){
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        correct= MediaPlayer.create(getApplicationContext(),R.raw.correct);
                    }
                    correct.start();
                    Toast.makeText(getApplicationContext(),"คำตอบถูกต้อง",Toast.LENGTH_SHORT).show();
                    Popup_Correct();
                    cerrent_Score.add(String.valueOf(Score));
                    cerrent_Char.add(Char_set.get(count).getChar());
                    SaveArray_Score(cerrent_Char,cerrent_Score);

                    try {
                        Char_set.remove(count);
                    } catch (Exception e) {
                        Log.d("exception ",e.toString());
                    }

                } else {
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
                    }
                    incorrect.start();
                    Score = Score - 5;
                    Toast.makeText(getApplicationContext(),"คำตอบผิด",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (random.get(2) == cha1){
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        correct= MediaPlayer.create(getApplicationContext(),R.raw.correct);
                    }
                    correct.start();
                    Toast.makeText(getApplicationContext(),"คำตอบถูกต้อง",Toast.LENGTH_SHORT).show();
                    Popup_Correct();
                    cerrent_Score.add(String.valueOf(Score));
                    cerrent_Char.add(Char_set.get(count).getChar());
                    SaveArray_Score(cerrent_Char,cerrent_Score);

                    try {
                        Char_set.remove(count);
                    } catch (Exception e) {
                        Log.d("Exception",e.toString());
                    }

                } else {
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
                    }
                    incorrect.start();
                    Score = Score - 5;
                    Toast.makeText(getApplicationContext(),"คำตอบผิด",Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count >= Char_set.size()){
                    Toast.makeText(flash_card_game_st.this,"ไม่พบคำถัดไป",Toast.LENGTH_SHORT).show();
                    count--;
//                    Popup_score();
                } else {
                    StringBuilder Sum_Char = new StringBuilder(); // เซฟ arrray Char_set
                    StringBuilder Sum_Choice1 = new StringBuilder(); // เซฟ arrray Char_set
                    StringBuilder Sum_Choice2 = new StringBuilder(); // เซฟ arrray Char_set

                    for (int i = 0; i < Char_set.size(); i++) {
                        Sum_Char.append(Char_set.get(i).getChar()).append(",");
                        Sum_Choice1.append(Char_set.get(i).getChoice1()).append(",");
                        Sum_Choice2.append(Char_set.get(i).getChoice2()).append(",");                    }
                    //SaveArray(Sumwordset.toString());

                    SaveArray_char(Sum_Char.toString());
                    SaveArray_choice1(Sum_Choice1.toString());
                    SaveArray_choice2(Sum_Choice2.toString());

                    SaveInt(count);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count < 0){
                    Toast.makeText(flash_card_game_st.this,"ไม่พบคำก่อนหน้า",Toast.LENGTH_SHORT).show();
                    count++;
                } else {
                    SaveInt(count);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(Char_set.get(count).getChar());
            }
        });
    }

    public void SaveInt(int value){ //เซฟค่า count
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", value);
        editor.commit();
    }
    public void LoadInt(){ // โหลดค่า count and first
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        count = sharedPreferences.getInt("key", 0);
        first = sharedPreferences.getInt("first",0);
    }

    public void checkFIrst(int first){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("first",first);
        editor.commit();
    }

    public void SaveArray_char(String array){ //เซฟค่า count
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("char",array);
        editor.commit();
    }

    public void SaveArray_choice1(String array){ //เซฟค่า count
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("choice1",array);
        editor.commit();
    }

    public void SaveArray_choice2(String array){ //เซฟค่า count
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("choice2",array);
        editor.commit();
    }

    public void LoadArray(){ // โหลดค่า count
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Array_char = sharedPreferences.getString("char", "Hello!");
        Array_choice1 = sharedPreferences.getString("choice1", "Hello!");
        Array_choice2 = sharedPreferences.getString("choice2", "Hello!");

    }

    public void SaveArray_Score(ArrayList<String> Char,ArrayList<String> score){ //เก็บคะแนนไว้ในอาเรย์กับตัวอักษรของคะแนนนั้น แล้วส่ง update ทีเดียว
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder character = new StringBuilder(); // เซฟ arrray Char_set
        for (int i = 0; i < Char.size(); i++) {
            character.append(Char.get(i)).append(",");
        }

        StringBuilder Array_Score = new StringBuilder(); // เซฟ arrray ของคะแนนตัวอักษรปัจจุบัน
        for (int i = 0; i < score.size(); i++) {
            Array_Score.append(score.get(i)).append(",");
        }

        editor.putString("Charecter_Score", String.valueOf(character));
        editor.putString("Score", String.valueOf(Array_Score));

        editor.commit();
    }

    public void Load_Array_Score(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String[] Char = sharedPreferences.getString("Charecter_Score","null").split(",");
        String[] Score = sharedPreferences.getString("Score","null").split(",");
        cerrent_Char.addAll(Arrays.asList(Char));
        cerrent_Score.addAll(Arrays.asList(Score));
    }

    public void Popup_score(){
        TextView Sum_Score,text1,text2,text3,text4,text5,score1,score2,score3,score4,score5;
        Button goRank;
        ImageView goBack;

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
        dialog.setContentView(R.layout.score_popup);

        Score_word_builder_word score = databaseHelper.Score_ex2(Groupname,user.getString("UserID",null)
                ,user.getString("Fullname",null));

        Sum_Score = dialog.findViewById(R.id.Sum_Score);
        text1 = dialog.findViewById(R.id.text1);
        text2 = dialog.findViewById(R.id.text2);
        text3 = dialog.findViewById(R.id.text3);
        text4 = dialog.findViewById(R.id.text4);
        text5 = dialog.findViewById(R.id.text5);
        score1 = dialog.findViewById(R.id.Score_text1);
        score2 = dialog.findViewById(R.id.Score_text2);
        score3 = dialog.findViewById(R.id.Score_text3);
        score4 = dialog.findViewById(R.id.Score_text4);
        score5 = dialog.findViewById(R.id.Score_text5);

        goBack = dialog.findViewById(R.id.this_back);
        goRank = dialog.findViewById(R.id.Rank);

        text1.setText(score.getWord().get(0));
        text2.setText(score.getWord().get(1));
        text3.setText(score.getWord().get(2));
        text4.setText(score.getWord().get(3));
        text5.setText(score.getWord().get(4));

        score1.setText(String.valueOf(score.getScore().get(0)));
        score2.setText(String.valueOf(score.getScore().get(1)));
        score3.setText(String.valueOf(score.getScore().get(2)));
        score4.setText(String.valueOf(score.getScore().get(3)));
        score5.setText(String.valueOf(score.getScore().get(4)));

        ArrayList<Integer> averrage = new ArrayList<>(score.getScore());
        int sum_average = Average(averrage);
        Sum_Score.setText("คะแนนรวม  " +String.valueOf(sum_average));

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        goRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Popup_rank();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface Dialog) {
                // if from activity
                dialog.dismiss();
                finish();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void Popup_rank(){
        Button myScore;
        ImageView goBack;

        dialog_rank.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
        dialog_rank.setContentView(R.layout.ranking_popup);

        ArrayList<Ranking_Item> ranking_items = databaseHelper.rank_ex2(Groupname);

        RecyclerView = dialog_rank.findViewById(R.id.recyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new Ranking_Adapter(ranking_items);

        RecyclerView.setLayoutManager(LayoutManager);
        RecyclerView.setAdapter(Adapter);


        goBack = dialog_rank.findViewById(R.id.this_back);
        myScore = dialog_rank.findViewById(R.id.MyScore);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_rank.dismiss();
                finish();
            }
        });

        myScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_rank.dismiss();
                Popup_score();
            }
        });

        dialog_rank.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // if from activity
                dialog_rank.dismiss();
                finish();
            }
        });

        dialog_rank.setCanceledOnTouchOutside(false);
        dialog_rank.show();
    }

//    public void Popup_setAnswer(){
//        dialog_setAnser.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
//        dialog_setAnser.setContentView(R.layout.game_st_confirm);
//
//        Button Back = dialog_setAnser.findViewById(R.id.this_back);
//        Button CF = dialog_setAnser.findViewById(R.id.CF);
//    }

    public void Popup_Correct(){
        dialog_correct.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
        dialog_correct.setContentView(R.layout.correct_popup);
        Button button = dialog_correct.findViewById(R.id.go_next);
        ImageView back = dialog_correct.findViewById(R.id.this_back);
        TextView textView = dialog_correct.findViewById(R.id.score_correct);
        textView.setText(String.valueOf(Score+" Score"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Char_set.size() == 0){
                    for (int i=0;i<cerrent_Char.size();i++) { //อัพเดตคะแนน
                        if (!cerrent_Char.get(i).equals("null")) {
                            String stID = databaseHelper.Find_stID_Char(cerrent_Char.get(i), Groupname);
                            databaseHelper.update_score_ex2(user.getString("UserID", null),
                                    Integer.parseInt(cerrent_Score.get(i)), stID);
                        }
                    }
                    dialog_correct.dismiss();
                    Popup_score();
                } else {
                    StringBuilder Sum_Char = new StringBuilder(); // เซฟ arrray Char_set
                    StringBuilder Sum_Choice1 = new StringBuilder(); // เซฟ arrray Char_set
                    StringBuilder Sum_Choice2 = new StringBuilder(); // เซฟ arrray Char_set

                    for (int i = 0; i < Char_set.size(); i++) {
                        Sum_Char.append(Char_set.get(i).getChar()).append(",");
                        Sum_Choice1.append(Char_set.get(i).getChoice1()).append(",");
                        Sum_Choice2.append(Char_set.get(i).getChoice2()).append(",");
                    }

//                    Log.d("rrrrrrrrr",Sum_Char.toString());
//                    Log.d("rrrrrrrrr",Sum_Choice1.toString());
//                    Log.d("rrrrrrrrr",Sum_Choice2.toString());

                    SaveArray_char(Sum_Char.toString());
                    SaveArray_choice1(Sum_Choice1.toString());
                    SaveArray_choice2(Sum_Choice2.toString());
                    SaveInt(count);

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    dialog_correct.dismiss();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_correct.dismiss();
                finish();
            }
        });

        dialog_correct.setCanceledOnTouchOutside(false);
        dialog_correct.show();
    }

    public int Average (ArrayList<Integer> number){
        int average = 0;
        for (int i=0;i<5;i++) {
            average += number.get(i);
        }
        average = average / 5;
        return  average;
    }

    ////service Music Start!
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isInteractive();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.close_music :
                mServ.stopMusic();
                return true;

            case R.id.set_Answer :
                dialog_setAnser.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                dialog_setAnser.setContentView(R.layout.game_st_confirm);

                Button Back = dialog_setAnser.findViewById(R.id.this_back);
                Button CF = dialog_setAnser.findViewById(R.id.CF);

                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_setAnser.dismiss();
                    }
                });

                CF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ทำต่อ
                        for (int i=0;i<cerrent_Char.size();i++) {
                            if (!cerrent_Char.get(i).equals("null")) {
                                String stID = databaseHelper.Find_stID_Char(cerrent_Char.get(i), Groupname);
                                databaseHelper.update_score_ex2(user.getString("UserID", null),
                                        Integer.parseInt(cerrent_Score.get(i)), stID);
                            }
                        }
                        dialog_setAnser.dismiss();
                        Popup_score();
                    }
                });
                dialog_setAnser.show();
                return true;

            case R.id.manual :
                final Dialog Manual = new Dialog(this);
                Manual.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                Manual.setContentView(R.layout.manual);

                ImageView Back_manual = Manual.findViewById(R.id.this_back);
                TextView Manual_text = Manual.findViewById(R.id.manual_text);

                Manual_text.setText("เลือกตคำศัพท์ที่ถูกต้องโดยฟังจากเสียงหรือรูปภาพ เมื่อตอบครบทุกข้อจะส่งคำตอบอัติโนมัติหรือถ้าทำไม่ครบทุกข้อสามารถกดส่งคำตอบได้ที่เมนูด้านขวาบนเลือก 'ส่งคำตอบ' \n");

                Back_manual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Manual.dismiss();
                    }
                });
//                Manual.setCanceledOnTouchOutside(false);
                Manual.show();
                return true;

            default: return false;
        }
    }

    @Override
    public void onBackPressed() {
        cf_back.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
        cf_back.setContentView(R.layout.game_cf_back);

        Button Back = cf_back.findViewById(R.id.this_back);
        Button CF = cf_back.findViewById(R.id.CF);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cf_back.dismiss();
            }
        });

        CF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cf_back.show();
    }
}
