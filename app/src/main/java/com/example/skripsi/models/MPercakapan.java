package com.example.skripsi.models;

public class MPercakapan {
    private String id_percakapan, no_percakapan, nama, kalimat;


    public MPercakapan(String id_percakapan, String nama, String kalimat, String no_percakapan) {
        this.id_percakapan = id_percakapan;
        this.no_percakapan = no_percakapan;
        this.nama = nama;
        this.kalimat = kalimat;
    }

    public String getId_percakapan(){
        return id_percakapan;
    }

    public String getNo_percakapan() {
        return no_percakapan;
    }

    public String getNama() {
        return nama;
    }

    public String getKalimat() {
        return kalimat;
    }
}
