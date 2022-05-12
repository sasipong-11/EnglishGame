package com.example.GameEnglish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class modify_word extends AppCompatActivity {

    ImageView upload_picture;
    Bitmap image = null;
    int SELECT_PICTURE = 15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_word);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("แก้ไขคำศัพท์");
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

        Bundle bundle = getIntent().getExtras();
        final String word = bundle.getString("word");

        final DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        final EditText editText = findViewById(R.id.Edit_Word);
        Button delete = findViewById(R.id.Delete);
        Button modify = findViewById(R.id.modify);
        upload_picture = findViewById(R.id.upload_image);
        editText.setText(word);

        //แสดงรูปจาก path ใช้ object เพราะว่ามันมีจะรูปที่เป็น  defual กับรูปที่เก็บไว้ในเครื่อง
        ArrayList<word_Image_object> path_image2  = databaseHelper.get_Image_word(word);

        path_image2.get(0).setDefualt_Image(null); //เอารูปออกชั่วคราว

        if (path_image2.get(0).getDefualt_Image() != null ) {
            int set_image = getResources().getIdentifier(path_image2.get(0).getDefualt_Image(), "drawable",getPackageName());
            upload_picture.setImageResource(set_image);

            if (path_image2.get(0).getDefualt_Image().equals("null")){
                File file = new File(path_image2.get(0).getPath_Image());
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                upload_picture.setImageBitmap(myBitmap);
            }

        } else if (path_image2.get(0).getPath_Image() != null) {
            File file = new File(path_image2.get(0).getPath_Image());
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            upload_picture.setImageBitmap(myBitmap);
        } else {
            //upload_picture.setVisibility(View.GONE);
        }

        upload_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
            }
        });


        modify.setOnClickListener(new View.OnClickListener() {
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

                     String Word_Mod = String.valueOf(editText.getText());
                     databaseHelper.update_word(String.valueOf(word), Word_Mod, path_image);
                     Add_Word.close_activity.finish();
                     Intent intent = new Intent(getApplicationContext(), Add_Word.class);
                     startActivity(intent);
                     finish();
                 }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean delete = databaseHelper.delete_word(String.valueOf(word));
                if (delete) {
                    Add_Word.close_activity.finish();
                    Intent intent = new Intent(getApplicationContext(),Add_Word.class);
                    startActivity(intent);
                    finish();
                }
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
                upload_picture.setImageBitmap(image);
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
