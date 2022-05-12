package com.example.GameEnglish.flash_card.st_flash_card_adapter;

public class Item_st_flash_card {
    private String name_char;
    private int check = 0;

    private String check1,check2,check3,check4;
    private String name_image1,name_image2,name_image3,name_image4;



    public Item_st_flash_card(String name_char, String name_image1, String name_image2 , String name_image3, String name_image4){
        this.name_char = name_char;
        this.name_image1 = name_image1;
        this.name_image2 = name_image2;
        this.name_image3 = name_image3;
        this.name_image4 = name_image4;

    }

    public String getName_char() {
        return name_char;
    }

    public void setName_char(String name_char) {
        this.name_char = name_char;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getCheck1() {
        return check1;
    }

    public void setCheck1(String check1) {
        this.check1 = check1;
    }

    public String getCheck2() {
        return check2;
    }

    public void setCheck2(String check2) {
        this.check2 = check2;
    }

    public String getCheck3() {
        return check3;
    }

    public void setCheck3(String check3) {
        this.check3 = check3;
    }

    public String getName_image1() {
        return name_image1;
    }

    public void setName_image1(String name_image1) {
        this.name_image1 = name_image1;
    }

    public String getName_image2() {
        return name_image2;
    }

    public void setName_image2(String name_image2) {
        this.name_image2 = name_image2;
    }

    public String getName_image3() {
        return name_image3;
    }

    public void setName_image3(String name_image3) {
        this.name_image3 = name_image3;
    }

    public String getCheck4() {
        return check4;
    }

    public void setCheck4(String check4) {
        this.check4 = check4;
    }

    public String getName_image4() {
        return name_image4;
    }

    public void setName_image4(String name_image4) {
        this.name_image4 = name_image4;
    }
}
