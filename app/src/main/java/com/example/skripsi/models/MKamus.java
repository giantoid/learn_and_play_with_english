package com.example.skripsi.models;

public class MKamus {
    private String id, kategori;

    public MKamus(String id, String kategori) {
        this.id = id;
        this.kategori = kategori;
    }

    public String getId() {
        return id;
    }

    public String getKategori() {
        return kategori;
    }
}
