package com.example.project_1_java.Model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String userId;
    private String userName;
    private Timestamp createdTimeStamp;
    private String imgProfile;
    private String gender;
    private String birthDay;
    private String phone;

    public UserModel() {}

    public UserModel(String userId, String userName, Timestamp createdTimeStamp, String imgProfile, String gender, String birthDay,String phone) {
        this.userId = userId;
        this.userName = userName;
        this.createdTimeStamp = createdTimeStamp;
        this.imgProfile = imgProfile;
        this.gender = gender;
        this.birthDay = birthDay;
        this.phone = phone;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
    public String getPhone(){return this.phone;}
    public void setPhone(String phone){this.phone = phone;}
}
