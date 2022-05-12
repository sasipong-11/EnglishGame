package com.example.GameEnglish.word_builder;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class segmentation {

    private String replace;
    private static String word1 = null;
    private String word;
    private String[] symbols = {"ั","็","ิ","่","ํ","ุ","ู","้","๊","๋","ี","์","ำ","ื"};

    public  String Break(String i){

        Locale u = new Locale("th");
        java.text.BreakIterator boundary = java.text.BreakIterator.getWordInstance(u);
        try {
            replace = i.replace(" ","");
            boundary.setText(replace);
            StringBuffer th = new StringBuffer();
            int start = boundary.first();
            for (int next = boundary.next(); next != java.text.BreakIterator.DONE; start = next, next = boundary.next()) {

                    th.append(replace.substring(start,next) + ",");
                    word = th.toString();
                    word1 = word; }
            }
        catch(Exception e){
            System.out.println("Error !" + e.getMessage());
        }
        return word1;
    }

    public ArrayList<String> split (String sentence){

        String[] words = sentence.split(",");
        ArrayList<String> segment = new ArrayList<>(Arrays.asList(words));

        return  segment;
    }

    public ArrayList<String> substring (String sentence){

        ArrayList<String> segmentation = new ArrayList<>();
        ArrayList<String> Arraytext = new ArrayList<>();
        Arraytext.addAll(Arrays.asList(sentence.split("(?!^)")));
        for (String Character : Arraytext) {
            boolean isMatch = false;
            for(String symbol : symbols) {
                if (Character.equals(symbol)){
                    isMatch = true;
                }
            }
            if (!isMatch){
                segmentation.add(Character);
            } else {
                segmentation.set(segmentation.size()-1,segmentation.get(segmentation.size()-1)+Character);
                //segmentation.add(segmentation.get(segmentation.size()-1)+Character);
                //segmentation.remove(segmentation.size()-2);
            }
        }
        //segmentation.remove(0);
        Log.i("segmentation",segmentation.toString());
        return segmentation;
    }
}
