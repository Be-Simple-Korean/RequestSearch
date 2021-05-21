package com.example.requestsearch.data.movie;

import com.example.requestsearch.data.BaseVO;
import com.google.gson.annotations.SerializedName;

/**
 * 영화 검색 결과 데이터
 */
public class MovieItemsVO extends BaseVO{
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
    private int viewType=4;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
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
