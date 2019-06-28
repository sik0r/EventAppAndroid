package com.example.financemanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.financemanager.Models.EventModel;
import com.example.financemanager.R;

import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EventModel> events;

    public EventListAdapter(Context context, ArrayList<EventModel> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return this.events.size();
    }

    @Override
    public Object getItem(int position) {
        return this.events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.event_list_item, null);

        TextView name = view.findViewById(R.id.eventTitile);
        TextView availableTickets = view.findViewById(R.id.availableTickets);
        TextView eventStartDate = view.findViewById(R.id.eventStartDate);
        TextView eventEndDate = view.findViewById(R.id.eventEndDate);

        name.setText(events.get(position).getName());
        availableTickets.setText(String.valueOf(events.get(position).getAvailableTicketsCount()));
        eventStartDate.setText(events.get(position).getStartDate());
        eventEndDate.setText(events.get(position).getEndDate());

        return view;
    }
}
