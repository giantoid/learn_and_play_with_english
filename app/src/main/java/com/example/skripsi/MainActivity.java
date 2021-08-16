package com.example.skripsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.skripsi.core.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

//    private Button btKeluar;
    private CardView cardBelajar, cardKamus, cardTebakGambar, cardUjian, cardProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardBelajar = findViewById(R.id.cardBelajar);
        cardKamus = findViewById(R.id.cardKamus);
        cardTebakGambar = findViewById(R.id.cardTebakGambar);
        cardUjian = findViewById(R.id.cardUjian);
//        btKeluar = findViewById(R.id.btKeluar);
        cardProfil = findViewById(R.id.cardProfil);

        cardBelajar.setOnClickListener(this);
        cardKamus.setOnClickListener(this);
        cardTebakGambar.setOnClickListener(this);
        cardUjian.setOnClickListener(this);
//        btKeluar.setOnClickListener(this);
        cardProfil.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == cardBelajar) {
            startActivity(new Intent(this,Belajar.class));
        } else if (view == cardKamus) {
            startActivity(new Intent(this,Kamus.class));
        } else if (view == cardTebakGambar) {
            startActivity(new Intent(this,TGambar.class));
        } else if (view == cardUjian) {
            startActivity(new Intent(this,Ujian.class));
//        } else if (view == btKeluar) {
//            SharedPrefManager.getInstance(getApplicationContext()).logout();
//            finish();
        } else if(view == cardProfil) {
            showMenu(view);
        }
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profil);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                return true;
            case R.id.keluar:
                System.exit(0);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}