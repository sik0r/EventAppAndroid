package com.example.financemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.TicketModel;
import com.example.financemanager.Services.TicketListService;

import java.io.Serializable;

public class TicketListActivity extends AppCompatActivity {

    RequestQueue queue;
    SharedPreferences preferences;
    ListView ticketListView;
    TicketListService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        this.ticketListView = (ListView) findViewById(R.id.ticketsList);
        service = new TicketListService(getBaseContext(), preferences);

        queue = Volley.newRequestQueue(this);
        queue.add(service.list(ticketListView));

        ticketListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TicketModel ticket = service.getTickets().get(position);
                Intent intent = new Intent(getBaseContext(), ShowTicketActivity.class);
                intent.putExtra("ticket", (Serializable) ticket);
                startActivity(intent);
            }
        });
    }
}
