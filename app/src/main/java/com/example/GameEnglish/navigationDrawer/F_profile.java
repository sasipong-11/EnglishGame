package com.example.GameEnglish.navigationDrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.example.GameEnglish.R;
import com.example.GameEnglish.menu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class F_profile extends AppCompatActivity {

    EditText Username,Fullname,Age,PW,PW_CF;
    ImageView profile;
    RadioButton male,female;
    Button update;
    String getsex,sex;
    String getUsername,getFullname,getAge,Picture,UserID,Password,Password_CF;
    static final int SELECT_PICTURE = 100;
    Bitmap image = null;
    boolean selectpicture = true;

    byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        final SharedPreferences user = getSharedPreferences("User", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = user.edit();
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        final TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("Edit Profile");
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

        Username = findViewById(R.id.Username);
        PW = findViewById(R.id.Password);
        PW_CF = findViewById(R.id.Password_CF);
        Fullname = findViewById(R.id.Fullname);
        Age = findViewById(R.id.Age);
        profile = findViewById(R.id.profile);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        update = findViewById(R.id.update);

        Picture = user.getString("Picture",null);
        getUsername = user.getString("Username",null);
        Password = user.getString("Password",null);
        getFullname = user.getString("Fullname",null);
        getAge = String.valueOf(user.getInt("Age",0));
        getsex = user.getString("sex",null);
        UserID = user.getString("UserID",null);

        if(Picture != null){
//            bytes = Base64.decode(Picture,Base64.DEFAULT); //แปลง String เป็น byte
//            Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
            File file = new File(Picture);
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            profile.setImageBitmap(myBitmap); }

        Username.setText(getUsername);
        PW.setText(Password);
        PW_CF.setText(Password);
        Fullname.setText(getFullname);
        Age.setText(getAge);

        if (getsex.equals("ชาย")){
            male.setChecked(true);
            sex = "ชาย";
        } else {
            female.setChecked(true);
            sex = "หญิง";
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Username.getText().toString().equals(getUsername) && Fullname.getText().toString().equals(getFullname) &&
                        Age.getText().toString().equals(getAge) && getsex.equals(sex) && selectpicture &&
                PW.getText().toString().equals(Password) && PW_CF.getText().toString().equals(Password)){
                    Toast.makeText(getApplicationContext(),"ไม่พบการเปลี่ยนแปลง",Toast.LENGTH_SHORT).show();
                }
                else {

                    Boolean Check_userID = databaseHelper.Check_IDUser(Username.getText().toString()); //ตรวจสอบว่าไอดีซ้ำกันไหม

                    if (!PW.getText().toString().equals(PW_CF.getText().toString()))
                    {
                        PW.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                        PW_CF.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                        Toast.makeText(getApplicationContext(),"รหัสผ่านไม่ตรงกัน",Toast.LENGTH_SHORT).show();
                    } else {
                        if (Username.getText().toString().equals(getUsername)){
                            String path_image = Picture;//เก็บ path รูป
                            byte[] inputData = bytes; //ถ้าไม่ได้อัพรูปใหม่ จะใช้รูปเก่า
                            if (image != null) {
                                inputData = convertBitmapIntoByteArray(); //แปลงรูปเป็น byte
                                path_image = export_image(inputData);
                            }
                            databaseHelper.update_user(Username.getText().toString(),Fullname.getText().toString()
                                    ,Integer.parseInt(Age.getText().toString()),sex,path_image, UserID,PW.getText().toString());

                            editor.putString("Username",Username.getText().toString());
                            editor.putString("Password",PW.getText().toString());
                            editor.putString("Fullname",Fullname.getText().toString());
                            editor.putInt("Age", Integer.parseInt(Age.getText().toString()));
                            editor.putString("sex",sex);
                            if(image != null){
                                //String saveThis = Base64.encodeToString(inputData, Base64.DEFAULT);//แปลง byte เป็น String
                                editor.putString("Picture",path_image);}
                            editor.commit();

                            finish();
                            Toast.makeText(getApplicationContext(),"แก้ไขแล้ว",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(F_profile.this, menu.class);
                            menu.close_activity.finish(); //ปิดหน้าเมนู รีโหลด
                            startActivity(intent);
                        } else {
                            if (Check_userID){
                                String path_image = null; //เก็บ path รูป
                                byte[] inputData = bytes; //ถ้าไม่ได้อัพรูปใหม่ จะใช้รูปเก่า
                                if (image != null) {
                                    inputData = convertBitmapIntoByteArray(); //แปลงรูปเป็น byte
                                    path_image = export_image(inputData);
                                }
                                databaseHelper.update_user(Username.getText().toString(),Fullname.getText().toString()
                                        ,Integer.parseInt(Age.getText().toString()),sex,path_image, UserID,PW.getText().toString());

                                editor.putString("Username",Username.getText().toString());
                                editor.putString("Password",PW.getText().toString());
                                editor.putString("Fullname",Fullname.getText().toString());
                                editor.putInt("Age", Integer.parseInt(Age.getText().toString()));
                                editor.putString("sex",sex);
                                if(image != null){
                                    //String saveThis = Base64.encodeToString(inputData, Base64.DEFAULT);//แปลง byte เป็น String
                                    editor.putString("Picture",path_image);}
                                editor.commit();

                                finish();
                                Toast.makeText(getApplicationContext(),"แก้ไขแล้ว",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(F_profile.this, menu.class);
                                menu.close_activity.finish(); //ปิดหน้าเมนู รีโหลด
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "ชื่อผู้ใช้งานนี้มีคนใช้แล้ว กรุณาเปลี่ยนชื่อผู้ใช้งาน", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURE)
        {
            if(data == null) return;
            Uri imageUri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                profile.setImageBitmap(image);
                selectpicture = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    sex = "ชาย";
                break;
            case R.id.female:
                if (checked)
                    sex = "หญิง";
                break;
        }
    }

    private byte[] convertBitmapIntoByteArray() { //ลดขนาดรูป แปลงรูป
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, stream);//ขนาดภาพที่ลดลง
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    public String export_image(byte[] data){

        String file_name = null;

        //date_time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //สร้างรูปใน Mydoc
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MyDocument/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdir();
            file.canExecute();
        }
        try {
            file_name = directory_path  + timeStamp + ".jpeg";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
            fileOutputStream.write(data);
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return file_name;
    }
}
