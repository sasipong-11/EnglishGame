package com.example.GameEnglish;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.GameEnglish.Edit_User_From_Admin.Call_GridAdapter_User;
import com.example.GameEnglish.MusicBG.HomeWatcher;
import com.example.GameEnglish.MusicBG.MusicService;
import com.example.GameEnglish.navigationDrawer.F_data;
import com.example.GameEnglish.navigationDrawer.F_profile;
import com.example.GameEnglish.navigationDrawer.F_setting;

import java.io.File;
import java.util.Objects;

public class menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    TextView Username,Fullname;

    SharedPreferences user;

    public TextView toolbar_text;

    public static Activity close_activity;//ปิดหน้าเมนูในหน้าแก้ไขโปรไฟล์

    HomeWatcher mHomeWatcher;

    Dialog dialog_logout;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        close_activity = this;

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

        toolbar_text = findViewById(R.id.titel_toolbar);
        toolbar_text.setText("Choose a Vocabulary Boards");

        user = getSharedPreferences("User", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
//            }
//        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
                if (mServ.mPlayer == null) {
                    menu.getItem(4).setTitle("Turn on music");
                } else {
                    menu.getItem(4).setTitle("Turn off music");
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dialog_logout = new Dialog(this);

        menu = navigationView.getMenu();
        if (!user.getString("Permission", null).equals("admin")) {
            menu.findItem(R.id.setting).setVisible(false);
            menu.findItem(R.id.EditUser).setVisible(false);
            menu.findItem(R.id.addPerson).setVisible(false);
        }

        //Text Profile
        View header = navigationView.getHeaderView(0);
        Username = header.findViewById(R.id.Username);
        Fullname = header.findViewById(R.id.Fullname);
        Username.setText(user.getString("Username",null));
        Fullname.setText(user.getString("Fullname",null));

        //Profile header
        ImageView imageView = header.findViewById(R.id.Profile);
        if (user.getString("Picture",null) != null){
//        byte[] bytes = Base64.decode(user.getString("Picture",null), Base64.DEFAULT); //แปลง String เป็น byte
//            Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);

            File file = new File(user.getString("Picture",null));
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new F_menu_home())
                    .commit();
            //navigationView.setCheckedItem(R.id.setting);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new F_menu_home()).addToBackStack(null).commit();
                Intent main = new Intent(getApplicationContext(),menu.class);
                startActivity(main);
                finish();
                break;
            case R.id.setting:
                Intent setting = new Intent(getApplicationContext(),F_setting.class);
                startActivity(setting);
                break;
            case R.id.edit:
                Intent editProfile = new Intent(getApplicationContext(), F_profile.class);
                startActivity(editProfile);
                break;
            case R.id.data:
                Intent myScpre = new Intent(getApplicationContext(), F_data.class);
                startActivity(myScpre);
                break;
            case R.id.EditUser:
                Intent editUser = new Intent(getApplicationContext(), Call_GridAdapter_User.class);
                startActivity(editUser);
                break;
            case R.id.addPerson:
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                break;
            case R.id.logout:

                dialog_logout.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
                dialog_logout.setContentView(R.layout.logout_confirm);

                Button Back = dialog_logout.findViewById(R.id.this_back);
                Button CF = dialog_logout.findViewById(R.id.CF);

                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_logout.dismiss();
                    }
                });

                CF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_logout.dismiss();
                        finish();
                        SharedPreferences.Editor editor = user.edit();
                        editor.clear();
                        editor.apply();
                        Intent Logout = new Intent(getApplicationContext(),login.class);
                        startActivity(Logout);
                    }
                });
                dialog_logout.show();
                break;

            case R.id.StopMusic:
                mServ.stopMusic();
//                if (mServ.mPlayer == null) {
//                    menu.getItem(4).setTitle("เปิดเสียงดนตรี");
//                } else {
//                    menu.getItem(4).setTitle("ปิดเสียงดนตรี");
//                }
                break;

            case R.id.credits:
                Intent credit_page = new Intent(getApplicationContext(),credits.class);
                startActivity(credit_page);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

//        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

//        if (mServ.mPlayer == null) {
//            menu.getItem(4).setTitle("เปิดเสียงดนตรี");
//        }

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
