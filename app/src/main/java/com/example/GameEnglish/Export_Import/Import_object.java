package com.example.GameEnglish.Export_Import;

public class Import_object {
    private String ID,groupname;

    public Import_object(String ID, String groupname){
        this.ID = ID;
        this.groupname = groupname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
