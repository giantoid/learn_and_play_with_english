package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Belajar extends AppCompatActivity implements View.OnClickListener {

    private Button btn_percakapan, btn_grammar, btn_alfabet, btn_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belajar);

        btn_percakapan = findViewById(R.id.btn_percakapan);
        btn_grammar = findViewById(R.id.btn_grammar);
        btn_alfabet = findViewById(R.id.btn_alfabet);
        btn_number = findViewById(R.id.btn_number);

        btn_percakapan.setOnClickListener(this);
        btn_grammar.setOnClickListener(this);
        btn_alfabet.setOnClickListener(this);
        btn_number.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_percakapan) {
            startActivity(new Intent(this, Percakapan.class));
        } else if (v == btn_grammar) {
            startActivity(new Intent(this, Grammar.class));
        } else if (v == btn_alfabet) {
            startActivity(new Intent(this, Alfabet.class));
        } else if (v == btn_number) {
            startActivity(new Intent(this, Number.class));
        }
    }
}