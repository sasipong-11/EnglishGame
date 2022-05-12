package com.example.GameEnglish.flash_card;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.MusicBG.HomeWatcher;
import com.example.GameEnglish.MusicBG.MusicService;
import com.example.GameEnglish.R;
import com.example.GameEnglish.TTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class flash_card_game extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    TTS tts;
    ImageView imageView,ch1,ch2,ch3;
    ImageView next,back,voice;

    int first = 0 ; //เช็คการทำงานรอบแรก
    int count; //เช็คเริ่มเข้ามาที่ตัวอักษรไหน

    SharedPreferences sharedPreferences; //เก็บค่า I ไม่ให้หาย
    ArrayList<String> char_array = new ArrayList<>();

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

        incorrect = MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
        correct = MediaPlayer.create(getApplicationContext(),R.raw.correct);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("เแบบฝึกหัด Flash card ");
        Title.setTextSize(20);

        ImageView back_toolbar = toolbar.findViewById(R.id.back);
        ImageView show_menu = toolbar.findViewById(R.id.show_menu);

        back_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        show_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                popupMenu.inflate(R.menu.menu_toolbar);
                popupMenu.setOnMenuItemClickListener(flash_card_game.this);
                Menu menu = popupMenu.getMenu();

                if (mServ.mPlayer == null) {
                    menu.getItem(0).setTitle("Turn on music");
                }
                menu.getItem(2).setVisible(false);
                popupMenu.show();
            }
        });

        final TTS tts = new TTS(this);

        Bundle bundle = getIntent().getExtras();
        char_array = bundle.getStringArrayList("wordset");

        LoadInt();

        if (first == 0){
            count = bundle.getInt("countarray");
            first++;
            checkFIrst(first);
        }

        imageView = findViewById(R.id.picture);
        ch1 = findViewById(R.id.ch1);
        ch2 = findViewById(R.id.ch2);
        ch3 = findViewById(R.id.ch3);

        voice = findViewById(R.id.voice_tts);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back_this);


        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Character character = databaseHelper.character(char_array.get(count));
        Collections.shuffle(character.getChoice());

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

                    count++; //เมื่อตอบถูกจะไปคำศัพท์ถัดไปทันที
                    if(count >= char_array.size()){
                        Toast.makeText(flash_card_game.this,"ไม่พบคำถัดไป",Toast.LENGTH_SHORT).show();
                        count--;
                    } else {
                        SaveInt(count);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                } else {
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
                    }
                    incorrect.start();
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

                    count++;
                    if(count >= char_array.size()){
                        Toast.makeText(flash_card_game.this,"ไม่พบคำถัดไป",Toast.LENGTH_SHORT).show();
                        count--;
                    } else {
                        SaveInt(count);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                } else {
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
                    }
                    incorrect.start();
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

                    count++;
                    if(count >= char_array.size()){
                        Toast.makeText(flash_card_game.this,"ไม่พบคำถัดไป",Toast.LENGTH_SHORT).show();
                        count--;
                    } else {
                        SaveInt(count);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                } else {
                    if(correct.isPlaying() || incorrect.isPlaying()){
                        incorrect= MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
                    }
                    incorrect.start();
                    Toast.makeText(getApplicationContext(),"คำตอบผิด",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * call TTS
         */
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(char_array.get(count));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count >= char_array.size()){
                    Toast.makeText(flash_card_game.this,"ไม่พบคำถัดไป",Toast.LENGTH_SHORT).show();
                    count--;
                } else {
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
                    Toast.makeText(flash_card_game.this,"ไม่พบคำก่อนหน้า",Toast.LENGTH_SHORT).show();
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
                tts.speak(char_array.get(count));
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.close_music :
                mServ.stopMusic();
                return true;

            case R.id.manual :
                final Dialog Manual = new Dialog(this);
                Manual.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                Manual.setContentView(R.layout.manual);

                ImageView Back_manual = Manual.findViewById(R.id.this_back);
                TextView Manual_text = Manual.findViewById(R.id.manual_text);

                Manual_text.setText("เลือกตคำศัพท์ที่ถูกต้องโดยฟังจากเสียงหรือรูปภาพ \n");

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
}
