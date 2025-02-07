package com.example.project_1_java.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderClassifyModel{
    private String idOrder;
    private String sellerId;
    private String seller;
    private String img;
    private String title;
    private Float price;
    private String type;
    private int count;
    private Float priceDelivery = 18.300F;
    private String delivery;
    private  String status;
    private String info;
    private String address;
    private Object orderDate;

    public OrderClassifyModel() {
    }

    public OrderClassifyModel(String title, String type, Float price, String img, int count, String sellerId, String idOrder,String seller) {
        this.sellerId = sellerId;
        this.idOrder = idOrder;
        this.seller = seller;
        this.title = title;
        this.type = type;
        this.price = price;
        this.img = img;
        this.count = count;
        this.info = null;
        this.address = null;
        this.delivery = "Nhanh";
        this.status = "Pending";
        this.orderDate = FieldValue.serverTimestamp();
    }
    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
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

    public Float getpriceDelivery() {
        return priceDelivery;
    }

    public void setpriceDelivery(Float priceDelivery) {
        this.priceDelivery = priceDelivery;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Object getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Object orderDate) {
        this.orderDate = orderDate;
    }

    @SuppressLint("DefaultLocale")
    public String getTotal() {
        Float finalPrice = (price != null ? price : 0.0F);
        Float finalpriceDelivery = (priceDelivery != null ? priceDelivery : 0.0F);
        return String.format("%.3f", finalPrice + finalpriceDelivery);
    }
}
