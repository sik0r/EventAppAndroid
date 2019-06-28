package com.example.financemanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ShowEventActivity extends AppCompatActivity {

    TextView name;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView availableTicket;
    TextView ticketPrice;
    Button purchase;
    RequestQueue queue;
    SharedPreferences preferences;
    EventModel eventModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        eventModel = (EventModel) getIntent().getSerializableExtra("event");

        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);

        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        availableTicket = (TextView) findViewById(R.id.tickets);
        ticketPrice = (TextView) findViewById(R.id.price);
        purchase = (Button) findViewById(R.id.purchase);
        purchase.setVisibility(View.INVISIBLE);

        name.setText(eventModel.getName());
        description.setText(eventModel.getDescription());
        startDate.setText(eventModel.getStartDate());
        endDate.setText(eventModel.getEndDate());
        availableTicket.setText(String.valueOf(eventModel.getAvailableTicketsCount()));
        ticketPrice.setText(String.valueOf(eventModel.getTickerPrice()));

        if (eventModel.hasTickets()) {
            purchase.setVisibility(View.VISIBLE);

            purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(ShowEventActivity.this);
                    dialog.setTitle("Purchase tickets.");
                    dialog.setContentView(R.layout.purchase_ticket_dialog);

                    Button ok = (Button) dialog.findViewById(R.id.ok);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText etCount = (EditText) dialog.findViewById(R.id.count);
                            final int count = Integer.parseInt(etCount.getText().toString());

                            JSONObject json = prepareJson(count);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.Url.PURCHASE_TICKETS, json,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            int available = eventModel.getAvailableTicketsCount() - count;
                                            eventModel.setAvailableTicketsCount(available);
                                            availableTicket.setText(String.valueOf(available));
                                            dialog.dismiss();

                                            Toast.makeText(ShowEventActivity.this, "Ticket purchased", Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            JSONObject response = null;
                                            try {
                                                response = new JSONObject(new String(error.networkResponse.data));
                                                Toast.makeText(ShowEventActivity.this, response.toString(), Toast.LENGTH_LONG).show();
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

                            queue.add(request);
                        }
                    });

                    dialog.show();
                }
            });
        }
    }

    private JSONObject prepareJson(int amount) {
        JSONObject json = new JSONObject();

        try {
            json.put("EventId", eventModel.getId());
            json.put("amount", amount);
        } catch (JSONException ex) {
            Log.e("ERROR JSONException", ex.getMessage(), ex);
            Toast.makeText(ShowEventActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return json;
    }
}
