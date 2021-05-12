package com.example.requestsearch.data.book;

import java.util.ArrayList;

/***
 * 책 전체 검색 결과 데이터
 */
public class SearchBookVO {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private ArrayList<BookItemsVO> items;


    public SearchBookVO(String lastBuildDate, int total, int start, int display, ArrayList<BookItemsVO> items ) {
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

    public ArrayList<BookItemsVO> getItems() {
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
