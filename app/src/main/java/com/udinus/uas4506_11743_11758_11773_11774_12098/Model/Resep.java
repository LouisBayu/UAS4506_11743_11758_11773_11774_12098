package com.udinus.uas4506_11743_11758_11773_11774_12098.Model;

import java.io.Serializable;

public class Resep implements Serializable {

    int image;
    String author, nama, kategori;
    String[] bahan, langkah;

    public String getAuthor() {
        return author;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNama() {
        return nama;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public String[] getBahan() {
        return bahan;
    }

    public void setBahan(String[] bahan) {
        this.bahan = bahan;
    }

    public String[] getLangkah() {
        return langkah;
    }

    public void setLangkah(String[] langkah) {
        this.langkah = langkah;
    }
}
