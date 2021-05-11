package com.example.requestsearch.network;

import android.util.Log;

import com.example.requestsearch.OnBookDataCallback;
import com.example.requestsearch.OnMovieDataCallback;
import com.example.requestsearch.data.ClientDataVO;
import com.example.requestsearch.data.book.SearchBook;
import com.example.requestsearch.data.movie.SearchMovie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class NetworkManager {

    private ClientDataVO clientDataVO = new ClientDataVO();
    private Retrofit retrofit;
    private NaverAPI naverAPI;
    private Call<SearchBook> callBookData;
    private Response<SearchBook> tmpBookResponse=null;
    private Call<SearchMovie> callMovieData;
    private Response<SearchMovie> tmpMovieResponse = null;
    private Throwable t = null;

    public NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        naverAPI = retrofit.create(NaverAPI.class);
    }

    public void requestBookData(String word, int start, int display, String sort,OnBookDataCallback onBookDataCallback){
        callBookData=naverAPI.getBookData(clientDataVO.getClientId(),clientDataVO.getClientSecret(),word,start,display,sort);
        callBookData.enqueue(new Callback<SearchBook>() {
            @Override
            public void onResponse(Call<SearchBook> call, Response<SearchBook> response) {
                tmpBookResponse=response;
                if(onBookDataCallback!=null){
                    onBookDataCallback.onResponse(callBookData,tmpBookResponse);
                }
            }

            @Override
            public void onFailure(Call<SearchBook> call, Throwable t) {
                if (onBookDataCallback != null) {
                    onBookDataCallback.onFailure(callBookData, t);
                }
            }
        });
    }

    public void requestMovieData(String word, int start, int display, OnMovieDataCallback onMovieDataCallback) {
        callMovieData = naverAPI.getMovieData(clientDataVO.getClientId(), clientDataVO.getClientSecret(), word, start, display);
        callMovieData.enqueue(new Callback<SearchMovie>() {
            @Override
            public void onResponse(Call<SearchMovie> call, Response<SearchMovie> response) {
                tmpMovieResponse = response;
                if (onMovieDataCallback != null) {
                    onMovieDataCallback.onResponse(callMovieData, tmpMovieResponse);
                }
            }

            @Override
            public void onFailure(Call<SearchMovie> call, Throwable t) {
                t = t;
                if (onMovieDataCallback != null) {
                    onMovieDataCallback.onFailure(callMovieData, t);
                }
            }
        });


    }

    public void requestMovieGenreData(String word, int start, int display, int position, OnMovieDataCallback onCallBack) {
        callMovieData = naverAPI.getMovieDataWithGenre(clientDataVO.getClientId(), clientDataVO.getClientSecret(), word, start, display, position);
        callMovieData.enqueue(new Callback<SearchMovie>() {
            @Override
            public void onResponse(Call<SearchMovie> call, Response<SearchMovie> response) {
                tmpMovieResponse = response;
                if (onCallBack != null) {
                    onCallBack.onResponse(callMovieData, tmpMovieResponse);
                }

            }

            @Override
            public void onFailure(Call<SearchMovie> call, Throwable t) {
                t = t;
                if (onCallBack != null) {
                    onCallBack.onFailure(callMovieData, t);
                }
            }
        });
    }
}
