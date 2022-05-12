package com.example.GameEnglish.flash_card;

import java.util.ArrayList;

public class Character {
    private String Image,correct;
    private ArrayList<String> choice;

    public Character(String Image, ArrayList<String> choice,String correct){
        this.Image = Image;
        this.choice = choice;
        this.correct = correct;
    }

    public ArrayList<String> getChoice() {
        return choice;
    }

    public void setChoice(ArrayList<String> choice) {
        this.choice = choice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }
}
