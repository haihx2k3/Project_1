package com.example.project_1_java.Model;

import java.util.List;

public class ImageModel {
    private String id;
    private List<String> image;

    public ImageModel() {
    }

    public ImageModel(String id, List<String> image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
