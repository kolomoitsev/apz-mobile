package com.example.apz_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SingleAnimalActivity extends AppCompatActivity {

    public void addNewAnimal(View view) {
        Intent intent = new Intent(this, NewAnimalActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_animal);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        String token = pref.getString("token", "");
        String animalId = pref.getString("animalId", "");
        String url = "http://10.0.2.2:3001/animal/" + animalId;

        TextView nameField = findViewById(R.id.nameId);
        TextView rfidField = findViewById(R.id.rfidId);
        TextView statusField = findViewById(R.id.statusId);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        nameField.append(" " + response.getString("animalName"));
                        rfidField.append(" " + response.getString("animalRfdi"));
                        statusField.append(" " + response.getString("animalStatus"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> Log.d("ERROR_ANIMAL", "error => " + error.toString())

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String bearerHeader = "Bearer " + token;
                params.put("Authorization", bearerHeader);
                return params;
            }
        };

        queue.add(getRequest);

    }
}