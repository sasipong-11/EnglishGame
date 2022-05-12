package com.example.GameEnglish.recyclerView_Ranking;

public class Ranking_Item {

    private String rank,name,score;

    public Ranking_Item(String rank, String name, String score){
        this.rank = rank;
        this.name = name;
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }
}
