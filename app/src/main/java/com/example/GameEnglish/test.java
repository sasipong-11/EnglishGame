package com.example.GameEnglish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ImageView imageView = findViewById(R.id.test);

        File file = new File("storage/emulated/0/MyDocument/test.jpeg");
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(myBitmap);


        //date_time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String FileName = timeStamp;


    }
}
