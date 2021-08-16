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
import com.example.skripsi.adapter.PercakapanAdapter;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MPercakapan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Percakapan extends AppCompatActivity implements PercakapanAdapter.PercakapanAdapterListener {

    private List<MPercakapan> mPercakapanList;
    private PercakapanAdapter adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percakapan);

        //getting the recyclerview from xml
        mPercakapanList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoint.API+"percakapan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("percakapan");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                mPercakapanList.add(new MPercakapan(
                                        json.getString("id_percakapan"),
                                        json.getString("nama"),
                                        json.getString("kalimat"),
                                        json.getString("no_percakapan")
                                ));
                                adapter = new PercakapanAdapter(Percakapan.this, mPercakapanList, Percakapan.this);
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
    public MPercakapan onPercakapanSelected(MPercakapan MPercakapan) {
        return MPercakapan;
    }
}