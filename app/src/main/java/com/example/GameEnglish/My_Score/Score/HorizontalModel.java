package com.example.GameEnglish.My_Score.Score;

public class HorizontalModel {
    String word, score;

    public HorizontalModel(String word,String score){
        this.word = word;
        this.score = score;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
