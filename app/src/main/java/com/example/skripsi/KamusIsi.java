package com.example.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.skripsi.adapter.KamusIsiAdapter;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MKamusIsi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KamusIsi extends AppCompatActivity implements KamusIsiAdapter.KamusIsiAdapterListener, SearchView.OnQueryTextListener {

    private List<MKamusIsi> mKamusIsiList;
    private KamusIsiAdapter adapter;

    private TextView textJudul;
    private SearchView cariKamus;

    private RecyclerView recyclerView;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamus_isi);

        mKamusIsiList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textJudul = findViewById(R.id.textJudul);
        cariKamus = findViewById(R.id.cariKamus);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        textJudul.setText(intent.getStringExtra("judul"));

        cariKamus.setOnQueryTextListener(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoint.API + "kamus/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("kamus");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                mKamusIsiList.add(new MKamusIsi(
                                        json.getString("id_kamus"),
                                        json.getString("english"),
                                        json.getString("indonesia"),
                                        json.getString("id_kategori"),
                                        json.getString("gambar")
                                ));
                                adapter = new KamusIsiAdapter(KamusIsi.this, mKamusIsiList, KamusIsi.this);
                                recyclerView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public MKamusIsi onKamusIsiSelected(MKamusIsi mKamusIsi) {
        return mKamusIsi;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return true;
    }
}