package com.example.project_1_java.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ClassifyModel implements Serializable {
    private String title;
    private String price;
    private String img;

    public ClassifyModel() {}

    public ClassifyModel(String title, String price, String img) {
        this.title = title;
        this.price = price;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public ClassifyModel copy(String img) {
        return new ClassifyModel(this.title, this.price, img);
    }

}
