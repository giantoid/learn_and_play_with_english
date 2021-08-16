package com.example.skripsi.models;

public class MKamusIsi {
    private String idKamus, english, indonesia, idKategori, gambar;

    public MKamusIsi(String idKamus, String english, String indonesia, String idKategori, String gambar) {
        this.idKamus = idKamus;
        this.english = english;
        this.indonesia = indonesia;
        this.idKategori = idKategori;
        this.gambar = gambar;
    }

    public String getIdKamus() {
        return idKamus;
    }

    public String getEnglish() {
        return english;
    }

    public String getIndonesia() {
        return indonesia;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public String getGambar() {
        return gambar;
    }
}
