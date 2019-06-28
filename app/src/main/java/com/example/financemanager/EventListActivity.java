package com.example.financemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Adapters.EventListAdapter;
import com.example.financemanager.Models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.financemanager.Constants.Url.EVENTS_LIST;

public class EventListActivity extends AppCompatActivity {

    RequestQueue queue;
    SharedPreferences preferences;
    ArrayList<EventModel> events = new ArrayList<>();
    ListView eventListView;
    EventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        this.eventListView = (ListView) findViewById(R.id.eventList);

        final StringRequest request = new StringRequest(Request.Method.GET, EVENTS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; jsonArray.length() > i; i++) {
                        JSONObject json = (JSONObject) jsonArray.get(i);
                        parseJson(json, i);
                    }

                    adapter = new EventListAdapter(getBaseContext(), events);
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
                        Log.d("===================", "DDDDDDDDDDDDDDDDDDddaw");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                Log.d("TOKEN===", preferences.getString(Constants.JWT_NAME, "xx"));
                params.put("Authorization", "Bearer " + preferences.getString(Constants.JWT_NAME, "xx"));

                return params;
            }
        };

        queue = Volley.newRequestQueue(this);
        queue.add(request);


        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventModel eventModel = events.get(position);

                final StringRequest getEventRequest = new StringRequest(Request.Method.GET, EVENTS_LIST + "/" + eventModel.getId(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("====", jsonObject.getString("id"));
                        } catch (Throwable ex) {
                            Log.d("JSONException", ex.getMessage());
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse.statusCode == 401) {
                                    Toast.makeText(EventListActivity.this, R.string.invalid_credentials, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
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

                queue.add(getEventRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_list_menu, menu);

        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){

            case R.id.createEventBtn:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    private void parseJson(JSONObject json, int i) throws JSONException {

        EventModel event = new EventModel();
        event.setId(json.getString("id"));
        event.setName(json.getString("name"));
        event.setStartDate(json.getString("startDate"));
        event.setEndDate(json.getString("endDate"));
        event.setAvailableTicketsCount(json.getInt("availableTicketsCount"));
        event.setPurchasedTicketsCount(json.getInt("purchasedTicketsCount"));

        Log.d("EVENT NAME", event.getName());

        this.events.add(i, event);
    }
}
