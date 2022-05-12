package com.example.GameEnglish.word_builder.easy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.MusicBG.HomeWatcher;
import com.example.GameEnglish.MusicBG.MusicService;
import com.example.GameEnglish.word_builder.GridviewAdapter;
import com.example.GameEnglish.R;

import java.util.ArrayList;

public class ex3_easy_menu extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ArrayList<String> words,st;
    GridView gridView,gridView_admin;
    GridviewAdapter gridviewAdapter,gridviewAdapter_admin;

    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_builder_menu);

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("เลือกแบบฝึกซ้อม");
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

        dbHelper = new DatabaseHelper(getApplicationContext());

        words = dbHelper.queryword("word");
        gridView = findViewById(R.id.gridView);

        ArrayList<String> numbers = new ArrayList<>();
        for (int i=1;i<=words.size();i++){
            numbers.add(String.valueOf(i));
        }
        

        //create Girdview
        gridviewAdapter = new GridviewAdapter(words,this,"easy",R.drawable.radius_button_color1,words);
        gridView.setAdapter(gridviewAdapter);

        //gridview admin setting
        gridView_admin = findViewById(R.id.gridView_admin);
        st = dbHelper.GetGroupname("Setting_ex3_easy","st_ex3_easy_id");
        gridviewAdapter_admin = new GridviewAdapter(st,this,"ex3_easy_game_st",R.drawable.radius_button_color1,null);
        gridView_admin.setAdapter(gridviewAdapter_admin);

        TextView textHide = findViewById(R.id.noEX);

        if (st.size() != 0){
            textHide.setVisibility(View.INVISIBLE);
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
