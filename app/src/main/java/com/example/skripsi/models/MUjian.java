package com.example.skripsi.models;

public class MUjian {
    private String gambar;
    private String id_soal;
    private String question;
    private String answer;
    private int level;
    private String a;
    private String b;
    private String c;
    private String d;

    public MUjian() {
    }

    public MUjian(String gambar, String id_soal, String question, String answer, int level, String pga, String pgb, String pgc, String pgd) {
        this.gambar=gambar;
        this.id_soal = id_soal;
        this.question = question;
        this.answer = answer;
        this.level = level;
        this.a = pga;
        this.b = pgb;
        this.c = pgc;
        this.d = pgd;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getId_soal() {
        return id_soal;
    }

    public void setId_soal(String id_soal) {
        this.id_soal = id_soal;
    }
}