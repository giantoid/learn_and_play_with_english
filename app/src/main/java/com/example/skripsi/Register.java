package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.skripsi.core.EndPoint;
import com.example.skripsi.core.SharedPrefManager;
import com.example.skripsi.core.VolleySingleton;
import com.example.skripsi.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_username, txt_password, txt_confirm_password;
    private Button btn_register, btn_login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);

        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void register() {
        final String username = txt_username.getText().toString().trim();
        final String password = txt_password.getText().toString().trim();
        final String confirm_password = txt_confirm_password.getText().toString().trim();

        //validations

        if (TextUtils.isEmpty(username)) {
            txt_username.setError("Silahkan masukkan username");
            txt_username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txt_password.setError("Silahkan masukkan password");
            txt_password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirm_password)) {
            txt_confirm_password.setError("Silahkan konfirmasi password");
            txt_confirm_password.requestFocus();
            return;
        }

        if (!TextUtils.equals(password,confirm_password)) {
            txt_confirm_password.setError("Password tidak sama");
            txt_confirm_password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoint.API+"register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id_user"),
                                        userJson.getString("username")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            register();
        } else if (v == btn_login) {
            startActivity(new Intent(this, Login.class));
        }
    }
}