package com.example.financemanager.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.financemanager.Constants;
import com.example.financemanager.TicketListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.financemanager.Constants.Url.TICKETS;

public class ShowTicketService {
    private Context context;
    private SharedPreferences preferences;

    public ShowTicketService(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    public StringRequest delete(String eventId, String ticketId) {

        final StringRequest request = new StringRequest(Request.Method.DELETE, TICKETS + "/" + eventId + "/" + ticketId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(context, TicketListActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "Ticket canceled", Toast.LENGTH_LONG).show();
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
}
