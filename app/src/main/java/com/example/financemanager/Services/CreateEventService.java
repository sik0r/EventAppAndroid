package com.example.financemanager.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.financemanager.Constants;
import com.example.financemanager.EventListActivity;
import com.example.financemanager.Models.EventModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateEventService {

    private Context context;
    private SharedPreferences preferences;

    public CreateEventService(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    public JsonObjectRequest create(EventModel model) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.Url.EVENTS, prepareJson(model),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Event created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, EventListActivity.class);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JSONObject response = null;
                        try {
                            response = new JSONObject(new String(error.networkResponse.data));
                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + preferences.getString(Constants.JWT_NAME, "xx"));

                return params;
            }
        };

        return request;
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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return json;
    }
}
