package com.example.skripsi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.skripsi.adapter.AlfabetAdapter;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MAlfabet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Alfabet extends AppCompatActivity implements AlfabetAdapter.AlfabetAdapterListener {

    private List<MAlfabet> mAlfabetList;
    private AlfabetAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alfabet);

        mAlfabetList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoint.API+"alfabet",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("alfabet");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                mAlfabetList.add(new MAlfabet(
                                        json.getString("id_alfabet"),
                                        json.getString("nama_alfabet")
                                ));
                                adapter = new AlfabetAdapter(Alfabet.this, mAlfabetList, Alfabet.this);
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
    public MAlfabet onAlfabetSelected(MAlfabet mAlfabet) {
        return mAlfabet;
    }
}