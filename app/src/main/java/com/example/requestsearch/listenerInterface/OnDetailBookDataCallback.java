package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.book.SearchBook;
import com.example.requestsearch.data.detail.Rss;

import retrofit2.Call;
import retrofit2.Response;

public interface OnDetailBookDataCallback {
    void onResponse(Call<Rss> call, Response<Rss> response);
    void onFailure(Call<Rss> call,  Throwable t);
}
