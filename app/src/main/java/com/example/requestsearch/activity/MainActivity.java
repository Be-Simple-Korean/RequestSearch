package com.example.requestsearch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.requestsearch.decoration.ItemDecoration;
import com.example.requestsearch.adapter.SearchAdapter;
import com.example.requestsearch.data.book.Item;
import com.example.requestsearch.data.book.Rss;
import com.example.requestsearch.data.errata.ErrAtaVo;
import com.example.requestsearch.dialog.SelectOptionDialog;

import com.example.requestsearch.listenerInterface.OnCallbackListener;
import com.example.requestsearch.listenerInterface.OnDimissListener;
import com.example.requestsearch.listenerInterface.OnItemClick;
import com.example.requestsearch.R;
import com.example.requestsearch.data.CurDataVO;
import com.example.requestsearch.data.movie.MovieGenreDataVO;
import com.example.requestsearch.data.movie.MovieItemsVO;
import com.example.requestsearch.data.movie.SearchMovieVO;
import com.example.requestsearch.dialog.GenreDialog;
import com.example.requestsearch.dialog.NoWordGuideDialog;
import com.example.requestsearch.network.NetworkManager;
import com.example.requestsearch.util.ValueFormat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 메인 액티비티
 */
//         * Q.StringBuilder

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String TYPE_BOOK = "book";
    private static final String TYPE_MOVIE = "movie";
    private static final String TYPE_DETAIL = "detail";
    private static final String URL = "url";
    private static final int EMPTY_SIZE = 0;
    private static final int START_POSITION = 1;
    private static final int SUCCESS_CODE = 200;
    private static final int DISPLAY = 15;
    private static final int HEADER_TYPE = 0;
    private static final int MOVIE_HEADER_TYPE = 5;
    private static final int NORESULT_TYPE = 1;
    private static final int LOADMORE_TYPE = 2;
    private static final int FINISH_VIEW_TYPE = -1;
    private static final int BOOK_TYPE = 0;
    private static final int MOVIE_TYPE = 1;

    private EditText etMainWord;
    private ImageButton ibDeleteWord;
    private TextView tvBookTab;
    private TextView tvMovieTab;
    private LinearLayout layoutMainTab;
    private RecyclerView recyclerView;
    private GenreDialog genreDialog;
    private FloatingActionButton floatingActionButton;
    private LinearLayoutManager linearLayoutManager;

    //    private MovieAdapter movieAdapter;
//    private BookAdapter bookAdapter;
    private SearchAdapter searchAdapter;
    private NetworkManager networkManager;
    private ArrayList<MovieGenreDataVO> genreList;
    private ArrayList<MovieItemsVO> movieMainItemsArrayList;
    private ArrayList<Item> detailMainItemArrayList;
    private CurDataVO curDataVO;
    private int maxMovieSize = 0;
    private int maxBookSize = 0;
    private int start = 1;
    private int curPosition = 0;
    private String type = "book";
    private String sort = "sim";
    private String d_range = "전체";
    private boolean isOpen = true;
    private boolean isBook = true;
    private boolean isMovie = true;
    private NoWordGuideDialog noWordGuideDialog;
    private InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ibDeleteWord = findViewById(R.id.imagebutton_main_deleteword); //검색어 삭제 버튼
        tvBookTab = findViewById(R.id.textview_main_booktab); //탭 - 책
        tvMovieTab = findViewById(R.id.textview_main_movietab); //탭 - 영화
        etMainWord = findViewById(R.id.edittext_main_word); //검색어 텍스트뷰
        recyclerView = findViewById(R.id.recyclerview_main);
        layoutMainTab = findViewById(R.id.layout_main_tab);
        floatingActionButton = findViewById(R.id.floating_main);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        settingEditText();
        curDataVO = new CurDataVO();
        linearLayoutManager = new LinearLayoutManager(this);
        networkManager = new NetworkManager();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(this));
//        bookAdapter = new BookAdapter(recyclerView);
//        movieAdapter = new MovieAdapter(recyclerView);
        searchAdapter = new SearchAdapter(recyclerView);
        recyclerView.setAdapter(searchAdapter);
