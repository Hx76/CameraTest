package com.example.cameratest;

import org.litepal.crud.LitePalSupport;

import java.sql.Blob;

public class UserInformation extends LitePalSupport {

    /*public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }*/

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    //private int ID;
    private String UserName;
    private String Email;
    private String PassWord;

    public Blob getUserFace() {
        return UserFace;
    }

    public void setUserFace(Blob userFace) {
        UserFace = userFace;
    }

    private Blob UserFace;


}
