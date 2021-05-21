package com.example.requestsearch.network.data.movie;

import com.example.requestsearch.network.data.BaseVO;

import java.util.ArrayList;

/**
 * 영화 전체 검색 데이터
 */
public class SearchMovieVO extends BaseVO {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private ArrayList<MovieItemsVO> items;

    public SearchMovieVO(String lastBuildDate, int total, int start, int display, ArrayList<MovieItemsVO> items) {
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

    public ArrayList<MovieItemsVO> getItems() {
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
