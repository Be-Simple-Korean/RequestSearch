package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.BaseVO;
import com.example.requestsearch.data.book.Rss;

import retrofit2.Call;
import retrofit2.Response;

public interface OnCallbackListener<T extends BaseVO> {
    void onResponse(Response<T> response);
    void onFailure(Throwable t);
}
