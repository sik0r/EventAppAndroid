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
import com.example.financemanager.Adapters.EventListAdapter;
import com.example.financemanager.Constants;
import com.example.financemanager.Models.EventModel;
import com.example.financemanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.financemanager.Constants.Url.EVENTS_LIST;

public class EventListService {

    private Context context;
    private SharedPreferences preferences;
    ArrayList<EventModel> events;

    public EventListService(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
        events = new ArrayList<>();
    }

    public StringRequest list(final ListView eventListView) {
        final StringRequest request = new StringRequest(Request.Method.GET, EVENTS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; jsonArray.length() > i; i++) {
                        JSONObject json = (JSONObject) jsonArray.get(i);
                        parseJson(json, i);
                    }

                    EventListAdapter adapter = new EventListAdapter(context, events);
                    eventListView.setAdapter(adapter);
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

    public ArrayList<EventModel> getEvents() {
        return events;
    }

    private void parseJson(JSONObject json, int i) throws JSONException {

        EventModel event = new EventModel();
        event.setId(json.getString("id"));
        event.setName(json.getString("name"));
        event.setDescription(json.getString("description"));
        event.setStartDate(json.getString("startDate"));
        event.setEndDate(json.getString("endDate"));
        event.setAvailableTicketsCount(json.getInt("availableTicketsCount"));
        event.setPurchasedTicketsCount(json.getInt("purchasedTicketsCount"));
        event.setTickerPrice(json.getInt("ticketPrice"));

        this.events.add(i, event);
    }
}
