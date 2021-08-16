package com.example.skripsi.models;

public class MTebakGambar {
    String id, id_kamus, english, indonesia, gambar, answer;

//    public MTebakGambar(String id, String id_kamus, String english, String indonesia, String gambar, String answer) {
//        this.id = id;
//        this.id_kamus = id_kamus;
//        this.english = english;
//        this.indonesia = indonesia;
//        this.gambar = gambar;
//        this.answer = answer;
//    }

    public MTebakGambar(String id) {
        this.id = id;
    }

    public MTebakGambar() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId_kamus(String id_kamus) {
        this.id_kamus = id_kamus;
    }

    public String getId_kamus() {
        return id_kamus;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getEnglish() {
        return english;
    }

    public void setIndonesia(String indonesia) {
        this.indonesia = indonesia;
    }

    public String getIndonesia() {
        return indonesia;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}
