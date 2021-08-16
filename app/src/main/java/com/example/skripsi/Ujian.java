package com.example.skripsi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.skripsi.Database.GroceryContract;
import com.example.skripsi.Database.UjianDB;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.SharedPrefManager;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MUjian;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Ujian extends AppCompatActivity {
    private SQLiteDatabase mDatabase_w;

    private static final int REQUEST_CODE_QUIZ = 1;

    private SharedPrefManager shp;

    private TextView textViewskor;
    private int skor;
    ContentValues cv;

    private Button buttonStartQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ujian);

        textViewskor = findViewById(R.id.text_view_skor);
        UjianDB dbHelper = new UjianDB(this);
        mDatabase_w = dbHelper.getWritableDatabase();
        startQuiz();
        
        shp = shp.getInstance(this);

        loadskor();
        buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setEnabled(false);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
                Intent intent = new Intent(Ujian.this, SoalUjian.class);
                startActivityForResult(intent, REQUEST_CODE_QUIZ);
            }
        });

//        cekTugas();

    }
    public void delete() {
        mDatabase_w.delete(GroceryContract.UjuanTB.TABLE_NAME, null, null);

    }
    private void startQuiz() {

        delete();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndPoint.API+"ujian", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("ujian");

                    for(int i=0; i<array.length(); i++ ){
                        JSONObject jo = array.getJSONObject(i);
                        String  gambar = jo.getString("gambar");
                        String  id_soal = jo.getString("id_soal");
                        String soal = jo.getString("nama_soal");
                        String nomor = jo.getString("level");
                        String a = jo.getString("a");
                        String b = jo.getString("b");
                        String c = jo.getString("c");
                        String d = jo.getString("d");
                        String kunci = jo.getString("true");
                        buttonStartQuiz.setEnabled(true);

                        cv = new ContentValues();
                        cv.put(GroceryContract.UjuanTB.COLUMN_ID_SOAL, id_soal);
                        cv.put(GroceryContract.UjuanTB.COLUMN_GAMBAR, gambar);
                        cv.put(GroceryContract.UjuanTB.COLUMN_QUESTION, soal);
                        cv.put(GroceryContract.UjuanTB.COLUMN_AMOUNT, nomor);
                        cv.put(GroceryContract.UjuanTB.COLUMN_A, a);
                        cv.put(GroceryContract.UjuanTB.COLUMN_B, b);
                        cv.put(GroceryContract.UjuanTB.COLUMN_C, c);
                        cv.put(GroceryContract.UjuanTB.COLUMN_D, d);
                        cv.put(GroceryContract.UjuanTB.COLUMN_ANSWER, kunci);
                        mDatabase_w.insert(GroceryContract.UjuanTB.TABLE_NAME, null, cv);
                    }

                    cekTugas();



                }
                catch (Exception e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ujian.this, "Periksa koneksi internet Anda!", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_QUIZ)
        {
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(SoalUjian.EXTRA_SCORE, 0);
                if (score > skor){
                    updateskor(score);
                }
            }
        }
    }

    private void loadskor(){
        skor = shp.getSkor();
        textViewskor.setText("Skor: " + skor);
    }

    private void updateskor(int skorNew){
        skor = skorNew;
        textViewskor.setText("Skor: " + skor);
        shp.setSkor(skor);
        buttonStartQuiz.setEnabled(false);
    }

    private void cekTugas() {
        UjianDB dbHelper = new UjianDB(this);
        List<MUjian> questionList = dbHelper.getAllQuestions();
        final int questionCountTotal = questionList.size();
        Log.d("log", String.valueOf(questionCountTotal));
        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndPoint.API+"cekujian/"+shp.getUser().getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("ujian");
                    Log.d("log", String.valueOf(array.length()));
                    if (array.length() ==  questionCountTotal) {
                        buttonStartQuiz.setEnabled(false);
                    } else {
                        shp.setSkor(0);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ujian.this, "Periksa koneksi internet Anda!", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}