package com.example.financemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.EventModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    Button save;
    EditText name;
    EditText description;
    EditText startDate;
    EditText endDate;
    EditText price;
    EditText count;
    RequestQueue queue;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        save = (Button) findViewById(R.id.saveBtn);

        name = (EditText) findViewById(R.id.eventName);
        description = (EditText) findViewById(R.id.eventDescription);
        startDate = (EditText) findViewById(R.id.eventStart);
        endDate = (EditText) findViewById(R.id.eventEnd);
        price = (EditText) findViewById(R.id.eventPrice);
        count = (EditText) findViewById(R.id.eventTickets);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = prepareJson(createModel());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.Url.CREATE_EVENT, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json = new JSONObject(response.toString());

                                    Intent intent = new Intent(getBaseContext(), EventListActivity.class);
                                    startActivity(intent);
                                } catch (JSONException ex) {
                                    Toast.makeText(CreateEventActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                JSONObject response = null;
                                try {
                                    response = new JSONObject(new String(error.networkResponse.data));
                                    Log.d("API ERROR", response.toString());
                                    Toast.makeText(CreateEventActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + preferences.getString(Constants.JWT_NAME, "xx"));

                        return params;
                    }
                };

                queue.add(request);
            }
        });

    }

    private EventModel createModel() {
        EventModel model = new EventModel();

        model.setName(name.getText().toString());
        model.setDescription(description.getText().toString());
        model.setStartDate(startDate.getText().toString());
        model.setEndDate(endDate.getText().toString());
        model.setTickerPrice(Double.parseDouble(price.getText().toString()));
        model.setTicketsCount(Integer.parseInt(count.getText().toString()));

        return model;
    }

    private JSONObject prepareJson(EventModel model) {
        JSONObject json = new JSONObject();

        try {
            json.put("name", model.getName());
            json.put("description", model.getDescription());
            json.put("startDate", model.getStartDate());
            json.put("endDate", model.getEndDate());
            json.put("tickets", model.getTicketsCount());
            json.put("Price", model.getTickerPrice());
        } catch (JSONException ex) {
            Log.e("ERROR JSONException", ex.getMessage(), ex);
            Toast.makeText(CreateEventActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return json;
    }
}
