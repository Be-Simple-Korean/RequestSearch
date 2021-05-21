package com.example.requestsearch.network.data.movie;

/**
 * 영화 장르 항목 데이터
 */
public class MovieGenreDataVO {

    private String gName;

    private boolean isSelected = false;

    public MovieGenreDataVO(String gName, boolean isSelected) {
        this.gName = gName;
        this.isSelected = isSelected;
    }

    public String getgName() {
        return gName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
