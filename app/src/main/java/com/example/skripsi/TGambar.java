package com.example.skripsi;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.skripsi.Database.GroceryContract;
import com.example.skripsi.Database.GroceryContract.TebakGambarTB;
import com.example.skripsi.Database.TebakGambarDB;
import com.example.skripsi.adapter.TebakGambarAdapter;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.models.MTebakGambar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TGambar extends AppCompatActivity {
    private SQLiteDatabase mDatabase_w;

    private List<MTebakGambar> mTebakGambar;
    private TebakGambarAdapter adapter;

    private RecyclerView rvLevel;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_gambar);

        mTebakGambar = new ArrayList<>();

        rvLevel = findViewById(R.id.rvLevel);
//        rvLevel.setHasFixedSize(true);
        rvLevel.setLayoutManager(new GridLayoutManager(this,4));

        TebakGambarDB dbHelper = new TebakGambarDB(this);
        mDatabase_w = dbHelper.getWritableDatabase();
        startQuiz();
    }
    public void delete() {
        mDatabase_w.delete(GroceryContract.TebakGambarTB.TABLE_NAME, null, null);

    }
    private void startQuiz() {

        delete();
        RequestQueue requestQueue = Volley.newRequestQueue(TGambar.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndPoint.API+"tebakgambar", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //let's parse json data
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("tebakgambar");

                    for(int i=0; i<array.length(); i++ ){
                        JSONObject jo = array.getJSONObject(i);
                        String id = jo.getString("id_tebakgambar");
                        String id_kamus = jo.getString("id_kamus");
                        String english = jo.getString("english");
                        String indonesia = jo.getString("indonesia");
                        String gambar = jo.getString("gambar");
                        mTebakGambar.add(new MTebakGambar(
                                jo.getString("id_tebakgambar")
                        ));

                        adapter = new TebakGambarAdapter(TGambar.this,mTebakGambar);
                        rvLevel.setAdapter(adapter);

                        cv = new ContentValues();
                        cv.put(TebakGambarTB.COLUMN_ID_TEBAKGAMBAR, id);
                        cv.put(TebakGambarTB.COLUMN_ID_KAMUS, id_kamus);
                        cv.put(TebakGambarTB.COLUMN_ENGLISH, english);
                        cv.put(TebakGambarTB.COLUMN_INDONESIA, indonesia);
                        cv.put(TebakGambarTB.COLUMN_GAMBAR, gambar);
                        cv.put(TebakGambarTB.COLUMN_ANSWER, "true");
                        mDatabase_w.insert(TebakGambarTB.TABLE_NAME, null, cv);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TGambar.this, "Periksa koneksi internet Anda!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mTebakGambar.clear();
        startQuiz();
    }
}