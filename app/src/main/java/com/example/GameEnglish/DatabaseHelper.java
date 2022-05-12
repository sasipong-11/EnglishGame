package com.example.GameEnglish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.GameEnglish.Export_Import.Import_object;
import com.example.GameEnglish.My_Score.Score.HorizontalModel;
import com.example.GameEnglish.flash_card.Character;
import com.example.GameEnglish.flash_card.Object_Choice;
import com.example.GameEnglish.flash_card.st_flash_card_adapter.Item_st_flash_card;
import com.example.GameEnglish.word_builder.word;
import com.example.GameEnglish.recyclerView_Ranking.Ranking_Item;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DatabaseHelper extends SQLiteAssetHelper {

    Context context;

    public DatabaseHelper(Context context) {
        super(context, "project1.3.db", null,3);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + "User");
        // Create tables again
        onCreate(db);
    }

    public User queryUser(String email, String password) {// check login first

        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query("User", new String[]{"UserID",
                        "Username", "Password","Fullname","Age","sex","Permission","Picture"}, "Username" + "=? and " + "Password" + "=?",
                new String[]{email, password}, null, null, null, "1");
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            user = new User(cursor.getString(1), cursor.getString(2),cursor.getString(0)
                    ,cursor.getString(3),cursor.getInt(4),cursor.getString(5)
                    ,cursor.getString(6),cursor.getString(7));
        }
        db.close();
        return user;
    }

    public void insert_user (String user,String password,String name,int age,String sex,String Picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("Username",user);
        Val.put("Password", password);
        Val.put("Fullname",name);
        Val.put("Age",age);
        Val.put("sex",sex);
        Val.put("Permission","User");
        Val.put("Picture",Picture);
        long rows = db.insert("User", null, Val);
        db.close();
    }

    public boolean Check_IDUser(String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean Check;
        Cursor cursor = db.rawQuery("select Username from User where Username = '"+userID+"'",null);
        if (cursor.getCount() > 0){
            Check = false;
        } else {
            Check = true;
        }
        return Check;
    }

    public void update_user (String user,String name,int age,String sex,String Picture,String UserID,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("Username",user);
        Val.put("Password",Password);
        Val.put("Fullname",name);
        Val.put("Age",age);
        Val.put("sex",sex);
        Val.put("Picture",Picture);
        long rows = db.update("User",Val, "UserID=" + UserID, null);
        db.close();
    }

    public ArrayList<String> queryword (String table){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * From "+table, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> GetGroupname(String table,String Order){ //เช็ค group โดยการลบคำซ่ำในช่อง group
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT GroupName , COUNT(*) count FROM " +table+ " GROUP BY [GroupName] Having COUNT(*) > 1 ORDER BY "+Order+ "", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> group_st_easy (String group){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Word.word From Setting_ex3_easy INNER join Word " +
                "ON Setting_ex3_easy.wordID = Word.wordID where GroupName " +" = '"+group+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> group_st_normal (String group,String table){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Sentence.sentence From "+table+" INNER join Sentence " +
                "ON "+table+".sentenceID = Sentence.sentenceID where GroupName " +" = '"+group+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public void insert_group (String ID ,String GroupName,String table,String tableFK){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> words = new ArrayList<>();
        ContentValues Val = new ContentValues();
        Val.put(tableFK,ID);
        Val.put("GroupName",GroupName);
        db.insert(table, null, Val);
        db.close();
    }

    public void insert_score_ex2(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> User = new ArrayList<>();
        ArrayList<String> wordID = new ArrayList<>();

        Cursor cursor = db.rawQuery("select UserID from User",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User.add(cursor.getString(0));
            cursor.moveToNext();
        }

        Cursor cursor_word = db.rawQuery("SELECT st_ex2_id\n" +
                "From Setting_ex2 \n" +
                "where GroupName like '"+GroupName+"'",null);
        cursor_word.moveToFirst();
        while (!cursor_word.isAfterLast()){
            wordID.add(cursor_word.getString(0));
            cursor_word.moveToNext();
        }

        int yes = 0;
        for (int i=0;i<User.size();i++) {
            ContentValues val = new ContentValues();
            for (int j=0;j<wordID.size();j++) {
                yes++;
                val.put("UserID", User.get(i));
                val.put("st_ex2_id",wordID.get(j));
                val.put("Score",0);
                if(yes % 2 == 0) {
                    val.put("this_Score","no"); //odd
                } else {
                    val.put("this_Score","yes"); //odd
                }
                db.insert("Score_ex2",null,val);
            }
        }
        db.close();
    }

    public void insert_score_easy(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> User = new ArrayList<>();
        ArrayList<String> wordID = new ArrayList<>();

        Cursor cursor = db.rawQuery("select UserID from User",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User.add(cursor.getString(0));
            cursor.moveToNext();
        }

        Cursor cursor_word = db.rawQuery("SELECT st_ex3_easy_id\n" +
                "From Setting_ex3_easy \n" +
                "where GroupName like '"+GroupName+"'",null);
        cursor_word.moveToFirst();
        while (!cursor_word.isAfterLast()){
            wordID.add(cursor_word.getString(0));
            cursor_word.moveToNext();
        }

        for (int i=0;i<User.size();i++) {
            ContentValues val = new ContentValues();
            for (int j=0;j<wordID.size();j++) {
                val.put("UserID", User.get(i));
                val.put("st_ex3_easy_id",wordID.get(j));
                val.put("Score",0);
                db.insert("Score_ex3_easy",null,val);
            }
        }
        db.close();
    }

    public void insert_score_normal(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> User = new ArrayList<>();
        ArrayList<String> wordID = new ArrayList<>();

        Cursor cursor = db.rawQuery("select UserID from User",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User.add(cursor.getString(0));
            cursor.moveToNext();
        }

        Cursor cursor_word = db.rawQuery("SELECT st_ex3_normal_id\n" +
                "From Setting_ex3_normal \n" +
                "where GroupName like '"+GroupName+"'",null);
        cursor_word.moveToFirst();
        while (!cursor_word.isAfterLast()){
            wordID.add(cursor_word.getString(0));
            cursor_word.moveToNext();
        }

        for (int i=0;i<User.size();i++) {
            ContentValues val = new ContentValues();
            for (int j=0;j<wordID.size();j++) {
                val.put("UserID", User.get(i));
                val.put("st_ex3_normal_id",wordID.get(j));
                val.put("Score",0);
                db.insert("Score_ex3_normal",null,val);
            }
        }
        db.close();
    }

    public void insert_score_hard(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> User = new ArrayList<>();
        ArrayList<String> wordID = new ArrayList<>();

        Cursor cursor = db.rawQuery("select UserID from User",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User.add(cursor.getString(0));
            cursor.moveToNext();
        }

        Cursor cursor_word = db.rawQuery("SELECT st_ex3_hard_id\n" +
                "From Setting_ex3_hard \n" +
                "where GroupName like '"+GroupName+"'",null);
        cursor_word.moveToFirst();
        while (!cursor_word.isAfterLast()){
            wordID.add(cursor_word.getString(0));
            cursor_word.moveToNext();
        }

        for (int i=0;i<User.size();i++) {
            ContentValues val = new ContentValues();
            for (int j=0;j<wordID.size();j++) {
                val.put("UserID", User.get(i));
                val.put("st_ex3_hard_id",wordID.get(j));
                val.put("Score",0);
                db.insert("Score_ex3_hard",null,val);
            }
        }
        db.close();
    }

    public void insert_allScore(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String UserID;
        Cursor cursor_user = db.rawQuery("select UserID from User where Fullname = '"+name+"'",null);
        cursor_user.moveToFirst();
        UserID = cursor_user.getString(0);

        ArrayList<String> scoreID = new ArrayList<>();
        Cursor cursor_ex2 = db.rawQuery("select st_ex2_id from Setting_ex2",null);
        cursor_ex2.moveToFirst();
        while (!cursor_ex2.isAfterLast()){
            scoreID.add(cursor_ex2.getString(0));
            cursor_ex2.moveToNext();
        }

        ArrayList<String> ex3_easy = new ArrayList<>();
        Cursor cursor_ex3_easy = db.rawQuery("select st_ex3_easy_id from Setting_ex3_easy",null);
        cursor_ex3_easy.moveToFirst();
        while (!cursor_ex3_easy.isAfterLast()){
            ex3_easy.add(cursor_ex3_easy.getString(0));
            cursor_ex3_easy.moveToNext();
        }

        ArrayList<String> ex3_normal = new ArrayList<>();
        Cursor cursor_ex3_normal = db.rawQuery("select st_ex3_normal_id from Setting_ex3_normal",null);
        cursor_ex3_normal.moveToFirst();
        while (!cursor_ex3_normal.isAfterLast()){
            ex3_normal.add(cursor_ex3_normal.getString(0));
            cursor_ex3_normal.moveToNext();
        }

        ArrayList<String> ex3_hard = new ArrayList<>();
        Cursor cursor_ex3_hard = db.rawQuery("select st_ex3_hard_id from Setting_ex3_hard",null);
        cursor_ex3_hard.moveToFirst();
        while (!cursor_ex3_hard.isAfterLast()){
            ex3_hard.add(cursor_ex3_hard.getString(0));
            cursor_ex3_hard.moveToNext();
        }

        int yes = 0;
        ContentValues val = new ContentValues();
        for (int j=0;j<scoreID.size();j++) {
            yes++;
            val.put("UserID",UserID);
            val.put("st_ex2_id",scoreID.get(j));
            val.put("Score",0);
            if(yes % 2 == 0) {
                val.put("this_Score","no"); //odd
            } else {
                val.put("this_Score","yes"); //odd
            }
            db.insert("Score_ex2",null,val);
        }

        ContentValues val_ex3_easy = new ContentValues();
        for (int j=0;j<ex3_easy.size();j++) {
            val_ex3_easy.put("UserID", UserID);
            val_ex3_easy.put("st_ex3_easy_id",ex3_easy.get(j));
            val_ex3_easy.put("Score",0);
            db.insert("Score_ex3_easy",null,val_ex3_easy);
        }

        ContentValues val_ex3_normal = new ContentValues();
        for (int j=0;j<ex3_normal.size();j++) {
            val_ex3_normal.put("UserID", UserID);
            val_ex3_normal.put("st_ex3_normal_id",ex3_normal.get(j));
            val_ex3_normal.put("Score",0);
            db.insert("Score_ex3_normal",null,val_ex3_normal);
        }

        ContentValues val_ex3_hard = new ContentValues();
        for (int j=0;j<ex3_hard.size();j++) {
            val_ex3_hard.put("UserID", UserID);
            val_ex3_hard.put("st_ex3_hard_id",ex3_hard.get(j));
            val_ex3_hard.put("Score",0);
            db.insert("Score_ex3_hard",null,val_ex3_hard);
        }
        db.close();
    }

    public ArrayList<word> query_id_word(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<word> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * From "+table, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            word word = new word(cursor.getString(0),cursor.getString(1));
            words.add(word);
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> Check_Group_db(String table){ //เช็ค group โดยการลบคำซ่ำในช่อง group
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT \"group\" , COUNT(*) count\n" +
                "\n" +
                "FROM "+table+ "\n" +
                "\n" +
                "GROUP BY [group] \n" +
                "\n" +
                "Having COUNT(*) > 1 \n", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }
    public ArrayList<String> ex3_easy_game_st(String GroupName,String UserID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Word.word From Setting_ex3_easy INNER join Word " +
                "ON Setting_ex3_easy.wordID = Word.wordID " +
                "where Setting_ex3_easy.GroupName like '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> ex3_normal_game_st(String GroupName,String UserID){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Sentence.sentence " +
                "From Setting_ex3_normal INNER join Sentence ON Setting_ex3_normal.sentenceID = Sentence.sentenceID " +
                "where Setting_ex3_normal.GroupName like '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> ex3_hard_game_st(String GroupName,String UserID){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Sentence.sentence From Setting_ex3_hard " +
                "INNER join Sentence ON Setting_ex3_hard.sentenceID = Sentence.sentenceID " +
                "where Setting_ex3_hard.GroupName like '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            words.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return words;
    }

    public ArrayList<String> get_Groupname_ex2_st(String Groupname,String UserID){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> get_groupname = new ArrayList<>();
        Cursor cursor = db.rawQuery("select Character_ex2.Char,Setting_ex2.st_ex2_id from Setting_ex2\n" +
                "inner join Character_ex2 on Setting_ex2.choiceID = Character_ex2.choiceID\n" +
                "where Setting_ex2.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_groupname.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return get_groupname;
    }

    public ArrayList<Object_Choice> test(String Groupname, String UserID){
        Object_Choice object_choice = null;
        ArrayList<Object_Choice> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Char_name = new ArrayList<>();
        ArrayList<String> Choice = new ArrayList<>();

        Cursor cursor = db.rawQuery("select Setting_ex2.st_ex2_id,Character_ex2.Char from Setting_ex2\n" +
                "inner join Character_ex2 on Setting_ex2.choiceID = Character_ex2.choiceID\n" +
                "where Setting_ex2.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Choice.add(cursor.getString(0));
            Char_name.add(cursor.getString(1));
            cursor.moveToNext();
        }

        /**
         * ลบคำซ่ำใน array charset
         */
        Set<String> set = new HashSet<>(Char_name);
        Char_name.clear();
        Char_name.addAll(set);

        int index1 = 0;
        int index2 = 1;
        int indexChar = 0;

        for (int i=0;i<Choice.size();i=i+2) {
            object_choice = new Object_Choice(Choice.get(index1), Choice.get(index2),Char_name.get(indexChar));
            arrayList.add(object_choice);

            index1 = index1+2;
            index2 = index2+2;
            indexChar++;
        }

        db.close();
        return arrayList;
    }

    public ArrayList<String> getAll_User(String select){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> User = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " +select+ " From User ORDER BY UserID",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return User;
    }

    public ArrayList<String> getAll_User_Picture(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Picture = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Picture From User ORDER BY UserID",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String path = cursor.getString(0);
            if (path != null) {
//                Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
                Picture.add(path);
            }
            cursor.moveToNext();
        }
        db.close();
        return Picture;
    }

    public User ModifileUser(String UserID) {// check login first

        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query("User", new String[]{"UserID",
                "Username", "Password","Fullname","Age","sex","Permission","Picture"}, "UserID = " + UserID, null, null, null, "1");
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            user = new User(cursor.getString(1), cursor.getString(2),cursor.getString(0)
                    ,cursor.getString(3),cursor.getInt(4),cursor.getString(5)
                    ,cursor.getString(6),cursor.getString(7));
        }
        db.close();
        return user;
    }

    public void delete_user (String UserID){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("User","UserID = " + UserID,null);
        db.close();
    }

    public Score_word_builder_word Score_ex3_easy(String Groupname, String UserID, String Fullname){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Word = new ArrayList<>();
        ArrayList<Integer> Score = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT Word.word,Score_ex3_easy.Score From Score_ex3_easy \n" +
                "LEFT JOIN Setting_ex3_easy on (Score_ex3_easy.st_ex3_easy_id = Setting_ex3_easy.st_ex3_easy_id)\n" +
                "LEFT JOIN Word on (Setting_ex3_easy.wordID = Word.wordID)\n" +
                "LEFT JOIN User on (Score_ex3_easy.UserID = User.UserID)\n" +
                "where Setting_ex3_easy.GroupName = '"+Groupname+"' and  Score_ex3_easy.UserID = '"+UserID +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Word.add(cursor.getString(0));
            Score.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        Score_word_builder_word Score_Word = new Score_word_builder_word(Fullname,Word,Score,UserID);
        db.close();
        return Score_Word;
    }

    public Score_word_builder_word Score_ex3_normal (String Groupname, String UserID, String Fullname){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Word = new ArrayList<>();
        ArrayList<Integer> Score = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT Sentence.sentence,Score_ex3_normal.Score From Score_ex3_normal \n" +
                "LEFT JOIN Setting_ex3_normal on (Score_ex3_normal.st_ex3_normal_id = Setting_ex3_normal.st_ex3_normal_id)\n" +
                "LEFT JOIN Sentence on (Setting_ex3_normal.sentenceID = Sentence.sentenceID)\n" +
                "LEFT JOIN User on (Score_ex3_normal.UserID = User.UserID)\n" +
                "where Setting_ex3_normal.GroupName = '"+Groupname+"' and  Score_ex3_normal.UserID = '"+UserID +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Word.add(cursor.getString(0));
            Score.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        Score_word_builder_word Score_Word = new Score_word_builder_word(Fullname,Word,Score,UserID);
        db.close();
        return Score_Word;
    }

    public Score_word_builder_word Score_ex3_hard (String Groupname, String UserID, String Fullname){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Word = new ArrayList<>();
        ArrayList<Integer> Score = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT Sentence.sentence,Score_ex3_hard.Score From Score_ex3_hard \n" +
                "LEFT JOIN Setting_ex3_hard on (Score_ex3_hard.st_ex3_hard_id = Setting_ex3_hard.st_ex3_hard_id)\n" +
                "LEFT JOIN Sentence on (Setting_ex3_hard.sentenceID = Sentence.sentenceID)\n" +
                "LEFT JOIN User on (Score_ex3_hard.UserID = User.UserID)\n" +
                "where Setting_ex3_hard.GroupName = '"+Groupname+"' and  Score_ex3_hard.UserID = '"+UserID +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Word.add(cursor.getString(0));
            Score.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        Score_word_builder_word Score_Word = new Score_word_builder_word(Fullname,Word,Score,UserID);
        db.close();
        return Score_Word;
    }

    public Score_word_builder_word Score_ex2 (String Groupname, String UserID, String Fullname){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Word = new ArrayList<>();
        ArrayList<Integer> Score = new ArrayList<>();

        Cursor cursor = db.rawQuery("select Character_ex2.Char,Score_ex2.Score From Score_ex2 \n" +
                "LEFT JOIN Setting_ex2 on (Score_ex2.st_ex2_id = Setting_ex2.st_ex2_id)\n" +
                "LEFT JOIN Character_ex2 on (Setting_ex2.choiceID = Character_ex2.choiceID)\n" +
                "LEFT JOIN User on (Score_ex2.UserID = User.UserID)\n" +
                "where Setting_ex2.GroupName = '"+Groupname+"' and  Score_ex2.UserID = '"+UserID+"' and Score_ex2.this_Score = 'yes'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Word.add(cursor.getString(0));
            Score.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        Score_word_builder_word Score_Word = new Score_word_builder_word(Fullname,Word,Score,UserID);
        db.close();
        return Score_Word;
    }

    public void update_score_ex2(String UserID, int Score, String stID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        //Val.put("UserID",UserID);
        Val.put("Score",Score);
        long rows = db.update("Score_ex2",Val, "st_ex2_id='" + stID + "' and UserID = '"+ UserID + "'", null);
        db.close();
    }

    public String Find_stID_Char(String Char,String Groupname){
        SQLiteDatabase db = this.getReadableDatabase();
        String stID = null;
        Cursor cursor = db.rawQuery("SELECT\n" +
                "    Setting_ex2.st_ex2_id\n" +
                "FROM\n" +
                "    Setting_ex2\n" +
                "INNER JOIN \n" +
                "\tCharacter_ex2 ON Setting_ex2.choiceID=Character_ex2.choiceID\n" +
                "where Character_ex2.char = '" +Char+"' and Setting_ex2.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        stID = cursor.getString(0);

        db.close();
        return stID;
    }

    public String Find_stID_word(String word,String Groupname){
        SQLiteDatabase db = this.getReadableDatabase();
        String stID = null;
        Cursor cursor = db.rawQuery("SELECT\n" +
                "    Setting_ex3_easy.st_ex3_easy_id\n" +
                "FROM\n" +
                "    Setting_ex3_easy\n" +
                "INNER JOIN \n" +
                "\tWord ON Setting_ex3_easy.wordID=Word.wordID\n" +
                "where Word.word = '" +word+"' and Setting_ex3_easy.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        stID = cursor.getString(0);

        db.close();
        return stID;
    }

    public String Find_stID_sentence(String word,String Groupname,String Table,String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String stID = null;
        Cursor cursor = db.rawQuery("SELECT\n" +
                "    "+Table+"."+ID+"\n" +
                "FROM\n" +
                "    "+Table+"\n" +
                "INNER JOIN \n" +
                "\tSentence ON "+Table+".sentenceID=Sentence.sentenceID\n" +
                "where Sentence.sentence = '" +word+"' and "+Table+".GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        stID = cursor.getString(0);

        db.close();
        return stID;
    }



    public void update_score_ex3_easy(String UserID, int Score, String stID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        //Val.put("UserID",UserID);
        Val.put("Score",Score);
        long rows = db.update("Score_ex3_easy",Val, "st_ex3_easy_id=" + stID + " and UserID = "+ UserID, null);
        db.close();
    }

    public void update_score_ex3_normal (String UserID,int Score,String stID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        //Val.put("UserID",UserID);
        Val.put("Score",Score);
        long rows = db.update("Score_ex3_normal",Val, "st_ex3_normal_id=" + stID + " and UserID = "+ UserID, null);
        db.close();
    }

    public void update_score_ex3_hard (String UserID,int Score,String stID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        //Val.put("UserID",UserID);
        Val.put("Score",Score);
        long rows = db.update("Score_ex3_hard",Val, "st_ex3_hard_id=" + stID + " and UserID = "+ UserID, null);
        db.close();
    }

    public boolean delete_word (String word){
        boolean delete;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select Setting_ex3_easy.wordID from Word INNER join Setting_ex3_easy \n" +
                "on (Setting_ex3_easy.wordID = Word.wordID)\n" +
                "where Word.word = '"+word+"'",null);
        if (cursor.getCount() == 0 || cursor == null) {
            db.delete("Word","word = '" + word +"'",null);
            delete = true;
        } else {
            Toast.makeText(context,"พบคำศัพท์ในแบบทดสอบกรุณาลบแบบทดสอบก่อน",Toast.LENGTH_SHORT).show();
            delete = false;
        }
        db.close();
        return delete;
    }

    public boolean delete_sentence (String sentence){
        Boolean delete;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor normal = db.rawQuery("select Setting_ex3_normal.sentenceID from Sentence INNER join Setting_ex3_normal \n" +
                "on (Setting_ex3_normal.sentenceID = Sentence.sentenceID)\n" +
                "where Sentence.sentence = '"+sentence+"'",null);
        Cursor hard = db.rawQuery("select Setting_ex3_hard.sentenceID from Sentence INNER join Setting_ex3_hard \n" +
                "on (Setting_ex3_hard.sentenceID = Sentence.sentenceID)\n" +
                "where Sentence.sentence = '"+sentence+"'",null);
        if (normal.getCount() == 0 || normal == null || hard.getCount() == 0 || hard == null) {
            db.delete("Sentence", "sentence = '" + sentence +"'",null);
            delete = true;
        } else {
            Toast.makeText(context,"พบคำประโยคในแบบทดสอบกรุณาลบแบบทดสอบก่อน",Toast.LENGTH_SHORT).show();
            delete = false;
        }
        db.close();
        return delete;
    }

    public void update_word (String word,String mod,String path_image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("word",mod);
        Val.put("path_image",path_image);
        Val.put("Image","null");
        try {
            long rows = db.update("Word",Val, "word = '" + word +"'", null);
        } catch (Exception e){
            Toast.makeText(context,"มีคำในฐานข้อมูลแล้ว",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void update_sentence (String word,String mod){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("sentence",mod);
        try {
            long rows = db.update("Sentence",Val,  "sentence = '" + word +"'", null);
        } catch (Exception e){
            Toast.makeText(context,"มีคำในฐานข้อมูลแล้ว",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public boolean AddWord (String word,String table,String path_image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Boolean check_word;
        switch (table) {
            case "Word" :
                Val.put("Word",word);
                Val.put("path_image",path_image);
                break;
            case "Sentence" : Val.put("Sentence",word); break;
        }
//        Val.put("word",word);
        try {
            db.insertOrThrow(table, null, Val);
            check_word = true;
        } catch (Exception e) {
            Toast.makeText(context,"มีข้อมูลในฐานข้อมูลแล้ว",Toast.LENGTH_SHORT).show();
            check_word = false;
        }
        db.close();
        return check_word;
    }

    public ArrayList<Ranking_Item> rank_ex2 (String group){
        Ranking_Item rankingItem = null;
        ArrayList<Ranking_Item> ranking_item = new ArrayList<>();
        int rank=1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select User.Fullname,avg(Score) from Score_ex2 \n" +
                "LEFT JOIN Setting_ex2 on (Score_ex2.st_ex2_id = Setting_ex2.st_ex2_id)\n" +
                "LEFT JOIN User on (Score_ex2.UserID = User.UserID)\n" +
                "where Setting_ex2.GroupName = '" + group +"' and Score_ex2.this_Score = 'yes'\n" +
                "Group by Score_ex2.UserID ORDER by sum(Score) desc\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            String a = cursor.getString(0);
            String[] arrB = a.split(" ");
            ArrayList<String> Firstname = new ArrayList(Arrays.asList(arrB));

            rankingItem = new Ranking_Item(String.valueOf(rank),Firstname.get(0),cursor.getString(1));
            ranking_item.add(rankingItem);
            rank++;
            cursor.moveToNext();
        }
        db.close();
        return ranking_item;
    }

    public ArrayList<Ranking_Item> rank_ex3_easy (String group){
        Ranking_Item rankingItem = null;
        ArrayList<Ranking_Item> ranking_item = new ArrayList<>();
        int rank=1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select User.Fullname,avg(Score) from Score_ex3_easy \n" +
                "LEFT JOIN Setting_ex3_easy on (Score_ex3_easy.st_ex3_easy_id = Setting_ex3_easy.st_ex3_easy_id)\n" +
                "LEFT JOIN User on (Score_ex3_easy.UserID = User.UserID)\n" +
                "where Setting_ex3_easy.GroupName = '" + group +"'\n" +
                "Group by Score_ex3_easy.UserID ORDER by sum(Score) desc\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            String a = cursor.getString(0);
            String[] arrB = a.split(" ");
            ArrayList<String> Firstname = new ArrayList(Arrays.asList(arrB));

            rankingItem = new Ranking_Item(String.valueOf(rank),Firstname.get(0),cursor.getString(1));
            ranking_item.add(rankingItem);
            rank++;
            cursor.moveToNext();
        }
        db.close();
        return ranking_item;
    }

    public ArrayList<Ranking_Item> rank_ex3_normal (String group){
        Ranking_Item rankingItem = null;
        ArrayList<Ranking_Item> ranking_item = new ArrayList<>();
        int rank=1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select User.Fullname,avg(Score) from Score_ex3_normal \n" +
                "LEFT JOIN Setting_ex3_normal on (Score_ex3_normal.st_ex3_normal_id = Setting_ex3_normal.st_ex3_normal_id)\n" +
                "LEFT JOIN User on (Score_ex3_normal.UserID = User.UserID)\n" +
                "where Setting_ex3_normal.GroupName = '" + group +"'\n" +
                "Group by Score_ex3_normal.UserID ORDER by sum(Score) desc\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            String a = cursor.getString(0);
            String[] arrB = a.split(" ");
            ArrayList<String> Firstname = new ArrayList(Arrays.asList(arrB));

            rankingItem = new Ranking_Item(String.valueOf(rank),Firstname.get(0),cursor.getString(1));
            ranking_item.add(rankingItem);
            rank++;
            cursor.moveToNext();
        }
        db.close();
        return ranking_item;
    }

    public ArrayList<Ranking_Item> rank_ex3_hard (String group){
        Ranking_Item rankingItem = null;
        ArrayList<Ranking_Item> ranking_item = new ArrayList<>();
        int rank=1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select User.Fullname,avg(Score) from Score_ex3_hard \n" +
                "LEFT JOIN Setting_ex3_hard on (Score_ex3_hard.st_ex3_hard_id = Setting_ex3_hard.st_ex3_hard_id)\n" +
                "LEFT JOIN User on (Score_ex3_hard.UserID = User.UserID)\n" +
                "where Setting_ex3_hard.GroupName = '" + group +"'\n" +
                "Group by Score_ex3_hard.UserID ORDER by sum(Score) desc\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            String a = cursor.getString(0);
            String[] arrB = a.split(" ");
            ArrayList<String> Firstname = new ArrayList(Arrays.asList(arrB));

            rankingItem = new Ranking_Item(String.valueOf(rank),Firstname.get(0),cursor.getString(1));
            ranking_item.add(rankingItem);
            rank++;
            cursor.moveToNext();
        }
        db.close();
        return ranking_item;
    }

    public ArrayList<String> SearchGroupName_ex2(String UserID){
        ArrayList Groupname = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Setting_ex2.GroupName , COUNT(*) count FROM Score_ex2 \n" +
                "LEFT JOIN Setting_ex2 on (Score_ex2.st_ex2_id = Setting_ex2.st_ex2_id)\n" +
                "where Score_ex2.UserID = '"+UserID+"' GROUP BY [GroupName] Having COUNT(*) > 1\n" +
                "ORDER by Setting_ex2.st_ex2_id\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Groupname.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return Groupname;
    }

    public ArrayList<String> SearchGroupName_easy(String UserID){
        ArrayList Groupname = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Setting_ex3_easy.GroupName , COUNT(*) count FROM Score_ex3_easy \n" +
                "LEFT JOIN Setting_ex3_easy on (Score_ex3_easy.st_ex3_easy_id = Setting_ex3_easy.st_ex3_easy_id)\n" +
                "where Score_ex3_easy.UserID = '"+UserID+"' GROUP BY [GroupName] Having COUNT(*) > 1\n" +
                "ORDER by Setting_ex3_easy.st_ex3_easy_id\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Groupname.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return Groupname;
    }

    public ArrayList<String> SearchGroupName_normal(String UserID){
        ArrayList Groupname = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Setting_ex3_normal.GroupName , COUNT(*) count FROM Score_ex3_normal \n" +
                "LEFT JOIN Setting_ex3_normal on (Score_ex3_normal.st_ex3_normal_id = Setting_ex3_normal.st_ex3_normal_id)\n" +
                "where Score_ex3_normal.UserID = '"+UserID+"' GROUP BY [GroupName] Having COUNT(*) > 1\n" +
                "ORDER by Setting_ex3_normal.st_ex3_normal_id\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Groupname.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return Groupname;
    }

    public ArrayList<String> SearchGroupName_hard(String UserID){
        ArrayList Groupname = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Setting_ex3_hard.GroupName , COUNT(*) count FROM Score_ex3_hard \n" +
                "LEFT JOIN Setting_ex3_hard on (Score_ex3_hard.st_ex3_hard_id = Setting_ex3_hard.st_ex3_hard_id)\n" +
                "where Score_ex3_hard.UserID = '"+UserID+"' GROUP BY [GroupName] Having COUNT(*) > 1\n" +
                "ORDER by Setting_ex3_hard.st_ex3_hard_id\n",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Groupname.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return Groupname;
    }

    public ArrayList<HorizontalModel> item_word_Ranking_ex2(String userID, String Groupname){
        ArrayList<HorizontalModel> horizontalModel_return = new ArrayList<>();
        HorizontalModel horizontalModel = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Character_ex2.Char,Score_ex2.Score\n" +
                "From Score_ex2\n" +
                "LEFT JOIN Setting_ex2 on (Score_ex2.st_ex2_id = Setting_ex2.st_ex2_id)\n" +
                "LEFT JOIN Character_ex2 on (Setting_ex2.choiceID = Character_ex2.choiceID)\n" +
                "where Score_ex2.UserID = '"+userID+"' and Setting_ex2.GroupName = '"+Groupname+"' and Score_ex2.this_Score = 'yes'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            horizontalModel = new HorizontalModel(cursor.getString(0),cursor.getString(1));
            horizontalModel_return.add(horizontalModel);
            cursor.moveToNext();
        }
        db.close();
        return horizontalModel_return;
    }

    public ArrayList<HorizontalModel> item_word_Ranking_easy(String userID, String Groupname){
        ArrayList<HorizontalModel> horizontalModel_return = new ArrayList<>();
        HorizontalModel horizontalModel = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Word.word,Score_ex3_easy.Score \n" +
                "From Score_ex3_easy\n" +
                "LEFT JOIN Setting_ex3_easy on (Score_ex3_easy.st_ex3_easy_id = Setting_ex3_easy.st_ex3_easy_id)\n" +
                "LEFT JOIN Word on (Setting_ex3_easy.wordID = Word.wordID)\n" +
                "where Score_ex3_easy.UserID = "+userID+" and Setting_ex3_easy.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            horizontalModel = new HorizontalModel(cursor.getString(0),cursor.getString(1));
            horizontalModel_return.add(horizontalModel);
            cursor.moveToNext();
        }

        db.close();
        return horizontalModel_return;
    }

    public ArrayList<HorizontalModel> item_word_Ranking_normal(String userID, String Groupname){
        ArrayList<HorizontalModel> horizontalModel_return = new ArrayList<>();
        HorizontalModel horizontalModel = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Sentence.sentence,Score_ex3_normal.Score \n" +
                "From Score_ex3_normal\n" +
                "LEFT JOIN Setting_ex3_normal on (Score_ex3_normal.st_ex3_normal_id = Setting_ex3_normal.st_ex3_normal_id)\n" +
                "LEFT JOIN Sentence on (Setting_ex3_normal.sentenceID = Sentence.sentenceID)\n" +
                "where Score_ex3_normal.UserID = "+userID+" and Setting_ex3_normal.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            horizontalModel = new HorizontalModel(cursor.getString(0),cursor.getString(1));
            horizontalModel_return.add(horizontalModel);
            cursor.moveToNext();
        }

        db.close();
        return horizontalModel_return;
    }

    public ArrayList<HorizontalModel> item_word_Ranking_hard(String userID, String Groupname){
        ArrayList<HorizontalModel> horizontalModel_return = new ArrayList<>();
        HorizontalModel horizontalModel = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Sentence.sentence,Score_ex3_hard.Score \n" +
                "From Score_ex3_hard\n" +
                "LEFT JOIN Setting_ex3_hard on (Score_ex3_hard.st_ex3_hard_id = Setting_ex3_hard.st_ex3_hard_id)\n" +
                "LEFT JOIN Sentence on (Setting_ex3_hard.sentenceID = Sentence.sentenceID)\n" +
                "where Score_ex3_hard.UserID = "+userID+" and Setting_ex3_hard.GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            horizontalModel = new HorizontalModel(cursor.getString(0),cursor.getString(1));
            horizontalModel_return.add(horizontalModel);
            cursor.moveToNext();
        }

        db.close();
        return horizontalModel_return;
    }

    public Character character(String Character){
        Character Char = null;
        String Image = null;
        String Correct = null;
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //ดึงข้อมูลรูปภาพกับคำถามที่ผิด
        Cursor cursor = db.rawQuery("select Image,Image_char,Anser FROM Character_ex2\n" +
                "where Char = '"+Character+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Image = cursor.getString(0);
            arrayList.add(cursor.getString(1));
            Correct = cursor.getString(2);
            cursor.moveToNext();
        }

        db.close();
        Char = new Character(Image,arrayList,Correct);
        return Char;
    }

    public Character get_choice_ex2_st(ArrayList<String> stID){
        Character character = null;
        String Image = null;
        String Correct = null;
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();

        for (int i=0;i<stID.size();i++) {
            Cursor cursor = db.rawQuery("select Character_ex2.Image,Character_ex2.Image_char,Character_ex2.Anser FROM Setting_ex2 INNER JOIN Character_ex2 \n" +
                    "on Setting_ex2.choiceID = Character_ex2.choiceID where Setting_ex2.st_ex2_id = '" + stID.get(i) + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Image = cursor.getString(0);
                arrayList.add(cursor.getString(1));
                Correct = cursor.getString(2);
                cursor.moveToNext();
            }
        }
        db.close();
        character = new Character(Image,arrayList,Correct);
        return character;
    }

    public Item_st_flash_card item_st_ex2(String Char){
        ArrayList<String> get_char = new ArrayList<>();
        String Character = null;
        Item_st_flash_card item_st_flashcard;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT char,Image_char from Character_ex2 where char = '"+Char+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_char.add(cursor.getString(1));
            Character = cursor.getString(0);
            cursor.moveToNext();
        }
        db.close();
        item_st_flashcard = new Item_st_flash_card(Character,get_char.get(0),get_char.get(1),get_char.get(2),get_char.get(3));
        return item_st_flashcard;
    }

    public void insert_char(String image_name,String Groupname){
        SQLiteDatabase db = this.getWritableDatabase();
        String choiceID = null;

        Cursor cursor = db.rawQuery("SELECT choiceID from Character_ex2 where Image_char = '"+image_name+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            choiceID = cursor.getString(0);
            cursor.moveToNext();
        }
        ContentValues Val = new ContentValues();
        Val.put("choiceID",choiceID);
        Val.put("GroupName", Groupname);
        long rows = db.insert("Setting_ex2", null, Val);
        db.close();
    }

    public ArrayList<Import_object> export_ex2(String Groupname){
        ArrayList<Import_object> return_object = new ArrayList<>();
        Import_object object = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select choiceID,GroupName from Setting_ex2 where GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            object = new Import_object(cursor.getString(0),cursor.getString(1));
            return_object.add(object);
            cursor.moveToNext();
        }
        db.close();
        return return_object;
    }

    public ArrayList<Import_object> export_ex3(String Groupname){
        ArrayList<Import_object> return_object = new ArrayList<>();
        Import_object object = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select wordID,GroupName from Setting_ex3_easy where GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            object = new Import_object(cursor.getString(0),cursor.getString(1));
            return_object.add(object);
            cursor.moveToNext();
        }
        db.close();
        return return_object;
    }

    public ArrayList<Import_object> export_ex4(String Groupname){
        ArrayList<Import_object> return_object = new ArrayList<>();
        Import_object object = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select SentenceID,GroupName from Setting_ex3_normal where GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            object = new Import_object(cursor.getString(0),cursor.getString(1));
            return_object.add(object);
            cursor.moveToNext();
        }
        db.close();
        return return_object;
    }

    public ArrayList<Import_object> export_ex5(String Groupname){
        ArrayList<Import_object> return_object = new ArrayList<>();
        Import_object object = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select SentenceID,GroupName from Setting_ex3_hard where GroupName = '"+Groupname+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            object = new Import_object(cursor.getString(0),cursor.getString(1));
            return_object.add(object);
            cursor.moveToNext();
        }
        db.close();
        return return_object;
    }

    public void Import_ex2(String choiceID,String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues(); //insert data
        Val.put("choiceID", choiceID);
        Val.put("GroupName", GroupName);
        long rows = db.insert("Setting_ex2", null, Val);
        db.close();
    }

    public void Import_ex3 (String wordID,String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("wordID",wordID);
        values.put("GroupName",GroupName);
        long row = db.insert("Setting_ex3_easy",null,values);
        db.close();
    }

    public void Import_ex4 (String sentenceID,String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sentenceID",sentenceID);
        values.put("GroupName",GroupName);
        long row = db.insert("Setting_ex3_normal",null,values);
        db.close();
    }

    public void Import_ex5 (String sentenceID,String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sentenceID",sentenceID);
        values.put("GroupName",GroupName);
        long row = db.insert("Setting_ex3_hard",null,values);
        db.close();
    }

    public String check_groupname_import(String GroupName,String table){
        SQLiteDatabase db = this.getReadableDatabase();
        String check_group = null;
        Cursor cursor = db.rawQuery("select GroupName from "+table+" where GroupName = '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            check_group = cursor.getString(0);
            cursor.moveToNext();
        }
        db.close();
        return check_group;
    }

    public void delete_st_ex2(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> delete_score = new ArrayList<>();
        Cursor cursor = db.rawQuery("select st_ex2_id from Setting_ex2 where GroupName = '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            delete_score.add(cursor.getString(0));
            cursor.moveToNext();
        }
        for (int i=0;i<delete_score.size();i++){
            db.delete("Score_ex2","st_ex2_id = "+delete_score.get(i),null);
        }

        db.delete("Setting_ex2","GroupName = '" + GroupName +"'",null);
        db.close();
    }

    public void delete_st_ex3(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> delete_score = new ArrayList<>();
        Cursor cursor = db.rawQuery("select st_ex3_easy_id from Setting_ex3_easy where GroupName = '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            delete_score.add(cursor.getString(0));
            cursor.moveToNext();
        }
        for (int i=0;i<delete_score.size();i++){
            db.delete("Score_ex3_easy","st_ex3_easy_id = "+delete_score.get(i),null);
        }

        db.delete("Setting_ex3_easy","GroupName = '" + GroupName +"'",null);
        db.close();
    }

    public void delete_st_ex4(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> delete_score = new ArrayList<>();
        Cursor cursor = db.rawQuery("select st_ex3_normal_id from Setting_ex3_normal where GroupName = '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            delete_score.add(cursor.getString(0));
            cursor.moveToNext();
        }
        for (int i=0;i<delete_score.size();i++){
            db.delete("Score_ex3_normal","st_ex3_normal_id = "+delete_score.get(i),null);
        }

        db.delete("Setting_ex3_normal","GroupName = '" + GroupName +"'",null);
        db.close();
    }

    public void delete_st_ex5(String GroupName){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> delete_score = new ArrayList<>();
        Cursor cursor = db.rawQuery("select st_ex3_hard_id from Setting_ex3_hard where GroupName = '"+GroupName+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            delete_score.add(cursor.getString(0));
            cursor.moveToNext();
        }
        for (int i=0;i<delete_score.size();i++){
            db.delete("Score_ex3_hard","st_ex3_hard_id = "+delete_score.get(i),null);
        }

        db.delete("Setting_ex3_hard","GroupName = '" + GroupName +"'",null);
        db.close();
    }


    public void update_ex3_easy_st (String wordID,String newGroup,String oldGroup,String oldID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("wordID",wordID);
        values.put("GroupName",newGroup);
        db.update("Setting_ex3_easy",values,"wordID = " +  oldID +" and GroupName = '" + oldGroup +"'",null);
        db.close();
    }

    public void update_ex3_normal_st (String sentenceID,String newGroup,String oldGroup,String oldID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sentenceID",sentenceID);
        values.put("GroupName",newGroup);
        db.update("Setting_ex3_normal",values,"sentenceID = " +  oldID +" and GroupName = '" + oldGroup +"'",null);
        db.close();
    }

    public void update_ex3_hard_st (String sentenceID,String newGroup,String oldGroup,String oldID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sentenceID",sentenceID);
        values.put("GroupName",newGroup);
        db.update("Setting_ex3_hard",values,"sentenceID = " +  oldID +" and GroupName = '" + oldGroup +"'",null);
        db.close();
    }

    public ArrayList<String> ex2_char_inGroup (String GroupName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Char = new ArrayList<>();
        Cursor cursor = db.rawQuery("select Character_ex2.Char , COUNT(*) count\n" +
                "from Setting_ex2 \n" +
                "INNER JOIN Character_ex2 \n" +
                "on Setting_ex2.choiceID = Character_ex2.choiceID\n" +
                "where Setting_ex2.GroupName = '" +GroupName+ "'\n" +
                "GROUP BY Character_ex2.Char\n" +
                "Having COUNT(*) > 1 ",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Char.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return Char;
    }

    public ArrayList<String> st_ex2_get_ImageChar(String GroupName){
        ArrayList<String> get_ImageChar = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select Character_ex2.Image_Char from Setting_ex2 INNER JOIN Character_ex2 \n" +
                "on Setting_ex2.choiceID = Character_ex2.choiceID\n" +
                "where Setting_ex2.GroupName = '" +GroupName+ "'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_ImageChar.add(cursor.getString(0));
            cursor.moveToNext();
        }
        db.close();
        return get_ImageChar;
    }

    public ArrayList<word_Image_object> get_Image_word(String word){
        ArrayList<word_Image_object> Image = new ArrayList<>();
        word_Image_object word_image_object = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select Image,path_image from Word where word = '"+word+"'",null);
        cursor.moveToFirst();
        word_image_object = new word_Image_object(cursor.getString(0),cursor.getString(1));
        Image.add(word_image_object);
        return Image;
    }
}
