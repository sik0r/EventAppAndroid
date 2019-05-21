package com.example.financemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.financemanager.Constants.Url.EVENTS_LIST;

public class EventListActivity extends AppCompatActivity {

    RequestQueue queue;
    SharedPreferences preferences;
    ArrayList<EventModel> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        init();

        final StringRequest request = new StringRequest(Request.Method.GET, EVENTS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; jsonArray.length() > i; i++) {
                        JSONObject json = (JSONObject) jsonArray.get(i);
                        parseJson(json);
                    }
                } catch (Throwable ex) {
                    Log.d("JSONException", ex.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("===================", "DDDDDDDDDDDDDDDDDDddaw");
                        Log.d("JSONE ErrorListener", error.getMessage());
                    }
                }
        );

        queue.add(request);
    }

    private void init() {
        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
    }

    private void parseJson(JSONObject json) throws JSONException {
        EventModel event = new EventModel();
        event.setId(json.getString("id"));
        event.setName(json.getString("name"));

        events.add(event);
    }
}
