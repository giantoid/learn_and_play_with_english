package com.example.skripsi.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.skripsi.Database.GroceryContract.UjuanTB;
import com.example.skripsi.models.MUjian;

import java.util.ArrayList;
import java.util.List;


public class UjianDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quizz.db";
    public static final int DATABASE_VERSION = 10;

    private SQLiteDatabase db;

    public UjianDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                UjuanTB.TABLE_NAME + " (" +
                UjuanTB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UjuanTB.COLUMN_ID_SOAL + " TEXT NOT NULL, " +
                UjuanTB.COLUMN_GAMBAR + " TEXT NOT NULL, " +
                UjuanTB.COLUMN_QUESTION + " TEXT NOT NULL, " +
                UjuanTB.COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                GroceryContract.UjuanTB.COLUMN_A + " TEXT NOT NULL, " +
                GroceryContract.UjuanTB.COLUMN_B + " TEXT NOT NULL, " +
                GroceryContract.UjuanTB.COLUMN_C + " TEXT NOT NULL, " +
                UjuanTB.COLUMN_D + " TEXT NOT NULL, " +
                GroceryContract.UjuanTB.COLUMN_ANSWER + " TEXT NOT NULL, " +
                UjuanTB.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_GROCERYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UjuanTB.TABLE_NAME);
        onCreate(db);
    }

//    private void fillQuestionsTable() {
//        Question q1 = new Question("", "", );
//        addQuestion(q1);
////        Question q2 = new Question("B is correct", "A", "B", "C", 2);
////        addQuestion(q2);
////        Question q3 = new Question("C is correct", "A", "B", "C", 3);
////        addQuestion(q3);
////        Question q4 = new Question("A is correct again", "A", "B", "C", 1);
////        addQuestion(q4);
////        Question q5 = new Question("B is correct again", "A", "B", "C", 2);
////        addQuestion(q5);
//
//    }

//    private void addQuestion(String soal, String nomor, String kunci, String pga, String pgb, String pgc, String pgd) {
//        ContentValues cv = new ContentValues();
//        cv.put(UjuanTB.COLUMN_QUESTION, soal);
//        cv.put(UjuanTB.COLUMN_AMOUNT, nomor);
//        cv.put(UjuanTB.COLUMN_A, pga);
//        cv.put(UjuanTB.COLUMN_B, pgb);
//        cv.put(UjuanTB.COLUMN_C, pgc);
//        cv.put(UjuanTB.COLUMN_D, pgd);
//        cv.put(UjuanTB.COLUMN_ANSWER, kunci);
//        db.insert(UjuanTB.TABLE_NAME, null, cv);
//    }

    public List<MUjian> getAllQuestions() {
        List<MUjian> mUjianList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + GroceryContract.UjuanTB.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                MUjian mUjian = new MUjian();
                mUjian.setId_soal(c.getString(c.getColumnIndex(UjuanTB.COLUMN_ID_SOAL)));
                mUjian.setGambar(c.getString(c.getColumnIndex(UjuanTB.COLUMN_GAMBAR)));
                mUjian.setQuestion(c.getString(c.getColumnIndex(UjuanTB.COLUMN_QUESTION)));
                mUjian.setA(c.getString(c.getColumnIndex(UjuanTB.COLUMN_A)));
                mUjian.setB(c.getString(c.getColumnIndex(UjuanTB.COLUMN_B)));
                mUjian.setC(c.getString(c.getColumnIndex(UjuanTB.COLUMN_C)));
                mUjian.setD(c.getString(c.getColumnIndex(UjuanTB.COLUMN_D)));
                mUjian.setAnswer(c.getString(c.getColumnIndex(GroceryContract.UjuanTB.COLUMN_ANSWER)));
                mUjian.setLevel(c.getInt(c.getColumnIndex(UjuanTB.COLUMN_AMOUNT)));
                mUjianList.add(mUjian);
            } while (c.moveToNext());
        }

        c.close();
        return mUjianList;
    }
}