package com.example.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class KamusDetail extends AppCompatActivity implements View.OnClickListener {

    private TextView txtJudul, txtEnglish;
    private ImageView imgKamus;
    private ImageButton btnSpeaker;

    TextToSpeech TTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamus_detail);

        txtJudul = findViewById(R.id.txtJudul);
        txtEnglish = findViewById(R.id.txtEnglish);
        imgKamus = findViewById(R.id.imgKamus);
        btnSpeaker = findViewById(R.id.btnSpeaker);

        TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.US);
//                    TTS.setLanguage(new Locale("in_ID"));

                } else {
                    Toast.makeText(KamusDetail.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnSpeaker.setOnClickListener(this);
        loadKamus();
    }

    private void loadKamus() {
        Intent intent = getIntent();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoint.API+"kamusDetail/"+intent.getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("kamusDetail");
                            JSONObject json = jsonArray.getJSONObject(0);
                            txtJudul.setText(json.getString("indonesia"));
                            txtEnglish.setText(json.getString("english"));
                            Glide.with(KamusDetail.this).load(EndPoint.GAMBAR+json.getString("gambar")).into(imgKamus);

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
    public void onClick(View view) {
        String toSpeak = txtEnglish.getText().toString().trim();
            TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}