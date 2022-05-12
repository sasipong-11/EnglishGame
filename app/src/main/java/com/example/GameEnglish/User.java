package com.example.GameEnglish;

public class User {
    private String Username,Password,UserID,Fullname,sex,Permission;
    private Integer Age;
    private String Picture;

    public User(String Username, String Password,String UserID,String Fullname,Integer Age,String sex,String Permission,String Picture) {
        this.Username = Username;
        this.Password = Password;
        this.UserID = UserID;
        this.Fullname = Fullname;
        this.Age = Age;
        this.sex = sex;
        this.Permission = Permission;
        this.Picture = Picture;

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
}
