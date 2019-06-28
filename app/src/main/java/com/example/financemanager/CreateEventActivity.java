package com.example.financemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.financemanager.Models.EventModel;
import com.example.financemanager.Services.CreateEventService;

public class CreateEventActivity extends AppCompatActivity {

    Button save;
    EditText name;
    EditText description;
    EditText startDate;
    EditText endDate;
    EditText price;
    EditText count;
    RequestQueue queue;
    SharedPreferences preferences;
    CreateEventService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        queue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("string", Context.MODE_PRIVATE);
        service = new CreateEventService(getBaseContext(), preferences);

        save = (Button) findViewById(R.id.saveBtn);
        name = (EditText) findViewById(R.id.eventName);
        description = (EditText) findViewById(R.id.eventDescription);
        startDate = (EditText) findViewById(R.id.eventStart);
        endDate = (EditText) findViewById(R.id.eventEnd);
        price = (EditText) findViewById(R.id.eventPrice);
        count = (EditText) findViewById(R.id.eventTickets);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(service.create(createModel()));
            }
        });

    }

    private EventModel createModel() {
        EventModel model = new EventModel();

        model.setName(name.getText().toString());
        model.setDescription(description.getText().toString());
        model.setStartDate(startDate.getText().toString());
        model.setEndDate(endDate.getText().toString());
        model.setTickerPrice(Double.parseDouble(price.getText().toString()));
        model.setTicketsCount(Integer.parseInt(count.getText().toString()));

        return model;
    }
}
