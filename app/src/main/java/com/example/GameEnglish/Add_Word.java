package com.example.GameEnglish;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GameEnglish.word_builder.GridviewAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Add_Word extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    GridView gridView;
    GridviewAdapter gridviewAdapter;
    Dialog dialog;
    Bitmap image = null;
    int SELECT_PICTURE = 11;
    public static Activity close_activity;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_word);

        close_activity = this;

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("Add Vocabulary");
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

        dialog = new Dialog(this);
        databaseHelper = new DatabaseHelper(this);
        gridView = findViewById(R.id.GridView_Sentence);
        ArrayList Word = databaseHelper.queryword("Word");

        gridviewAdapter = new GridviewAdapter(Word,this,"Delete_Mod_Word",R.drawable.radius_button_color_ex2,null);
        gridView.setAdapter(gridviewAdapter);

        FloatingActionButton Add = findViewById(R.id.Add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_word();
            }
        });
    }

    public void Add_word(){
        dialog.setContentView(R.layout.addword_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_radius_while);
        final EditText editText = dialog.findViewById(R.id.Add_Word);
        Button button = dialog.findViewById(R.id.CF);
        imageView = dialog.findViewById(R.id.upload_image);
        //imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),"กรุณาพิมพ์ข้อความ",Toast.LENGTH_SHORT).show();
                } else if (editText.getText().length() <= 2){
                    Toast.makeText(getApplicationContext(),"ข้อความสั่นเกินไป",Toast.LENGTH_SHORT).show();
                } else if (editText.getText().length() > 12){
                    Toast.makeText(getApplicationContext(),"ข้อความยาวกินไป",Toast.LENGTH_SHORT).show();
                } else {

                    String path_image = null; //เก็บ path รูป
                    byte[] inputData; //ถ้าไม่ได้อัพรูปใหม่ จะใช้รูปเก่า
                    if (image != null) {
                        inputData = convertBitmapIntoByteArray(); //แปลงรูปเป็น byte
                        path_image = export_image(inputData);
                    }

                    Boolean Check_Word = databaseHelper.AddWord(String.valueOf(editText.getText()),"Word",path_image);
                    if (Check_Word) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), Add_Word.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"มีคำในฐานข้อมูลแล้ว",Toast.LENGTH_SHORT).show();
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
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
            }
        });

        ImageView goBack = dialog.findViewById(R.id.this_back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
            //Toast.makeText(getApplicationContext(), "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return file_name;
    }

}
