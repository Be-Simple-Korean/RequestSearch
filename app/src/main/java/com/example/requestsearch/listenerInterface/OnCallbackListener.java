package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.network.data.BaseVO;

import retrofit2.Response;

public interface OnCallbackListener<T extends BaseVO> {
    void onResponse(Response<T> response);
    void onFailure(Throwable t);
}
