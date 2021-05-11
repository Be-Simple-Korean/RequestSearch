package com.example.requestsearch.data.movie;

import com.google.gson.annotations.SerializedName;

public class MovieItems {
    private String title;
    private String link;
    @SerializedName("image")
    private String imgLink;
    private String subtitle;
    @SerializedName("pubDate")
    private String pubDate;
    private String director;
    private String actor;
    private String userRating;
    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public MovieItems(String title, String link, String imgLink, String subtitle, String pubDate, String director, String actor, String userRating) {
        this.title = title;
        this.link = link;
        this.imgLink = imgLink;
        this.subtitle = subtitle;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.userRating = userRating;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDirector() {
        return director;
    }

    public String getActor() {
        return actor;
    }

    public String getUserRating() {
        return userRating;
    }

    @Override
    public String toString() {
        return "MovieItems{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", imgLink='" + imgLink + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", director='" + director + '\'' +
                ", actor='" + actor + '\'' +
                ", userRating='" + userRating + '\'' +
                ", viewType=" + viewType +
                '}'+"\n";
    }
}
