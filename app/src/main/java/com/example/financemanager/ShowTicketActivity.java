package com.example.financemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.TicketModel;
import com.example.financemanager.Services.ShowTicketService;

public class ShowTicketActivity extends AppCompatActivity {

    TextView eventName;
    TextView purchasedAt;
    TextView seating;
    TextView price;
    Button cancel;

    ShowTicketService service;
    RequestQueue queue;
    SharedPreferences preferences;
    TicketModel ticketModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);

        service = new ShowTicketService(getBaseContext(), this.getSharedPreferences("string", Context.MODE_PRIVATE));
        ticketModel = (TicketModel) getIntent().getSerializableExtra("ticket");
        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        initViewItems();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(service.delete(ticketModel.getEventId(), ticketModel.getId()));
            }
        });
    }

    private void initViewItems() {
        eventName = (TextView) findViewById(R.id.eventName);
        purchasedAt = (TextView) findViewById(R.id.purchasedAt);
        seating = (TextView) findViewById(R.id.seating);
        price = (TextView) findViewById(R.id.price);
        cancel = (Button) findViewById(R.id.cancelTicket);

        eventName.setText(ticketModel.getEventName());
        purchasedAt.setText(ticketModel.getPurchasedAt());
        seating.setText(String.valueOf(ticketModel.getSeating()));
        price.setText(String.valueOf(ticketModel.getPrice()));
    }
}
