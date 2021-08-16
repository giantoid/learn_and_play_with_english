package com.example.skripsi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.skripsi.Database.UjianDB;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.SharedPrefManager;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.MUjian;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SoalUjian extends AppCompatActivity {
    public static final String EXTRA_SCORE = "skor";
    private static final long COUNTDOWN_IN_MILLIS = 60000;

    private TextView textViewQuestion, textViewScore, textViewQuestionCount, textViewCountDown;

    private Button buttonConfirmNext, bt1, bt2, bt3, bt4;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private List<MUjian> questionList;
    private int questionCountTotal;
    private MUjian currentQuestion;
    private int questionCounter;
    private boolean isAnswered;
    private int score;
    private String id_user;

    private ImageView imageView;

    private ProgressBar progressBar;

    private SharedPrefManager shp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_ujian);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        bt1 = findViewById(R.id.tb1);
        bt2 = findViewById(R.id.tb2);
        bt3 = findViewById(R.id.tb3);
        bt4 = findViewById(R.id.tb4);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imgSoal);

        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        UjianDB dbHelper = new UjianDB(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();
        shp = shp.getInstance(this);

        id_user = String.valueOf(shp.getUser().getId());

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnswered) {
                    if (shp.getJawaban().equals("a"))
                    {
                        checkAnswer();
                    }
                    else if (shp.getJawaban().equals("b"))
                    {
                        checkAnswer();
                    }
                    else if (shp.getJawaban().equals("c"))
                    {
                        checkAnswer();
                    }
                    else if (shp.getJawaban().equals("d"))
                    {
                        checkAnswer();
                    }
                    if (shp.getJawaban().equals("")) {
                        bt1.setEnabled(true);
                        bt2.setEnabled(true);
                        bt3.setEnabled(true);
                        bt4.setEnabled(true);
                    }
                } else {
                    checkAnswer();
                }
//                isAnswered = !isAnswered;
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setEnabled(false);
                bt2.setEnabled(true);
                bt3.setEnabled(true);
                bt4.setEnabled(true);
                shp.setJawaban("a");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setEnabled(true);
                bt2.setEnabled(false);
                bt3.setEnabled(true);
                bt4.setEnabled(true);
                shp.setJawaban("b");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setEnabled(true);
                bt2.setEnabled(true);
                bt3.setEnabled(false);
                bt4.setEnabled(true);
                shp.setJawaban("c");
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setEnabled(true);
                bt2.setEnabled(true);
                bt3.setEnabled(true);
                bt4.setEnabled(false);
                shp.setJawaban("d");
            }
        });

    }

    private void checkAnswer() {
//            answered = true;
        countDownTimer.cancel();

        if(currentQuestion.getAnswer().equals(shp.getJawaban()))
        {
//            Toast.makeText(SoalUjian.this, "Jawaban Benar", Toast.LENGTH_SHORT).show();
            score++;
            textViewScore.setText("Score: "+score);
            postAnswer(currentQuestion.getId_soal(), id_user, shp.getJawaban(),"1");
            shp.delJawaban();

            showNextQuestion();
        }
        else {
//            Toast.makeText(SoalUjian.this, "Jawaban Salah", Toast.LENGTH_SHORT).show();
            postAnswer(currentQuestion.getId_soal(), id_user, shp.getJawaban(),"0");
            shp.delJawaban();

            showNextQuestion();
        }
    }

    private void showNextQuestion() {
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            if (!currentQuestion.getGambar().equals("")){
                Glide.with(SoalUjian.this).load(EndPoint.GAMBAR_UJIAN+currentQuestion.getGambar()).into(imageView);
            }
            textViewQuestion.setText(currentQuestion.getQuestion());
            bt1.setText("a. "+currentQuestion.getA());
            bt2.setText("b. "+currentQuestion.getB());
            bt3.setText("c. "+currentQuestion.getC());
            bt4.setText("d. "+currentQuestion.getD());
            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
//            answered = false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);

        shp.delJawaban();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void postAnswer(final String id_soal, final String id_user, final String jawaban, final String status){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoint.API+"jawab",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_soal", id_soal);
                params.put("id_user", id_user);
                params.put("jawaban", jawaban);
                params.put("status", status);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}