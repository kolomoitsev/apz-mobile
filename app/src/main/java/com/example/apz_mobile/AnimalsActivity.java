package com.example.apz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimalsActivity extends AppCompatActivity {

    public ArrayList<String> animals = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        String token = pref.getString("token", "");
        String userId = pref.getString("id", "");

        String url = "http://10.0.2.2:3001/animal/";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    // response
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String taskName = obj.getString("animalName");
                            animals.add(taskName);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }

                    ListView listView = findViewById(R.id.listView);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, animals);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        try {

                            Intent intent = new Intent(this, SingleAnimalActivity.class);

                            SharedPreferences.Editor editor = pref.edit();

                            JSONObject obj = response.getJSONObject(position);
                            String animalId = obj.getString("_id");
                            editor.putString("animalId", animalId);
                            editor.apply();

                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    listView.setAdapter(adapter);

                },
                error -> Log.d("ERROR", "error => " + error.toString())

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