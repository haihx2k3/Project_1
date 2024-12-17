package com.example.project_1_java.Model;

import java.util.List;

public class BoothModel {
    private String id;
    private String sellerId;
    private String title;
    private String price;
    private List<String> uriList;

    public BoothModel() {
    }

    public BoothModel(String id, String sellerId, String title, String price, List<String> uriList) {
        this.id = id;
        this.sellerId = sellerId;
        this.title = title;
        this.price = price;
        this.uriList = uriList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
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

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
}
