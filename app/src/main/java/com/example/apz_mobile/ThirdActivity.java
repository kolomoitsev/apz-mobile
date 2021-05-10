package com.example.apz_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

public class ThirdActivity extends AppCompatActivity {

    public void setDone(View view) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        String token = pref.getString("token", "");
        String taskId = pref.getString("taskId", "");

        String url = "http://10.0.2.2:3001/task/done/" + taskId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.PATCH, url, null,
                response -> {
                    Intent intent = new Intent(this, SecondActivity.class);
                    Log.d("Done", response.toString());
                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                },
                error -> {
                    Log.d("ERROR", "error => " + error.toString());
                    Toast.makeText(getApplicationContext(), "Not successful", Toast.LENGTH_LONG).show();
                }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        TextView taskName = findViewById(R.id.taskNameId);
        TextView taskDescription = findViewById(R.id.taskDescriptionId);
        TextView taskLength = findViewById(R.id.taskLengthId);
        TextView taskStatus = findViewById(R.id.taskStatusId);
        TextView taskDeadline = findViewById(R.id.taskDeadlineId);
        TextView taskCreator = findViewById(R.id.taskCreatedById);
        TextView taskCreatorEmail = findViewById(R.id.taskCreatedEmailId);
        TextView taskCreatorPhone = findViewById(R.id.taskCreatedPhoneId);
        TextView taskCategoryName = findViewById(R.id.taskCategoryId);
        TextView taskCategoryDescription = findViewById(R.id.taskCategoryDescriptionId);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        String token = pref.getString("token", "");
        String taskId = pref.getString("taskId", "");

        String url = "http://10.0.2.2:3001/task/exact/" + taskId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        JSONObject creator = response.getJSONObject("creator");
                        JSONObject task = response.getJSONObject("task");
                        JSONObject category = response.getJSONObject("category");

                        taskName.append(" " + task.getString("taskName"));
                        taskDescription.append(" " + task.getString("taskDescription"));
                        taskStatus.append(" " + task.getString("taskStatus"));
                        taskLength.append(" " + task.getString("taskLength") + " minutes");
                        taskDeadline.append(" " + task.getString("taskDeadline"));

                        taskCreator.append(" " + creator.getString("userName") + " " + creator.getString("userLastName"));
                        taskCreatorEmail.append(" " + creator.getString("userEmail"));
                        taskCreatorPhone.append(" " + creator.getString("userPhone"));

                        taskCategoryName.append(" " + category.getString("taskCategoryName"));
                        taskCategoryDescription.append(" " + category.getString("taskCategoryDescription"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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