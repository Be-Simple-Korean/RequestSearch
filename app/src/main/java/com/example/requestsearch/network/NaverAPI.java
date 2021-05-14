package com.example.requestsearch.network;


import androidx.annotation.XmlRes;

import com.example.requestsearch.data.book.SearchBookVO;
import com.example.requestsearch.data.detail.Rss;
import com.example.requestsearch.data.movie.SearchMovieVO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * 네이버 OPEN API 인터페이스
 */
public interface NaverAPI {

    @GET("/v1/search/movie.json")
    @Json
    Call<SearchMovieVO> getMovieData(
            @Query("query") String query,
            @Query("start") int start,
            @Query("display") int display
    );

    @GET("/v1/search/movie.json")
    @Json
    Call<SearchMovieVO> getMovieDataWithGenre(
            @Query("query") String query,
            @Query("start") int start,
            @Query("display") int display,
            @Query("genre") int genre
    );

    @GET("/v1/search/book.xml")
    @Xml
    Call<Rss> getBookData(
            @Query("query") String query,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/book_adv.xml")
    @Xml
    Call<Rss> getRangeDataByTitle(
            @Query("d_titl") String d_titl,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/book_adv.xml")
    @Xml
    Call<Rss> getRangeDataByAuthor(
            @Query("d_auth") String d_author,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/book_adv.xml")
    @Xml
    Call<Rss> getRangeDataByPubl(
            @Query("d_publ") String d_publ,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Xml {
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Json {
    }
}
