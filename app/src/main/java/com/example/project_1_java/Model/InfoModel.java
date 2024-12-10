package com.example.project_1_java.Model;

import java.io.Serializable;

public class InfoModel implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String location;
    private String locationPlus;

    public InfoModel() {}

    public InfoModel(String id, String name, String phone, String location, String locationPlus) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.locationPlus = locationPlus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationPlus() {
        return locationPlus;
    }

    public void setLocationPlus(String locationPlus) {
        this.locationPlus = locationPlus;
    }
}
