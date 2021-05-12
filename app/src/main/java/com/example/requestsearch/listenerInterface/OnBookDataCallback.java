package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.book.SearchBook;
import com.example.requestsearch.data.movie.SearchMovie;

import retrofit2.Call;
import retrofit2.Response;

public interface OnBookDataCallback {
    void onResponse(Call<SearchBook> call, Response<SearchBook> response);
    void onFailure(Call<SearchBook> call,  Throwable t);
}
