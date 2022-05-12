package com.example.GameEnglish.word_builder;

public class word {
    private String id,word;

    public word(String id, String word) {
        this.id = id;
        this.word = word;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
