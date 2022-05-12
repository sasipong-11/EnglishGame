package com.example.GameEnglish;

import java.util.ArrayList;

public class Score_word_builder_word {

    private String Fullname,User;
    private ArrayList<String> Word;
    private ArrayList<Integer> Score;

    public Score_word_builder_word(String Fullname, ArrayList<String> Word, ArrayList<Integer> Score, String User){
        this.Fullname = Fullname;
        this.Word = Word;
        this.Score = Score;
        this.User = User;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public ArrayList<String> getWord() {
        return Word;
    }

    public void setWord(ArrayList word) {
        Word = word;
    }

    public ArrayList<Integer> getScore() {
        return Score;
    }

    public void setScore(ArrayList score) {
        Score = score;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
