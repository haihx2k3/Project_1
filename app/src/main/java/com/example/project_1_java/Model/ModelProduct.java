package com.example.project_1_java.Model;

import java.io.Serializable;
import java.util.List;

public class ModelProduct {
    private String id;
    private String sellerId;
    private String title;
    private String detail;
    private String price;
    private List<String> uriList;
    private List<ClassifyModel> classify;
    public ModelProduct(){};
    public ModelProduct(String id, String sellerId, String title, String detail, String price, List<String> uriList, List<ClassifyModel> classify) {
        this.id = id;
        this.sellerId = sellerId;
        this.title = title;
        this.detail = detail;
        this.price = price;
        this.uriList = uriList;
        this.classify = classify;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public List<ClassifyModel> getClassify() {
        return classify;
    }

    public void setClassify(List<ClassifyModel> classify) {
        this.classify = classify;
    }
}
