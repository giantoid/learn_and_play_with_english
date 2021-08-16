package com.example.skripsi.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.skripsi.Database.GroceryContract.TebakGambarTB;
import com.example.skripsi.models.MTebakGambar;

import java.util.ArrayList;
import java.util.List;


public class TebakGambarDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quizz_db";
    public static final int DATABASE_VERSION = 10;

    private SQLiteDatabase db;

    public TebakGambarDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                TebakGambarTB.TABLE_NAME + " (" +
                GroceryContract.TebakGambarTB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryContract.TebakGambarTB.COLUMN_ID_TEBAKGAMBAR + " TEXT NOT NULL, " +
                GroceryContract.TebakGambarTB.COLUMN_ID_KAMUS + " INTEGER NOT NULL, " +
                TebakGambarTB.COLUMN_ENGLISH + " TEXT NOT NULL, " +
                TebakGambarTB.COLUMN_INDONESIA + " TEXT NOT NULL, " +
                GroceryContract.TebakGambarTB.COLUMN_GAMBAR + " TEXT NOT NULL, " +
                TebakGambarTB.COLUMN_ANSWER + " TEXT NOT NULL, " +
                TebakGambarTB.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_GROCERYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GroceryContract.TebakGambarTB.TABLE_NAME);
        onCreate(db);
    }

    public List<MTebakGambar> getAllQuestions() {
        List<MTebakGambar> mTebakGambarList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TebakGambarTB.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                MTebakGambar mTebakGambar = new MTebakGambar();
                mTebakGambar.setId(c.getString(c.getColumnIndex(TebakGambarTB.COLUMN_ID_TEBAKGAMBAR)));
                mTebakGambar.setId_kamus(c.getString(c.getColumnIndex(GroceryContract.TebakGambarTB.COLUMN_ID_KAMUS)));
                mTebakGambar.setEnglish(c.getString(c.getColumnIndex(TebakGambarTB.COLUMN_ENGLISH)));
                mTebakGambar.setIndonesia(c.getString(c.getColumnIndex(TebakGambarTB.COLUMN_INDONESIA)));
                mTebakGambar.setGambar(c.getString(c.getColumnIndex(TebakGambarTB.COLUMN_GAMBAR)));
                mTebakGambar.setAnswer(c.getString(c.getColumnIndex(GroceryContract.TebakGambarTB.COLUMN_ANSWER)));
                mTebakGambarList.add(mTebakGambar);
            } while (c.moveToNext());
        }

        c.close();
        return mTebakGambarList;
    }
}