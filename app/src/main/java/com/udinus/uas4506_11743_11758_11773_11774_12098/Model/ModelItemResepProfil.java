package com.udinus.uas4506_11743_11758_11773_11774_12098.Model;

public class ModelItemResepProfil {

    String title, category;
    int image;

    public ModelItemResepProfil(String title, String category, int image) {
        this.title = title;
        this.category = category;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getImage() {
        return image;
    }
}
