package com.example.project_1_java.Store.InterFace;

import com.example.project_1_java.Model.ClassifyModel;

import java.util.ArrayList;

public interface ClassifyReceived {
    void onDataSent(ArrayList<ClassifyModel> data);
}
