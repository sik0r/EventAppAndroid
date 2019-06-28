package com.example.financemanager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.EventModel;
import com.example.financemanager.Services.ShowEventService;

public class ShowEventActivity extends AppCompatActivity {

    TextView name;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView availableTicket;
    TextView ticketPrice;
    Button purchase;
    Button delete;
    RequestQueue queue;
    SharedPreferences preferences;
    EventModel eventModel;
    ShowEventService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        service = new ShowEventService(getBaseContext(), this.getSharedPreferences("string", Context.MODE_PRIVATE));
        eventModel = (EventModel) getIntent().getSerializableExtra("event");
        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        initViewItems();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(service.delete(eventModel.getId()));
            }
        });

        if (eventModel.hasTickets()) {
            purchase.setVisibility(View.VISIBLE);

            purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepareDialog();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), EventListActivity.class);
        startActivity(intent);
    }

    private void initViewItems() {
        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        availableTicket = (TextView) findViewById(R.id.tickets);
        ticketPrice = (TextView) findViewById(R.id.price);
        purchase = (Button) findViewById(R.id.purchase);
        delete = (Button) findViewById(R.id.delete);
        purchase.setVisibility(View.INVISIBLE);

        name.setText(eventModel.getName());
        description.setText(eventModel.getDescription());
        startDate.setText(eventModel.getStartDate());
        endDate.setText(eventModel.getEndDate());
        availableTicket.setText(String.valueOf(eventModel.getAvailableTicketsCount()));
        ticketPrice.setText(String.valueOf(eventModel.getTickerPrice()));
    }

    private void prepareDialog() {
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

                if (count > eventModel.getAvailableTicketsCount()) {
                    Toast.makeText(getBaseContext(), "Not enought available tickets to purchase.", Toast.LENGTH_LONG).show();
                } else {
                    queue.add(service.purchase(eventModel, count, availableTicket));
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}
