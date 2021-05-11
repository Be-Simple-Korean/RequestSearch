package com.example.requestsearch.data.movie;

import java.util.ArrayList;

public class SearchMovie {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private ArrayList<MovieItems> items;

    public SearchMovie(String lastBuildDate, int total, int start, int display, ArrayList<MovieItems> items) {
        this.lastBuildDate = lastBuildDate;
        this.total = total;
        this.start = start;
        this.display = display;
        this.items = items;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public int getStart() {
        return start;
    }

    public int getDisplay() {
        return display;
    }

    public ArrayList<MovieItems> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total=" + total +
                ", start=" + start +
                ", display=" + display +"\n"+
                ", items=" + items +
                '}';
    }
}
