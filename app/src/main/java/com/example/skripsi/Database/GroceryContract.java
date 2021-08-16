package com.example.skripsi.Database;

import android.provider.BaseColumns;

public class GroceryContract {
    private GroceryContract() {
    }
    public static final class UjuanTB implements BaseColumns {
        public static final String TABLE_NAME = "quiz";
        public static final String COLUMN_ID_SOAL = "id_soal";
        public static final String COLUMN_GAMBAR = "gambar";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_A = "a";
        public static final String COLUMN_B = "b";
        public static final String COLUMN_C = "c";
        public static final String COLUMN_D = "d";
        public static final String COLUMN_ANSWER= "answer";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static final class TebakGambarTB implements BaseColumns {
        public static final String TABLE_NAME = "tebakgambar";
        public static final String COLUMN_ID_TEBAKGAMBAR = "id_tebakgambar";
        public static final String COLUMN_ID_KAMUS= "id_kamus";
        public static final String COLUMN_ENGLISH= "englis";
        public static final String COLUMN_INDONESIA= "indonesia";
        public static final String COLUMN_GAMBAR= "gambar";
        public static final String COLUMN_ANSWER= "answer";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}