package com.example.financemanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.financemanager.Models.TicketModel;
import com.example.financemanager.R;

import java.util.ArrayList;

public class TicketListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TicketModel> tickets;

    public TicketListAdapter(Context context, ArrayList<TicketModel> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.ticket_list_item, null);

        TextView name = view.findViewById(R.id.eventName);
        TextView seating = view.findViewById(R.id.seating);
        TextView purchasedAt = view.findViewById(R.id.purchasedAt);

        name.setText(tickets.get(position).getEventName());
        seating.setText(String.valueOf(tickets.get(position).getSeating()));
        purchasedAt.setText(tickets.get(position).getPurchasedAt());

        return view;
    }
}
