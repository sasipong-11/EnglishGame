package com.example.GameEnglish.navigationDrawer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.MusicBG.HomeWatcher;
import com.example.GameEnglish.MusicBG.MusicService;
import com.example.GameEnglish.My_Score.My_Score_main_flash_card;
import com.example.GameEnglish.My_Score.My_Score_main_word_builder;
import com.example.GameEnglish.R;

import java.util.Objects;

public class F_data extends AppCompatActivity {

    Button score2,score3,Word;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

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

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("คะแนนข้อมูลผู้ใช้งาน");
        Title.setTextSize(20);

        ImageView back = toolbar.findViewById(R.id.back);
        ImageView show_menu = toolbar.findViewById(R.id.show_menu);
        show_menu.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        score2 = findViewById(R.id.myscore2);
        score3 = findViewById(R.id.myscore3);
        Word = findViewById(R.id.Word_Data);


        score2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), My_Score_main_flash_card.class);
                startActivity(intent);
            }
        });

        score3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), My_Score_main_word_builder.class);
                startActivity(intent);
            }
        });

        Word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), F_Word_data.class);
                startActivity(intent);
            }
        });



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
