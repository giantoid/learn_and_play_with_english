package com.example.skripsi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.skripsi.Database.TebakGambarDB;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.SharedPrefManager;
import com.example.skripsi.models.MTebakGambar;

import java.util.ArrayList;
import java.util.List;

public class TebakGambar extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_SCORE = "skor";

    private List<MTebakGambar> questionList;
    private int questionCountTotal;
    private MTebakGambar currentQuestion;
    private int questionCounter = 0;
    private int score;

    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private ImageView imgTb;
    private CardView btMic;
    private EditText txtJawab;
    private TextView txtIndo;
    private ImageView mic;
    private Button btNext;
    private SpeechRecognizer speechRecognizer;
    int RecordAudioRequestCode = 750;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tebak_gambar);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        imgTb = findViewById(R.id.imgTb);
        txtJawab = findViewById(R.id.txtJawab);
        txtIndo = findViewById(R.id.txtIndo);
        btMic = findViewById(R.id.btMic);
        mic = findViewById(R.id.mic);
        btNext = findViewById(R.id.btNext);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        btNext.setOnClickListener(this);

        TebakGambarDB dbHelper = new TebakGambarDB(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
//        Collections.shuffle(questionList);

        showQuestion();

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                txtJawab.setText("");
                txtJawab.setHint("Mendengarkan...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Log.d("Voice", "onError: "+i);
                speechRecognizer.cancel();
                btMic.setCardBackgroundColor(Color.rgb(18, 165, 241));
                txtJawab.setHint("Tahan tombol mikrofon untuk merekm");
//                mic.setImageResource(R.drawable.ic_baseline_mic_24);
            }

            @Override
            public void onResults(Bundle bundle) {
                btMic.setCardBackgroundColor(Color.rgb(18, 165, 241));
//                mic.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                float[] scores = bundle.getFloatArray(android.speech.SpeechRecognizer.CONFIDENCE_SCORES);
                txtJawab.setText(data.get(0));
                checkAnswer();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                btMic.setCardBackgroundColor(Color.rgb(18, 165, 241));
//                mic.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                txtJawab.setText(data.get(0));
                checkAnswer();
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        btMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    btMic.setCardBackgroundColor(Color.RED);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAnswer() {

        if(currentQuestion.getEnglish().toLowerCase().equals(txtJawab.getText().toString().toLowerCase()))
        {
            showAlertDialogWithAutoDismiss("Jawaban benar", "Silahkan melanjutkan ke soal selanjutnya. :)");
            btNext.setVisibility(View.VISIBLE);
            btMic.setVisibility(View.GONE);
            SharedPrefManager.getInstance(this).setLevel(String.valueOf(questionCounter));
        }
        else {
            showAlertDialogWithAutoDismiss("Jawaban salah", "Silahkan coba lagi dan tetap semangat ya. :)");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private void showQuestion() {
        Intent intent = getIntent();
            currentQuestion = questionList.get(Integer.parseInt(intent.getStringExtra("id")));
            Glide.with(TebakGambar.this).load(EndPoint.GAMBAR+currentQuestion.getGambar()).into(imgTb);
            txtIndo.setText(currentQuestion.getIndonesia());
            questionCounter = Integer.parseInt(intent.getStringExtra("id"))+1;
            Log.d("d",String.valueOf(questionCounter));
    }

    private void showNextQuestion() {
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            Glide.with(TebakGambar.this).load(EndPoint.GAMBAR+currentQuestion.getGambar()).into(imgTb);
            txtIndo.setText(currentQuestion.getIndonesia());
            Log.d("d",String.valueOf(questionCounter));
            questionCounter++;
        } else {
            finishQuiz();
        }
    }

    @Override
    public void onClick(View view) {
        showNextQuestion();
        btMic.setVisibility(View.VISIBLE);
        btNext.setVisibility(View.GONE);
        txtJawab.setText("");
        txtJawab.setHint("Tahan tombol mikrofon untuk merekam");
    }

    public void showAlertDialogWithAutoDismiss(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TebakGambar.this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        }, 2000); //change 5000 with a specific time you want
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id","1");
        setResult(RESULT_OK, intent);
        finish();
    }
}
