package com.example.requestsearch.network;

import android.util.Log;

import com.example.requestsearch.listenerInterface.OnBookDataCallback;
import com.example.requestsearch.listenerInterface.OnDetailBookDataCallback;
import com.example.requestsearch.listenerInterface.OnMovieDataCallback;
import com.example.requestsearch.data.ClientDataVO;
import com.example.requestsearch.data.book.SearchBook;
import com.example.requestsearch.data.detail.Rss;
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
    private Call<Rss> callDetailBookData;
    private Response<Rss> tmpDetailResponse = null;
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

//    /**
//     * xml로 데이터를 받지 않기 때문에 사용안함
//     * @param d_range
//     * @param word
//     * @param start
//     * @param display
//     * @param sort
//     * @param onDetailBookDataCallback
//     */
//    public void requestDetailBookData(String d_range, String word, int start, int display, String sort, OnDetailBookDataCallback onDetailBookDataCallback){
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://openapi.naver.com")
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .build();
//        naverAPI = retrofit.create(NaverAPI.class);
//        Log.e("send in network",d_range+"/"+word+"/"+start+"/"+display+"/"+sort);
//        switch (d_range){
//            case "책제목":
//                callDetailBookData=naverAPI.getRangeDataByTitle(clientDataVO.getClientId(),clientDataVO.getClientSecret(),word,start,display,sort);
//                break;
//            case "저자":
//                Log.e("수행",7+"");
//                callDetailBookData=naverAPI.getRangeDataByAuthor(clientDataVO.getClientId(),clientDataVO.getClientSecret(),word,start,display,sort);
//                break;
//            case "출판사":
//                callDetailBookData=naverAPI.getRangeDataByPubl(clientDataVO.getClientId(),clientDataVO.getClientSecret(),word,start,display,sort);
//                break;
//        }
//        Log.e("수행",8+"");
//        callDetailBookData.enqueue(new Callback<Rss>() {
//            @Override
//            public void onResponse(Call<Rss> call, Response<Rss> response) {
//                Log.e("수행",9+"");
//                tmpDetailResponse=response;
//                if(onDetailBookDataCallback!=null){
//                    Log.e("수행",10+"");
//                    onDetailBookDataCallback.onResponse(callDetailBookData,tmpDetailResponse);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Rss> call, Throwable t) {
//                Log.e("수행","9-1");
//                t = t;
//                if (onDetailBookDataCallback != null) {
//                    onDetailBookDataCallback.onFailure(callDetailBookData, t);
//                }
//            }
//        });
//    }

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
