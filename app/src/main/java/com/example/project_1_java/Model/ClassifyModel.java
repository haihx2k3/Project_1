package com.example.project_1_java.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ClassifyModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }
    protected ClassifyModel(Parcel in) {
        title = in.readString();
        price = in.readString();
        img = in.readString();
    }

    public static final Creator<ClassifyModel> CREATOR = new Creator<ClassifyModel>() {
        @Override
        public ClassifyModel createFromParcel(Parcel in) {
            return new ClassifyModel(in);
        }

        @Override
        public ClassifyModel[] newArray(int size) {
            return new ClassifyModel[size];
        }
    };
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title!=null ? title:"");
        dest.writeString(price!=null ? price:"");
        dest.writeString(img!=null ? img:"");
    }

}
