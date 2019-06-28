package com.example.financemanager.Models;

import java.io.Serializable;

public class EventModel implements Serializable {

    private String id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private int ticketsCount;
    private int purchasedTicketsCount;
    private int availableTicketsCount;
    private double tickerPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(int ticketsCount) {
        this.ticketsCount = ticketsCount;
    }

    public int getPurchasedTicketsCount() {
        return purchasedTicketsCount;
    }

    public void setPurchasedTicketsCount(int purchasedTicketsCount) {
        this.purchasedTicketsCount = purchasedTicketsCount;
    }

    public int getAvailableTicketsCount() {
        return availableTicketsCount;
    }

    public boolean hasTickets() {
        return availableTicketsCount > 0;
    }

    public void setAvailableTicketsCount(int availableTicketsCount) {
        this.availableTicketsCount = availableTicketsCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getTickerPrice() {
        return tickerPrice;
    }

    public void setTickerPrice(double tickerPrice) {
        this.tickerPrice = tickerPrice;
    }
}
