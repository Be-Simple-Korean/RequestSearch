package com.example.requestsearch;

import com.example.requestsearch.data.movie.SearchMovie;

import retrofit2.Call;
import retrofit2.Response;

public interface OnMovieDataCallback {
    void onResponse(Call<SearchMovie> call, Response<SearchMovie> response);
    void onFailure(Call<SearchMovie> call,  Throwable t);
}
