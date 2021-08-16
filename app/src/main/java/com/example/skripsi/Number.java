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
import com.example.skripsi.adapter.NumberAdapter;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Number extends AppCompatActivity implements NumberAdapter.NumberAdapterListener {

    private List<MNumber> mNumberList;
    private NumberAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        mNumberList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoint.API+"number",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("number");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                mNumberList.add(new MNumber(
                                        json.getString("id_number"),
                                        json.getString("nama_number")
                                ));
                                adapter = new NumberAdapter(Number.this, mNumberList, Number.this);
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
    public MNumber onNumberSelected(MNumber mNumber) {
        return mNumber;
    }
}