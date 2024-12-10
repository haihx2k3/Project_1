package com.example.project_1_java.Model;

public class BranchModel {
    private Integer image;
    private String title;

    public BranchModel(String title, Integer image) {
        this.title = title;
        this.image = image;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
