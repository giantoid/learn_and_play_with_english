package com.example.skripsi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.skripsi.adapter.KamusAdapter;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MKamus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Kamus extends AppCompatActivity implements KamusAdapter.KamusAdapterListener {

    private List<MKamus> mKamusList;
    private KamusAdapter adapter;
    
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamus);

        mKamusList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoint.API+"kategori",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("kategori");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                mKamusList.add(new MKamus(
                                        json.getString("id_kategori"),
                                        json.getString("nama_kategori")
                                ));
                                adapter = new KamusAdapter(Kamus.this, mKamusList, Kamus.this);
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
    public MKamus onKamusSelected(MKamus mKamus) {
        return mKamus;
    }
}