package com.example.hellorfid.utils;
import com.example.hellorfid.model.CommanModel;
public class CommanModelHolder {
    private static CommanModelHolder instance;
    private CommanModel commanModel;

    private CommanModelHolder() {}

    public static synchronized CommanModelHolder getInstance() {
        if (instance == null) {
            instance = new CommanModelHolder();
        }
        return instance;
    }

    public void setCommanModel(CommanModel model) {
        this.commanModel = model;
    }

    public CommanModel getCommanModel() {
        return commanModel;
    }
}