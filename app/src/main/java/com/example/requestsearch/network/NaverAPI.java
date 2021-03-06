package com.example.requestsearch.network;


import com.example.requestsearch.network.data.book.RssVO;
import com.example.requestsearch.network.data.errata.ErrAtaVO;
import com.example.requestsearch.network.data.movie.SearchMovieVO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import retrofit2.Call;
import retrofit2.http.GET;
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
            @Query("display") int display,
            @Query("genre") int genre
    );

    @GET("/v1/search/book.xml")
    @Xml
    Call<RssVO> getBookData(
            @Query("query") String query,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/book_adv.xml")
    @Xml
    Call<RssVO> getRangeDataByTitle(
            @Query("d_titl") String d_titl,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/book_adv.xml")
    @Xml
    Call<RssVO> getRangeDataByAuthor(
            @Query("d_auth") String d_author,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/book_adv.xml")
    @Xml
    Call<RssVO> getRangeDataByPubl(
            @Query("d_publ") String d_publ,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort
    );

    @GET("/v1/search/errata.json")
    @Json
    Call<ErrAtaVO> getErrAtaData(
            @Query("query") String query
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
