package com.example.requestsearch.data.movie;

public class MovieGenreData {

    private String gName;

    private boolean isSelected = false;

    public MovieGenreData(String gName, boolean isSelected) {
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
