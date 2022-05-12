package com.example.GameEnglish.Export_Import;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.GameEnglish.DatabaseHelper;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Export_Import {

    private Context context;
    private DatabaseHelper databaseHelper;
    private ArrayList<Import_object> objects = new ArrayList<>();

    public Export_Import(Context context){
        this.context = context;
    }

    public void export_ex2(ArrayList<String> GroupName,String export_name,String Click){

        databaseHelper = new DatabaseHelper(context);

        //generate data
        StringBuilder data = new StringBuilder();
        data.append("ex2");
        data.append("\n"+"choiceID,GroupName");
        objects.clear();


        for (int i=0;i<GroupName.size();i++){
            objects = databaseHelper.export_ex2(GroupName.get(i));
            for(int j = 0; j<objects.size(); j++){
                data.append("\n"+objects.get(j).getID()+","+objects.get(j).getGroupname());
            }
        }

        switch (Click) {

            case "export" :

                //export ไปที่เครื่อง
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MyDocument/";
                File file = new File(directory_path);
                if (!file.exists()) {
                    file.mkdir();
                    file.canExecute();
                }
                try {
                    String file_name = directory_path + export_name + ".csv";
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
                    fileOutputStream.write(data.toString().getBytes());
                    fileOutputStream.close();
                    Toast.makeText(context, "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                break;

            case "share" :

                try {
                    //saving the file into device
                    FileOutputStream out = context.openFileOutput(export_name + ".csv", Context.MODE_PRIVATE);
                    //OutputStream out = new FileOutputStream("data");
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    File filelocation = new File(context.getFilesDir(), export_name + ".csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.projectld.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, export_name);
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    context.startActivity(Intent.createChooser(fileIntent, "นำข้อมูลออกเสร็จสิ้น"));
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                break;
        }
    }

    public void export_ex3(ArrayList<String> GroupName, final String export_name,String Click){

        databaseHelper = new DatabaseHelper(context);

        //generate data
        final StringBuilder data = new StringBuilder();
        data.append("ex3");
        data.append("\n"+"WordID,GroupName");

        for (int i=0;i<GroupName.size();i++){
            objects = databaseHelper.export_ex3(GroupName.get(i));
            for(int j = 0; j<objects.size(); j++){
                data.append("\n"+objects.get(j).getID()+","+objects.get(j).getGroupname());
            }
        }

        switch (Click) {

            case "export" :
                //export ไปที่เครื่อง
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MyDocument/";
                File file = new File(directory_path);
                if (!file.exists()) {
                    file.mkdir();
                    file.canExecute();
                }
                try {
                    String file_name = directory_path + export_name + ".csv";
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
                    fileOutputStream.write(data.toString().getBytes());
                    fileOutputStream.close();
                    Toast.makeText(context, "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                break;

            case "share" :
                try {
                    //saving the file into device
                    FileOutputStream out = context.openFileOutput(export_name + ".csv", Context.MODE_PRIVATE);
                    //OutputStream out = new FileOutputStream("data");
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    File filelocation = new File(context.getFilesDir(), export_name + ".csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.projectld.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, export_name);
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    context.startActivity(Intent.createChooser(fileIntent, "แชร์ข้อมูลเสร็จสิ้น"));
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                break;
        }

    }

    public void export_Word(ArrayList<String> word,String export_name,String Click){

        databaseHelper = new DatabaseHelper(context);

        //generate data
        StringBuilder data = new StringBuilder();
        data.append("word");
        data.append("\n"+"id,word");

        for (int i=0;i<word.size();i++){
            data.append("\n"+i+","+word.get(i));
        }

        switch (Click) {

            case "export" :
                //export ไปที่เครื่อง
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MyDocument/";
                File file = new File(directory_path);
                if (!file.exists()) {
                    file.mkdir();
                    file.canExecute();
                }
                try {
                    String file_name = directory_path + export_name + ".csv";
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
                    fileOutputStream.write(data.toString().getBytes());
                    fileOutputStream.close();
                    Toast.makeText(context, "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                break;


            case "share" :
                try {
                    //saving the file into device
                    FileOutputStream out = context.openFileOutput(export_name + ".csv", Context.MODE_PRIVATE);
                    //OutputStream out = new FileOutputStream("data");
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    File filelocation = new File(context.getFilesDir(), export_name + ".csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.projectld.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, export_name);
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    context.startActivity(Intent.createChooser(fileIntent, "นำข้อมูลออกเสร็จสิ้น"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void import_csv (Uri uri){

        ArrayList<String> GroupName = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);

        try{
            // ContentValues cv = new ContentValues();
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            InputStreamReader isr = new InputStreamReader(inputStream);
            CSVReader dataRead = new CSVReader(isr);
            String[] import_data;
            String Ex = null;
            String get_Groupname = null;

            ArrayList<Import_object> object_ex2 = new ArrayList<>();
            Import_object Import_object;

            int is_First = 0; //เอาไว้เช็คการทำงานครั่งแรก

            while((import_data = dataRead.readNext())!=null) { //save data in object array

                if (is_First == 0) { //เช็คว่าทำการครั่งแรกใช่หรือไม่ เพื่อเก็บ get_groupname ซึ่งคือ ex2
                    Ex = import_data[0];
                    is_First++;
                }
                if (is_First >= 3){ //รอบแรกคือชื่อ ex2 รอบแรกสองคือ ชื่อคอลัม เช่น choiceID,groupname
                    Import_object = new Import_object(import_data[0],import_data[1]);
                    object_ex2.add(Import_object);

                    GroupName.add(import_data[1]); //เก็บ groupname ไว้ใน array จากนนั้นจะทำการลบคำซ้ำ

                    //cv.clear();
                } else {
                    is_First ++;
                }
            }
            Set<String> set = new HashSet<>(GroupName); //ลบชื่อกลุ่มซ้ำออก
            GroupName.clear();
            GroupName.addAll(set);
            boolean Check_Group = false;

            switch (Ex){

                case "ex2" :

                    for (int i=0;i<GroupName.size();i++){ //ตรวจสอบว่ามีชื่อกลุ่มซ้ำกันไหม
                        get_Groupname = databaseHelper.check_groupname_import(object_ex2.get(1).getGroupname(),"Setting_ex2"); //เช็คว่าชื่อ groupname ซ่ำกับในแอปหรือไม่
                        if (get_Groupname == null){
                            Check_Group = true;
                        }
                    }

                    if (Check_Group){
                        for (int i=0;i<object_ex2.size();i++){ //เพิ่มคำศัพท์เข้า db ทีละคำ
                            String choice = object_ex2.get(i).getID(); //get choiceID from table setting2
                            String groupname = object_ex2.get(i).getGroupname(); //get groupname from table setting2
                            databaseHelper.Import_ex2(choice,groupname);
                            Toast.makeText(context,"นำข้อมูลเข้าเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                        }

                        for (int j=0;j<GroupName.size();j++){ //เพิ่ม score = 0
                            databaseHelper.insert_score_ex2(GroupName.get(j));
                        }

                    } else {
                        Toast.makeText(context,"แบบทดสอบชื่อซ้ำกัน กรุณาเปลี่ยนชื่อแบบทดสอบในแอปพลิเคชัน",Toast.LENGTH_SHORT).show();
                    }
                    dataRead.close();
                    break;

                case "ex3" :

                    for (int i=0;i<GroupName.size();i++){ //ตรวจสอบว่ามีชื่อกลุ่มซ้ำกันไหม
                        get_Groupname = databaseHelper.check_groupname_import(object_ex2.get(1).getGroupname(),"Setting_ex3_easy"); //เช็คว่าชื่อ groupname ซ่ำกับในแอปหรือไม่
                        if (get_Groupname == null){
                            Check_Group = true;
                        }
                    }

                    if (Check_Group){
                        for (int i=0;i<object_ex2.size();i++){
                            String choice = object_ex2.get(i).getID(); //get choiceID from table setting2
                            String groupname = object_ex2.get(i).getGroupname(); //get groupname from table setting2
                            databaseHelper.Import_ex3(choice,groupname);
                            Toast.makeText(context,"นำข้อมูลเข้าเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                        }

                        for (int j=0;j<GroupName.size();j++){ //เพิ่ม score = 0
                            databaseHelper.insert_score_easy(GroupName.get(j));

                        }
                    } else {
                        Toast.makeText(context,"แบบทดสอบชื่อซ้ำกัน กรุณาเปลี่ยนชื่อแบบทดสอบในแอปพลิเคชัน",Toast.LENGTH_SHORT).show();
                    }
                    dataRead.close();
                    break;

                case "ex4" :

                    for (int i=0;i<GroupName.size();i++){ //ตรวจสอบว่ามีชื่อกลุ่มซ้ำกันไหม
                        get_Groupname = databaseHelper.check_groupname_import(object_ex2.get(1).getGroupname(),"Setting_ex3_normal"); //เช็คว่าชื่อ groupname ซ่ำกับในแอปหรือไม่
                        if (get_Groupname == null){
                            Check_Group = true;
                        }
                    }

                    if (Check_Group){
                        for (int i=0;i<object_ex2.size();i++){
                            String choice = object_ex2.get(i).getID(); //get choiceID from table setting2
                            String groupname = object_ex2.get(i).getGroupname(); //get groupname from table setting2
                            Toast.makeText(context,"นำข้อมูลเข้าเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                        }


                        for (int j=0;j<GroupName.size();j++){ //เพิ่ม score = 0
                            databaseHelper.insert_score_normal(GroupName.get(j));
                        }

                    } else {
                        Toast.makeText(context,"แบบทดสอบชื่อซ้ำกัน กรุณาเปลี่ยนชื่อแบบทดสอบในแอปพลิเคชัน",Toast.LENGTH_SHORT).show();
                    }
                    dataRead.close();
                    break;

                case "ex5" :

                    for (int i=0;i<GroupName.size();i++){ //ตรวจสอบว่ามีชื่อกลุ่มซ้ำกันไหม
                        get_Groupname = databaseHelper.check_groupname_import(object_ex2.get(1).getGroupname(),"Setting_ex3_hard"); //เช็คว่าชื่อ groupname ซ่ำกับในแอปหรือไม่
                        if (get_Groupname == null){
                            Check_Group = true;
                        }
                    }

                    if (Check_Group){
                        for (int i=0;i<object_ex2.size();i++){
                            String choice = object_ex2.get(i).getID(); //get choiceID from table setting2
                            String groupname = object_ex2.get(i).getGroupname(); //get groupname from table setting2
                            Toast.makeText(context,"นำข้อมูลเข้าเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                        }

                        for (int j=0;j<GroupName.size();j++){ //เพิ่ม score = 0
                            databaseHelper.insert_score_hard(GroupName.get(j));
                        }

                    } else {
                        Toast.makeText(context,"แบบทดสอบชื่อซ้ำกัน กรุณาเปลี่ยนชื่อแบบทดสอบในแอปพลิเคชัน",Toast.LENGTH_SHORT).show();
                    }
                    dataRead.close();
                    break;

                case "word" :
                    for (int i=0;i<object_ex2.size();i++){
                        String word = object_ex2.get(i).getGroupname();
                        databaseHelper.AddWord(word,"Word",null);
                        Toast.makeText(context,"นำข้อมูลเข้าเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case "sentence" :
                    for (int i=0;i<object_ex2.size();i++){
                        String sentence = object_ex2.get(i).getGroupname();
                        databaseHelper.AddWord(sentence,"Sentence",null);
                        Toast.makeText(context,"นำข้อมูลเข้าเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                    }
                    break;

                default : Toast.makeText(context,"ไม่พบแบบทดสอบ กรุณาเลือกไฟล์อื่น",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) { Log.e("TAG",e.toString());
        }

    }


}
