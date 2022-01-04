package com.udinus.uas4506_11743_11758_11773_11774_12098.Model;

import java.util.ArrayList;

public class ResepModel {
    private String nama;
    private String author;
    private String kategori;
    private String image;
    private ArrayList<String> bahan;
    private ArrayList<String> langkah;

    public ResepModel(){

    }

    public ResepModel(String nama, String author, String kategori, String image, ArrayList<String> bahan, ArrayList<String> langkah) {
        this.nama = nama;
        this.author = author;
        this.kategori = kategori;
        this.image = image;
        this.bahan = bahan;
        this.langkah = langkah;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getBahan() {
        return bahan;
    }

    public void setBahan(ArrayList<String> bahan) {
        this.bahan = bahan;
    }

    public ArrayList<String> getLangkah() {
        return langkah;
    }

    public void setLangkah(ArrayList<String> langkah) {
        this.langkah = langkah;
    }
}
