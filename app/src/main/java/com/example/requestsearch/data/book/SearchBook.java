package com.example.requestsearch.data.book;

import java.util.ArrayList;

public class SearchBook {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private ArrayList<BookItems> items;


    public SearchBook(String lastBuildDate, int total, int start, int display,ArrayList<BookItems> items ) {
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

    public ArrayList<BookItems> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total=" + total +
                ", start=" + start +
                ", display=" + display +
                ", items=" + items +
                '}';
    }
}
