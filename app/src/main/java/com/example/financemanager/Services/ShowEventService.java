package com.example.financemanager.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.financemanager.Constants;
import com.example.financemanager.EventListActivity;
import com.example.financemanager.Models.EventModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.financemanager.Constants.Url.EVENTS_LIST;

public class ShowEventService {

    private Context context;
    private SharedPreferences preferences;

    public ShowEventService(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    public StringRequest delete(String eventId) {

        final StringRequest request = new StringRequest(Request.Method.DELETE, EVENTS_LIST + "/" + eventId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(context, EventListActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "Event deleted", Toast.LENGTH_LONG).show();
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

    public JsonObjectRequest purchase(final EventModel eventModel, final int count, final TextView availableTicket) {
        JSONObject json = prepareJson(eventModel.getId(), count);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.Url.PURCHASE_TICKETS, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int available = eventModel.getAvailableTicketsCount() - count;
                        eventModel.setAvailableTicketsCount(available);
                        availableTicket.setText(String.valueOf(available));

                        Toast.makeText(context, "Ticket purchased", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError", error.toString());
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

    private JSONObject prepareJson(String eventId, int amount) {
        JSONObject json = new JSONObject();

        try {
            json.put("EventId", eventId);
            json.put("amount", amount);
        } catch (JSONException ex) {
            Log.e("ERROR JSONException", ex.getMessage(), ex);
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return json;
    }
}
