package com.example.financemanager.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.financemanager.Adapters.TicketListAdapter;
import com.example.financemanager.Constants;
import com.example.financemanager.Models.TicketModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.financemanager.Constants.Url.EVENTS;
import static com.example.financemanager.Constants.Url.TICKETS;

public class TicketListService {

    private Context context;
    private SharedPreferences preferences;
    private ArrayList<TicketModel> tickets;

    public TicketListService(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
        tickets = new ArrayList<>();
    }

    public StringRequest list(final ListView ticketListView) {
        final StringRequest request = new StringRequest(Request.Method.GET, TICKETS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; jsonArray.length() > i; i++) {
                        JSONObject json = (JSONObject) jsonArray.get(i);
                        parseJson(json, i);
                    }

                    TicketListAdapter adapter = new TicketListAdapter(context, tickets);
                    ticketListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (Throwable ex) {
                    Log.d("JSONException", ex.getMessage());
                }
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

    public ArrayList<TicketModel> getTickets() {
        return tickets;
    }

    private void parseJson(JSONObject json, int i) throws JSONException {

        TicketModel ticket = new TicketModel();
        ticket.setId(json.getString("id"));
        ticket.setEventId(json.getString("eventId"));
        ticket.setEventName(json.getString("eventName"));
        ticket.setPrice(Double.parseDouble(json.getString("price")));
        ticket.setSeating(json.getInt("seating"));
        ticket.setPurchasedAt(json.getString("purchasedAt"));

        this.tickets.add(i, ticket);
    }
}
