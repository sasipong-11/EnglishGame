package com.example.GameEnglish.flash_card;

public class Object_Choice {
    private String choice1,choice2,Char;

    public Object_Choice(String choice1,String choice2,String Char){
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.Char = Char;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChar() {
        return Char;
    }

    public void setChar(String aChar) {
        Char = aChar;
    }
}
