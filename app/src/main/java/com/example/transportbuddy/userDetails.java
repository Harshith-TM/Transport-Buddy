package com.example.transportbuddy;

public class userDetails {

    String userName;
    String userMail;
    String userPwd;
    String  userPhone;
    String userDob;
    String userGender;

    public userDetails(String userName, String userMail, String userPwd,String userPhone,String userDob,String userGender) {
        this.userName = userName;
        this.userMail = userMail;
        this.userPwd = userPwd;
        this.userPhone = userPhone;
        this.userDob = userDob;
        this.userGender= userGender;
    }

    public String getUserName() {
        return userName;
    }
    public String getUserMail() {
        return userMail;
    }
    public String getPassWord() {
        return userPwd;
    }
    public String getUserPhone(){return userPhone;}
    public String getUserDob(){return userDob;}
    public String getUserGender(){return userGender;}

}