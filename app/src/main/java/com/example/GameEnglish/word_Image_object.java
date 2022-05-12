package com.example.GameEnglish;

public class word_Image_object {
    private String defualt_Image;
    private String path_Image;

    public word_Image_object(String defualt_Image,String path_Image){
        this.defualt_Image = defualt_Image;
        this.path_Image = path_Image;
    }

    public String getDefualt_Image() {
        return defualt_Image;
    }

    public void setDefualt_Image(String defualt_Image) {
        this.defualt_Image = defualt_Image;
    }

    public String getPath_Image() {
        return path_Image;
    }

    public void setPath_Image(String path_Image) {
        this.path_Image = path_Image;
    }
}
