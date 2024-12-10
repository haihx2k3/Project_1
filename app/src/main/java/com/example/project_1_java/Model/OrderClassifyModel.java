package com.example.project_1_java.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.project_1_java.Funcion.FormatVND;

public class OrderClassifyModel implements Parcelable {
    private String idOrder;
    private String sellerId;
    private String img;
    private String title;
    private Float price;
    private String type;
    private Integer count;
    private Float delivery = 18.300F;
    private String info;
    private String address;

    public OrderClassifyModel() {
    }

    public OrderClassifyModel(String title, String type, Float price, String img, int count, String sellerId, String idOrder) {
        this.title = title;
        this.type = type;
        this.price = price;
        this.img = img;
        this.count = count;
        this.sellerId = sellerId;
        this.info = null;
        this.address = null;
        this.idOrder = idOrder;
    }

    protected OrderClassifyModel(Parcel in) {
        idOrder = in.readString();
        sellerId = in.readString();
        img = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        type = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        if (in.readByte() == 0) {
            delivery = null;
        } else {
            delivery = in.readFloat();
        }
        info = in.readString();
        address = in.readString();
    }

    public static final Creator<OrderClassifyModel> CREATOR = new Creator<OrderClassifyModel>() {
        @Override
        public OrderClassifyModel createFromParcel(Parcel in) {
            return new OrderClassifyModel(in);
        }

        @Override
        public OrderClassifyModel[] newArray(int size) {
            return new OrderClassifyModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idOrder);
        dest.writeString(sellerId);
        dest.writeString(img);
        dest.writeString(title);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeString(type);
        if (count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(count);
        }
        if (delivery == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(delivery);
        }
        dest.writeString(info);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters...

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
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

    public void setCount(int count) {
        this.count = count;
    }

    public Float getDelivery() {
        return delivery;
    }

    public void setDelivery(Float delivery) {
        this.delivery = delivery;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getTotal() {
        Float finalPrice = (price != null ? price : 0.0F);
        Float finalDelivery = (delivery != null ? delivery : 0.0F);
        return String.format("%.3f", finalPrice + finalDelivery);
    }
}