//        movieAdapter.setOnItemClick(onItemClick);
//        bookAdapter.setOnItemClick(onItemClick);
        searchAdapter.setOnItemClick(onItemClick);
        tvBookTab.setOnClickListener(onClickListener);
        tvMovieTab.setOnClickListener(onClickListener);
        ibDeleteWord.setOnClickListener(onClickListener);
        floatingActionButton.setOnClickListener(onClickListener);

        //검색 버튼
        findViewById(R.id.imagebutton_main_findword).setOnClickListener(view -> {
            if (inputMethodManager.isAcceptingText()) { //키보드가 열려있다면
                inputMethodManager.hideSoftInputFromWindow(etMainWord.getWindowToken(), 0);
            }
            if (etMainWord.getText().length() == 0) {
                noWordGuideDialog = new NoWordGuideDialog(this);
                noWordGuideDialog.show();
            } else {
                resetAndSearch();
            }
        });

        // 영화 장르 리스트 초기화
        genreList = new ArrayList<>();
        Resources r = getResources();
        String[] word = r.getStringArray(R.array.genre);
        for (int i = 0; i < word.length; i++) {
            genreList.add(new MovieGenreDataVO(word[i], false));
        }

        //애니메이터
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.computeVerticalScrollOffset() == recyclerView.getTop()) {
//                    floatingActionButton.setVisibility(View.INVISIBLE);
                    floatingActionButton.hide();
                }
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //아래로 스크롤
                    if (isOpen) { //탭을 닫는 코드
                        floatingActionButton.show();
//                        floatingActionButton.setVisibility(View.VISIBLE);
                        showAnimation(0, -layoutMainTab.getHeight());
                    }
                } else { //현재 위치를 기준으로 위로 스크롤
                    if (!isOpen) { //탭을 여는 코드
                        showAnimation(-layoutMainTab.getHeight(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        curDataVO.setCurWord("");
    }


    /**
     * 메인 액티비티 클릭 리스너
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imagebutton_main_deleteword: //검색창 X버튼 클릭
                    etMainWord.setText("");
                    ibDeleteWord.setVisibility(View.INVISIBLE);
                    break;
                case R.id.textview_main_booktab: //탭 - 책
                    if (!type.equals(TYPE_BOOK)) {
                        type = TYPE_BOOK;
                        start = 1;
                        tvBookTab.setTextColor(getResources().getColor(R.color.naver_color));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.black));
                        String word = checkWord();
                        requestSearchData(type, word);
                    }
                    break;
                case R.id.textview_main_movietab: //탭 - 영화
                    if (!type.equals(TYPE_MOVIE)) {
//                        recyclerView.setAdapter(movieAdapter);
                        start = 1;
                        type = TYPE_MOVIE;
                        curPosition = 0;
                        setGenreList();
                        tvBookTab.setTextColor(getResources().getColor(R.color.black));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.naver_color));
                        String word = checkWord();
                        requestSearchData(TYPE_MOVIE, word);
                    }
                    break;
                case R.id.floating_main: //플로팅 액션버튼
                    recyclerView.scrollToPosition(0);
//                    floatingActionButton.setVisibility(View.INVISIBLE);
                    floatingActionButton.hide();
                    break;
            }
        }
    };


    /**
     * ObjectAnimator 설정 메소드
     *
     * @param firstValue  오브젝트 속성 시작값
     * @param secondValue 오브젝트 속성 끝값
     */
    private void showAnimation(int firstValue, int secondValue) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(layoutMainTab, "translationY", firstValue, secondValue);
        anim.setDuration(500);
        anim.start();
        isOpen = !isOpen;
    }

    /**
     * EditText 부가 설정
     */
    private void settingEditText() {
        //검색어 삭제버튼 처리
        etMainWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //변경되기전 문자열
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //변경될떄마다 호출
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //변경된 이후
                if (etMainWord.getText().length() > 0) {
                    ibDeleteWord.setVisibility(View.VISIBLE); //삭제버튼 보이기
                } else if (etMainWord.getText().length() == 0) {
                    ibDeleteWord.setVisibility(View.INVISIBLE); //삭제버튼 보이기
                }
            }
        });

        // 키보드 다음 -> 검색
        etMainWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        inputMethodManager.hideSoftInputFromWindow(etMainWord.getWindowToken(), 0);
                        if (etMainWord.getText().length() == 0) {
                            noWordGuideDialog = new NoWordGuideDialog(MainActivity.this);
                            noWordGuideDialog.show();
                        } else {
                            resetAndSearch();
                        }
                        break;
                }
                return false;
            }
        });

    }


    /**
     * 검색옵션 초기화
     */
    private void setFilterData() {
        type = TYPE_BOOK;
        sort = "sim";
        d_range = "전체";
    }

    /**
     * 검색옵션 초기화 후 검색 수행
     */
    private void resetAndSearch() {
        start = 1;
        setFilterData();
        setGenreList();
        String word = checkWord();
        if (!word.equals("")) {
            requestResultCount(word);
        }
    }

    /**
     * 결과값 개수 요청
     */
    private void requestResultCount(String word) {
        networkManager.requestBookData2(word, start, DISPLAY, sort, new OnCallbackListener<Rss>() {

            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        int total = Integer.parseInt(response.body().getChannel().getTotal());
                        if (total == EMPTY_SIZE) {
                            isBook = false;
                        } else {
                            isBook = true;
                        }
                        String resultTotal = new ValueFormat().getTotalFormat("책", total);
                        tvBookTab.setText(resultTotal);
                    }
                    requestMovieCount(word);
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    /**
     * 영화 결과값 개수 요청
     *
     * @param word
     */
    private void requestMovieCount(String word) {
        networkManager.requestMovieData(word, start, DISPLAY, curPosition, new OnCallbackListener<SearchMovieVO>() {
            @Override
            public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        int total = response.body().getTotal();
                        if (total == EMPTY_SIZE) {
                            isMovie = false;
                        } else {
                            isMovie = true;
                        }
                        String resultTotal = new ValueFormat().getTotalFormat("영화", total);
                        tvMovieTab.setText(resultTotal);
                    }
                    if ((isBook && isMovie) || (isBook && !isMovie) || (!isBook && !isMovie)) {
                        tvBookTab.setTextColor(getResources().getColor(R.color.naver_color));
//        tvBookTab.setTextColor(ContextCompat.getColor(this, R.color.naver_color));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.black));
//                                    recyclerView.setAdapter(bookAdapter);
//                                    recyclerView.setAdapter(dataAdapter);
                        type = TYPE_BOOK;
                        requestSearchData(TYPE_BOOK, word);
                    } else {
                        tvBookTab.setTextColor(getResources().getColor(R.color.black));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.naver_color));
//                                    recyclerView.setAdapter(movieAdapter);
                        type = TYPE_MOVIE;
                        requestSearchData(TYPE_MOVIE, word);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchMovieVO> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    /**
     * 장르리스트 초가화
     */
    private void setGenreList() {
        for (int i = 0; i < genreList.size(); i++) {
            genreList.get(i).setSelected(false);
        }
        //DEFALUT = 전체
        genreList.get(0).setSelected(true);
    }

    /**
     * 검색어 확인
     *
     * @return "" => 단어 없음 , else = 검색 단어 return
     */
    private String checkWord() {
        String sMainWord = etMainWord.getText().toString();
        String word = "";

        if (sMainWord.trim().length() == 0) { //검색단어가 없다면
            if (curDataVO.getCurWord().trim().length() == 0) { //검색단어도 없고 검색했던 단어도 없는경우
                noWordGuideDialog = new NoWordGuideDialog(this);
                noWordGuideDialog.show();
                word = "";
            } else { //검색단어는 없지만 검색했던 단어가 존재하는 경우
                word = curDataVO.getCurWord();
            }
        } else { //검색 단어가 있다면
            word = sMainWord;
            curDataVO.setCurWord(word);
        }
        return word;
    }

    /**
     * 검색 데이터 요청
     */
    private void requestSearchData(String type, String word) {
        if (type.equals(TYPE_MOVIE)) {
            recycleMovieList();
            networkManager.requestMovieData(word, start, DISPLAY, curPosition, new OnCallbackListener<SearchMovieVO>() {
                @Override
                public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                    if (response.code() == SUCCESS_CODE) {
                        if (response.body() != null) {
                            if (response.body().getTotal() != EMPTY_SIZE) {
                                setMovieDataInList(response, word);
                            } else {
                                requestErrAtaData(word);
                            }
                        } else { //body==null
                            requestErrAtaData(word);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchMovieVO> call, Throwable t) {
                    requestErrAtaData(word);
                    Log.e(TAG, t.getMessage());
                }
            });
        } else if (type.equals(TYPE_BOOK)) {
            recycleBookList();
            networkManager.requestBookData(word, start, DISPLAY, sort, new OnCallbackListener<Rss>() {
                @Override
                public void onResponse(Call<Rss> call, Response<Rss> response) {
                    if (response.code() == SUCCESS_CODE) {
                        if (response.body() != null) {
                            if (Integer.parseInt(response.body().getChannel().getTotal()) != EMPTY_SIZE) {
                                setBookDataInList(response, word);
                            } else { //total ==0
                                requestErrAtaData(word);
                            }
                        } else { //response==null
                            requestErrAtaData(word);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Rss> call, Throwable t) {
                    requestErrAtaData(word);
                    Log.e(TAG, t.getMessage());
                }
            });
        } else if (type.equals(TYPE_DETAIL)) {
            recycleBookList();
            networkManager.requestDetailBookData(d_range, word, start, DISPLAY, sort, new OnCallbackListener<Rss>() {
                @Override
                public void onResponse(Call<Rss> call, Response<Rss> response) {
                    if (response.code() == SUCCESS_CODE) {
                        if (response.body() != null) {
                            if (Integer.parseInt(response.body().getChannel().getTotal()) != EMPTY_SIZE) {
                                setBookDataInList(response, word);
                            } else { //total ==0
                                requestErrAtaData(word);
                            }
                        } else { //body==null
                            requestErrAtaData(word);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Rss> call, Throwable t) {
                    requestErrAtaData(word);
                    Log.e(TAG, t.getMessage());

                }
            });
        }
    }

    /**
     * 오타변환 단어 요청
     *
     * @param word
     */
    private void requestErrAtaData(String word) {
        networkManager.requestErrAtaData(word, new OnCallbackListener<ErrAtaVo>() {
            @Override
            public void onResponse(Call<ErrAtaVo> call, Response<ErrAtaVo> response) {
                if (response.code() == SUCCESS_CODE) {
                    ErrAtaVo errAtaVo = response.body();
                    if (type.equals(TYPE_BOOK) || type.equals(TYPE_DETAIL)) {
                        Item header = new Item();
                        header.setViewType(HEADER_TYPE);
                        detailMainItemArrayList.add(header); //헤더

                        Item noResultItems = new Item();
                        noResultItems.setViewType(NORESULT_TYPE);
                        detailMainItemArrayList.add(noResultItems); //결과없음

//                        bookAdapter.setWord(word);
//                        bookAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
                        searchAdapter.setTypeNumber(BOOK_TYPE);
                        searchAdapter.setWord(word);
                        searchAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
                        tvBookTab.setText("책(0)");
                        if (errAtaVo != null && errAtaVo.getErrata() != null) {
                            searchAdapter.setErrata(errAtaVo.getErrata());
                        }
//                        bookAdapter.notifyDataSetChanged();
                        searchAdapter.notifyDataSetChanged();
                    } else { // TYPE_MOVIE
                        MovieItemsVO headerMovieItems = new MovieItemsVO();
                        headerMovieItems.setViewType(MOVIE_HEADER_TYPE);
                        movieMainItemsArrayList.add(HEADER_TYPE, headerMovieItems); //헤더

                        MovieItemsVO noResultItems = new MovieItemsVO();
                        noResultItems.setViewType(NORESULT_TYPE);
                        movieMainItemsArrayList.add(NORESULT_TYPE, noResultItems); //결과없음

                        searchAdapter.setTypeNumber(1);
                        searchAdapter.setWord(word);
                        searchAdapter.setMovieItemsArrayList(movieMainItemsArrayList);
//                        movieAdapter.setWord(word);
//                        movieAdapter.setMovieMainItemsArrayList(movieMainItemsArrayList);
                        tvMovieTab.setText("영화(0)");
                        if(errAtaVo!= null && errAtaVo.getErrata()!=null){
                            searchAdapter.setErrata(errAtaVo.getErrata());
                        }
//                        movieAdapter.notifyDataSetChanged();
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<ErrAtaVo> call, Throwable t) {
                Log.e("error Errata", t.getMessage());
            }
        });
    }

    /**
     * 책 검색 결과 리스트 초기화
     */
    private void recycleBookList() {
        if (start == START_POSITION) {
            if (detailMainItemArrayList == null) {
                detailMainItemArrayList = new ArrayList<>();
            } else {
                detailMainItemArrayList.clear();
            }
        }
    }

    /**
     * 영화 데이터 리스트 초기화
     */
    private void recycleMovieList() {
        if (start == START_POSITION) {
            if (movieMainItemsArrayList == null) {
                movieMainItemsArrayList = new ArrayList<>();
            } else {
                movieMainItemsArrayList.clear();
            }
        }
    }

    /**
     * 책 검색 데이터 리스트에 세팅
     *
     * @param response
     * @param word
     */
    private void setBookDataInList(Response<Rss> response, String word) {
        maxBookSize = Integer.parseInt(response.body().getChannel().getTotal());
        if (start == START_POSITION) {
            Item header = new Item();
            header.setViewType(HEADER_TYPE);
            detailMainItemArrayList.add(header); //헤더처리
        }
        detailMainItemArrayList.addAll(response.body().getChannel().getItem());

        if (start != 1) {
//            bookAdapter.notifyDataSetChanged();
            searchAdapter.notifyDataSetChanged();
        }

        String resultTotal = new ValueFormat().getTotalFormat("책", maxBookSize);
        tvBookTab.setText(resultTotal);

        Log.e("max/size", maxBookSize + "/" + detailMainItemArrayList.size());
        if (maxBookSize > detailMainItemArrayList.size()) {
            Item loadMoreItems = new Item();
            loadMoreItems.setViewType(LOADMORE_TYPE);
            detailMainItemArrayList.add(loadMoreItems);
            start += DISPLAY;
        } else {
            Log.e("finish", "수행");
            Item finishItem = new Item();
            finishItem.setViewType(FINISH_VIEW_TYPE);
            detailMainItemArrayList.add(finishItem);
        }

        searchAdapter.setTypeNumber(BOOK_TYPE);
        searchAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
        searchAdapter.setWord(word);
        searchAdapter.notifyDataSetChanged();
//
//        bookAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
//        bookAdapter.setWord(word);
//        bookAdapter.notifyDataSetChanged();
    }

    /**
     * 영화 데이터 세팅
     *
     * @param response
     * @param word
     */
    private void setMovieDataInList(Response<SearchMovieVO> response, String word) {
        maxMovieSize = response.body().getTotal();
        if (start == START_POSITION) {
            MovieItemsVO movieHeader = new MovieItemsVO();
            movieHeader.setViewType(MOVIE_HEADER_TYPE);
            movieMainItemsArrayList.add(0, movieHeader); //헤더처리
        }
        movieMainItemsArrayList.addAll(response.body().getItems());

        if (start != 1) {
//            movieAdapter.notifyDataSetChanged();
            searchAdapter.notifyDataSetChanged();
        }

        if (maxMovieSize > movieMainItemsArrayList.size()) {
            MovieItemsVO loadMoreMovieItems = new MovieItemsVO();
            loadMoreMovieItems.setViewType(LOADMORE_TYPE);
            movieMainItemsArrayList.add(loadMoreMovieItems);//더보기 버튼 처리
            start += DISPLAY;
        } else {
            MovieItemsVO finishItem = new MovieItemsVO();
            finishItem.setViewType(FINISH_VIEW_TYPE);
            movieMainItemsArrayList.add(finishItem);//더보기 버튼 처리
        }
        String resultTotal = new ValueFormat().getTotalFormat("영화", maxMovieSize);
        tvMovieTab.setText(resultTotal);

        searchAdapter.setTypeNumber(MOVIE_TYPE);
        searchAdapter.setWord(word);
        searchAdapter.setMovieItemsArrayList(movieMainItemsArrayList);
        searchAdapter.notifyDataSetChanged();
    }

    /**
     * Dialog 닫기 리스너
     */
    OnDimissListener onDimissListener = new OnDimissListener() {
        @Override
        public void onDismissed(GenreDialog dialog, boolean isItem, int position) {
            if (!isItem) { //닫기 버튼 클릭시
                dialog.dismiss();
            } else { //아이템 클릭시
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        curPosition = position;
                        start = START_POSITION;
                        for (int i = 0; i < genreList.size(); i++) {
                            if (i == curPosition) {
                                genreList.get(i).setSelected(true);
                            } else {
                                genreList.get(i).setSelected(false);
                            }
                        }
                        requestSearchData(TYPE_MOVIE, checkWord());
                    }
                },500);
            }
        }

        @Override
        public void onDismissed(SelectOptionDialog dialog, int position, boolean isRange) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            if (isRange) {
                                d_range = "전체";
                            } else {
                                sort = "sim";
                            }
                            break;
                        case 1:
                            if (isRange) {
                                d_range = "책제목";
                            } else {
                                sort = "date";
                            }
                            break;
                        case 2:
                            if (isRange) {
                                d_range = "저자";
                            } else {
                                sort = "count";
                            }
                            break;
                        case 3:
                            d_range = "출판사";
                            break;
                    }
                    start = 1;
                    String word = checkWord();
                    if (!word.equals("")) {
                        if (d_range.equals("전체")) {
                            requestSearchData("book", word);
                        } else {
                            requestSearchData("detail", word);
                        }
                    }
                }
            },500);
        }
    };

    /**
     * Recyclerview 아이템 클릭 리스너
     */
    OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onItemClick(View v, int position, String word) {
            switch (v.getId()) {
                case R.id.layout_movie_item:
                case R.id.layout_book_item:  // 아이템 제목 클릭
                    Intent intent = new Intent(MainActivity.this, WebViewActivty.class);
                    if (type.equals(TYPE_BOOK)) {
                        intent.putExtra(URL, detailMainItemArrayList.get(position).getLink());
                    } else {
                        intent.putExtra(URL, movieMainItemsArrayList.get(position).getLink());
                    }
                    startActivity(intent);
                    break;
                case R.id.layout_movie_genre: // 탭 - 영화 - 헤더 - 장르
                    genreDialog = new GenreDialog(MainActivity.this, genreList, type);
                    genreDialog.setOnDimissListener(onDimissListener);
                    genreDialog.show();
                    break;
                case R.id.btn_recyclerview_loadmore: //더보기 버튼
                    if (type.equals(TYPE_BOOK)) {
                        detailMainItemArrayList.remove(position);
                        requestSearchData(TYPE_BOOK, word);
                    } else {
                        movieMainItemsArrayList.remove(position);
                        requestSearchData(TYPE_MOVIE, word);
                    }
                    break;
                case R.id.layout_main_option: // 탭 - 책 - 옵션
                    SelectOptionDialog copySelectOptionDialog = new SelectOptionDialog(MainActivity.this, sort, d_range, type);
                    copySelectOptionDialog.show();
                    copySelectOptionDialog.setOnDimissListener(onDimissListener);
                    break;
                case R.id.textview_item_noresult: // 결과없음 - 오타변환단어
                    etMainWord.setText(word);
                    resetAndSearch();
                    break;
            }
        }
    };

}
