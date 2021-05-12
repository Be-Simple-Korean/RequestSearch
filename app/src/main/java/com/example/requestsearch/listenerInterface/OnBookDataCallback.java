package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.book.SearchBookVO;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 책 검색 콜백
 */
public interface OnBookDataCallback {
    void onResponse(Call<SearchBookVO> call, Response<SearchBookVO> response);
    void onFailure(Call<SearchBookVO> call, Throwable t);
}
