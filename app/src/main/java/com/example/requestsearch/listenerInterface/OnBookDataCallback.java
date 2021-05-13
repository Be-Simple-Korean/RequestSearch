package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.book.SearchBookVO;
import com.example.requestsearch.data.detail.Rss;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 책 검색 콜백
 */
public interface OnBookDataCallback {
    void onResponse(Call<Rss> call, Response<Rss> response);
    void onFailure(Call<Rss> call, Throwable t);
}
