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

import java.util.HashMap;
import java.util.Map;

public class NewAnimalActivity extends AppCompatActivity {

    public void onAnimalSave(View view) {
        EditText animalName = findViewById(R.id.animalNameid);
        EditText animalRfid = findViewById(R.id.animalRfidId);

        String animalNamePost = animalName.getText().toString();
        String animalRfidPost = animalRfid.getText().toString();

        Intent intent = new Intent(this, AnimalsActivity.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        String token = pref.getString("token", "");
        String reservationId = pref.getString("reservationId", "");

        String url = "http://10.0.2.2:3001/animal/";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("reservationId", reservationId);
            postData.put("animalName", animalNamePost);
            postData.put("animalRfdi", animalRfidPost);
            postData.put("animalStatus", "Active");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, response -> {

            Toast.makeText(getApplicationContext(), "Animal added", Toast.LENGTH_SHORT)
                    .show();
            startActivity(intent);

        }, error -> {
            error.printStackTrace();

            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT)
                    .show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String bearerHeader = "Bearer " + token;
                params.put("Authorization", bearerHeader);

                return params;
            }
        };
        ;

        requestQueue.add(jsonObjectRequest);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_animal);
    }
}