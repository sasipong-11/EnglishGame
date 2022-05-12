package com.example.GameEnglish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText user,password,confirmPS,name,age;
    Button button;
    String sex = null;
    ImageView imageView;
    static final int SELECT_PICTURE = 100;
    Bitmap image = null;

    byte[] inputData = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("add user");
        Title.setTextSize(20);

        ImageView show_menu = toolbar.findViewById(R.id.show_menu);
        show_menu.setVisibility(View.GONE);

        ImageView back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = findViewById(R.id.selectPicture);

        user = findViewById(R.id.ID);
        password = findViewById(R.id.password);
        confirmPS = findViewById(R.id.psconfirm);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);

        button = findViewById(R.id.button);

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty()
                        || age.getText().toString().isEmpty() || sex == null){

                    Toast.makeText(Register.this,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();

                } else { if(!password.getText().toString().equals(confirmPS.getText().toString()))
                {
                    password.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    confirmPS.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(Register.this,"รหัสผ่านไม่ตรงกัน",Toast.LENGTH_SHORT).show();
                } else {
                    if (image == null) { //ถ้าไม่ได้เลือกรูปให้ใช้รูปค่า defualt
                        Drawable myDrawable = getResources().getDrawable(R.drawable.kidpicture);
                        imageView.setImageDrawable(myDrawable);
                        image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    }
                    //แปลงรูปเป็น byte ก่อน insert เข้า database
                    inputData = convertBitmapIntoByteArray();
                    String path_image = null; //เก็บ path รูป
                    path_image = export_image(inputData);


                    Boolean Check_userID = databaseHelper.Check_IDUser(user.getText().toString());
                    if (Check_userID) {
                        //insert table user
                        databaseHelper.insert_user(user.getText().toString(), password.getText().toString(), name.getText().toString(),
                                Integer.parseInt(age.getText().toString()), sex, path_image);
                        databaseHelper.insert_allScore(name.getText().toString());
                        finish();
                        Toast.makeText(Register.this, "เพิ่มผู้ใช้งานแล้ว", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "ชื่อผู้ใช้งานนี้มีคนใช้แล้ว กรุณาเปลี่ยนชื่อผู้ใช้งาน", Toast.LENGTH_SHORT).show();
                    }
                }
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
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
                imageView.setImageBitmap(image);

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
                    sex = "Male";
                break;
            case R.id.female:
                if (checked)
                    sex = "Female";
                break;
        }
    }

    private byte[] convertBitmapIntoByteArray() { //ลดขนาดรูป แปลงรูป
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, stream);//ขนาดภาพที่ลดลง
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
