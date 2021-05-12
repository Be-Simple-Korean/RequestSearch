package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.movie.SearchMovieVO;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 영화 검색 콜백
 */
public interface OnMovieDataCallback {
    void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response);
    void onFailure(Call<SearchMovieVO> call, Throwable t);
}
