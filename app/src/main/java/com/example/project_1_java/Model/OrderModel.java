package com.example.project_1_java.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class OrderModel implements Parcelable {
    private String id;
    private String title;
    private String type;
    private String price;
    private String img;
    private int count;
    private Float total;
    private String sellerId;

    public OrderModel() {}

    public OrderModel(String id, String title, String type, String price, String img, int count, Float total, String sellerId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.price = price;
        this.img = img;
        this.count = count;
        this.total = total;
        this.sellerId = sellerId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(price);
        parcel.writeString(img);
        parcel.writeInt(count);
        parcel.writeValue(total);
        parcel.writeString(sellerId);
    }
    protected OrderModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
        price = in.readString();
        img = in.readString();
        count = in.readInt();
        total = (Float) in.readValue(Float.class.getClassLoader());
        sellerId = in.readString();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };
}
