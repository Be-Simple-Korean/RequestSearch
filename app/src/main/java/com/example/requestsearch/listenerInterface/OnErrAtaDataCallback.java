package com.example.requestsearch.listenerInterface;

import com.example.requestsearch.data.errata.ErrAtaVo;
import com.example.requestsearch.data.movie.SearchMovieVO;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 오타변환 단어  콜백
 */
public interface OnErrAtaDataCallback {
    void onResponse(Call<ErrAtaVo> call, Response<ErrAtaVo> response);
    void onFailure(Call<ErrAtaVo> call, Throwable t);
}
