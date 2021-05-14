package com.example.requestsearch.network;

import android.util.Log;

import com.example.requestsearch.XmlOrJsonConverterFactory;
import com.example.requestsearch.listenerInterface.OnBookDataCallback;
import com.example.requestsearch.listenerInterface.OnDetailBookDataCallback;
import com.example.requestsearch.listenerInterface.OnMovieDataCallback;

import com.example.requestsearch.data.detail.Rss;
import com.example.requestsearch.data.movie.SearchMovieVO;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 네트워크 처리 클래스
 */
public class NetworkManager {

    private Retrofit retrofit;
    private NaverAPI naverAPI;
    private Call<Rss> callBookData;
    private Response<Rss> tmpBookResponse=null;
    private Call<SearchMovieVO> callMovieData;
    private Response<SearchMovieVO> tmpMovieResponse = null;
//    private Call<Rss> callDetailBookData;
//    private Response<Rss> tmpDetailResponse = null;
    private Throwable t = null;


    public NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .addConverterFactory(new XmlOrJsonConverterFactory())
                .client(client.build())
                .build();
        naverAPI = retrofit.create(NaverAPI.class);
    }

    OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request=chain
                    .request()
                    .newBuilder()
                    .addHeader("X-Naver-Client-Id","T92xNEy7zmxTD8fIFj6P")
                    .addHeader("X-Naver-Client-Secret","1G1gY5aG5X")
                    .build();
            return chain.proceed(request);
        }
    });
    /**
     * 책 검색 요청
     * @param word
     * @param start
     * @param display
     * @param sort
     * @param onBookDataCallback
     */
    public void requestBookData(String word, int start, int display, String sort,OnBookDataCallback onBookDataCallback){
//        callBookData=naverAPI.getBookData(clientDataVO.getClientId(),clientDataVO.getClientSecret(),word,start,display,sort);
        callBookData=naverAPI.getBookData(word,start,display,sort);
        callBookData.enqueue(new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {

                tmpBookResponse=response;
                if(onBookDataCallback!=null){
                    onBookDataCallback.onResponse(callBookData,tmpBookResponse);
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                Log.e("TAG",t.getMessage());
                if (onBookDataCallback != null) {
                    onBookDataCallback.onFailure(callBookData, t);
                }
            }
        });
    }
    public void requestDetailBookData(String d_range, String word, int start, int display, String sort, OnDetailBookDataCallback onDetailBookDataCallback){
        naverAPI = retrofit.create(NaverAPI.class);
        Log.e("send in network",d_range+"/"+word+"/"+start+"/"+display+"/"+sort);
        switch (d_range){
            case "책제목":
                callBookData=naverAPI.getRangeDataByTitle(word,start,display,sort);
                break;
            case "저자":
                callBookData=naverAPI.getRangeDataByAuthor(word,start,display,sort);
                break;
            case "출판사":
                callBookData=naverAPI.getRangeDataByPubl(word,start,display,sort);
                break;
        }
        callBookData.enqueue(new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                tmpBookResponse=response;
                if(onDetailBookDataCallback!=null){
                    onDetailBookDataCallback.onResponse(callBookData,tmpBookResponse);
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                t = t;
                if (onDetailBookDataCallback != null) {
                    onDetailBookDataCallback.onFailure(callBookData, t);
                }
            }
        });
    }

    /**
     * 영화 검색 요청
     * @param word
     * @param start
     * @param display
     * @param onMovieDataCallback
     */
    public void requestMovieData(String word, int start, int display, OnMovieDataCallback onMovieDataCallback) {
        callMovieData = naverAPI.getMovieData( word, start, display);
        callMovieData.enqueue(new Callback<SearchMovieVO>() {
            @Override
            public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                tmpMovieResponse = response;
                if (onMovieDataCallback != null) {
                    onMovieDataCallback.onResponse(callMovieData, tmpMovieResponse);
                }
            }

            @Override
            public void onFailure(Call<SearchMovieVO> call, Throwable t) {
                Log.e("TAG",t.getMessage());
                t = t;
                if (onMovieDataCallback != null) {
                    onMovieDataCallback.onFailure(callMovieData, t);
                }
            }
        });


    }

    /**
     * 영화 장르 검색 요청
     * @param word
     * @param start
     * @param display
     * @param position
     * @param onCallBack
     */
    public void requestMovieGenreData(String word, int start, int display, int position, OnMovieDataCallback onCallBack) {
        callMovieData = naverAPI.getMovieDataWithGenre( word, start, display, position);
        callMovieData.enqueue(new Callback<SearchMovieVO>() {
            @Override
            public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                tmpMovieResponse = response;
                if (onCallBack != null) {
                    onCallBack.onResponse(callMovieData, tmpMovieResponse);
                }

            }

            @Override
            public void onFailure(Call<SearchMovieVO> call, Throwable t) {
                t = t;
                if (onCallBack != null) {
                    onCallBack.onFailure(callMovieData, t);
                }
            }
        });
    }
}
