package com.example.apz_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public void SignIn(View view) {
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);

        String emailPost = emailField.getText().toString();
        String passwordPost = passwordField.getText().toString();

        Intent intent = new Intent(this, SecondActivity.class);

        String url = "http://10.0.2.2:3001/user/login";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("userEmail", emailPost);
            postData.put("userPassword", passwordPost);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, response -> {

            try {
                JSONObject tokens = response.getJSONObject("tokens");

                String token = tokens.getString("accessToken");
                String refreshToken = tokens.getString("refreshToken");
                String email = response.getString("userEmail");
                String id = response.getString("_id");

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("token", token);
                editor.apply();
                editor.putString("refreshToken", refreshToken);
                editor.apply();
                editor.putString("email", email);
                editor.apply();
                editor.putString("id", id);
                editor.apply();

                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Bad credentials", Toast.LENGTH_SHORT)
                        .show();
            }
        }, error -> {
            error.printStackTrace();

            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT)
                    .show();
        });

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

}