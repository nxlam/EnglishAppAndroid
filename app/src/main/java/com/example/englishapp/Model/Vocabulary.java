package com.example.englishapp.Model;

import java.io.Serializable;

public class Vocabulary implements Serializable {
    int id, categoryID;
    String engsub, vietsub;
    byte[] img;

    public Vocabulary(int categoryID, String engsub, String vietsub, byte[] img) {
        this.categoryID = categoryID;
        this.engsub = engsub;
        this.vietsub = vietsub;
        this.img = img;
    }

    public Vocabulary(int id, int categoryID, String engsub, String vietsub, byte[] img) {
        this.id = id;
        this.categoryID = categoryID;
        this.engsub = engsub;
        this.vietsub = vietsub;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getEngsub() {
        return engsub;
    }

    public void setEngsub(String engsub) {
        this.engsub = engsub;
    }

    public String getVietsub() {
        return vietsub;
    }

    public void setVietsub(String vietsub) {
        this.vietsub = vietsub;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
