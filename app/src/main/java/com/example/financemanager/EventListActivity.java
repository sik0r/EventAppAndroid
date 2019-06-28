package com.example.financemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Adapters.EventListAdapter;
import com.example.financemanager.Models.EventModel;
import com.example.financemanager.Services.EventListService;

import java.io.Serializable;

public class EventListActivity extends AppCompatActivity {

    RequestQueue queue;
    SharedPreferences preferences;
    ListView eventListView;
    EventListAdapter adapter;
    EventListService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        this.eventListView = (ListView) findViewById(R.id.eventList);
        service = new EventListService(getBaseContext(), preferences);

        queue = Volley.newRequestQueue(this);
        queue.add(service.list(eventListView));

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventModel eventModel = service.getEvents().get(position);
                Intent intent = new Intent(getBaseContext(), ShowEventActivity.class);
                intent.putExtra("event", (Serializable) eventModel);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_list_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.createEventBtn:
                Intent intent = new Intent(getBaseContext(), CreateEventActivity.class);
                startActivity(intent);
                return true;
            case R.id.my_tickets:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
