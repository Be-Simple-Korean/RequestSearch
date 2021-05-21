package com.example.requestsearch.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;
import com.example.requestsearch.adapter.SearchAdapter;
import com.example.requestsearch.network.data.CurDataVO;
import com.example.requestsearch.network.data.book.ItemVO;
import com.example.requestsearch.network.data.book.RssVO;
import com.example.requestsearch.network.data.errata.ErrAtaVO;
import com.example.requestsearch.network.data.movie.MovieGenreDataVO;
import com.example.requestsearch.network.data.movie.MovieItemsVO;
import com.example.requestsearch.network.data.movie.SearchMovieVO;
import com.example.requestsearch.adapter.decoration.ItemDecoration;
import com.example.requestsearch.dialog.GenreDialog;
import com.example.requestsearch.dialog.NoWordGuideDialog;
import com.example.requestsearch.dialog.SelectOptionDialog;
import com.example.requestsearch.listenerInterface.OnCallbackListener;
import com.example.requestsearch.listenerInterface.OnDimissListener;
import com.example.requestsearch.listenerInterface.OnItemClick;
import com.example.requestsearch.network.NetworkManager;
import com.example.requestsearch.util.ValueFormat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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
    private ArrayList<ItemVO> detailMainItemVOArrayList;
    private CurDataVO curDataVO;

    private boolean isOpen = true; //탭 활성화 체크 변수
    private NoWordGuideDialog noWordGuideDialog;
    private InputMethodManager inputMethodManager;

    // API에 쓰이는 변수
    private int maxMovieSize = 0; //영화검색결과 총 개수
    private int maxBookSize = 0;  //책검색결과 총 개수
    private int bookCurPosition = 0; //사용자가 보던 책 데이터의 위치값
    private int movieCurPosition = 0; //사용자가 보던 영화 데이터의 위치값
    private int bookStartPosition = 1; // 책검색의 검색시작 위치값
    private int movieStartPosition = 1; // 영화검색의 검색시작 위치값
    private int curCode = 0; //장르 데이터의 코드값
    public String type = "book"; //데이터의 타입 - 책 / 영화
    public String sort = "sim"; //책 검색 데이터의 정렬
    public String d_range = "전체"; // 책 상세검색의 범위


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
                //현재위치가 최상단인지 체크
                if (recyclerView.computeVerticalScrollOffset() == recyclerView.getTop()) {
                    floatingActionButton.hide();
                }
                if (dy > 0) { //아래로 스크롤
                    if (isOpen) { //탭을 닫는 코드
                        floatingActionButton.show();
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
                        movieCurPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        tvBookTab.setTextColor(getResources().getColor(R.color.naver_color));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.black));
                        String word = getWord();
                        setSearchAdapter(TYPE_BOOK, word);
                        if (bookCurPosition > 0) {
                            recyclerView.scrollToPosition(bookCurPosition);
                        }
                    }
                    break;
                case R.id.textview_main_movietab: //탭 - 영화
                    if (!type.equals(TYPE_MOVIE)) {
//                        recyclerView.setAdapter(movieAdapter);
                        type = TYPE_MOVIE;
                        bookCurPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        tvBookTab.setTextColor(getResources().getColor(R.color.black));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.naver_color));
                        String word = getWord();
                        setSearchAdapter(TYPE_MOVIE, word);
                        if (movieCurPosition > 0) {
                            recyclerView.scrollToPosition(movieCurPosition);
                        }
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
     * 어댑터 데이터 세팅
     *
     * @param type
     * @param word
     */
    private void setSearchAdapter(String type, String word) {
        if (type.equals(TYPE_BOOK)||type.equals(TYPE_DETAIL)) {
            searchAdapter.setTypeNumber(SearchAdapter.BOOK_TYPE);
            searchAdapter.setDetailMainItemVOArrayList(detailMainItemVOArrayList);
            searchAdapter.setWord(word);
            searchAdapter.notifyDataSetChanged();
        } else {
            searchAdapter.setTypeNumber(SearchAdapter.MOVIE_TYPE);
            searchAdapter.setMovieItemsArrayList(movieMainItemsArrayList);
            searchAdapter.setWord(word);
            searchAdapter.notifyDataSetChanged();
        }

    }

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
        bookStartPosition = 1;
        movieStartPosition = 1;
        bookCurPosition = 0;
        movieCurPosition = 0;
        curCode = 0;
        setFilterData();
        setGenreList();
        String word = getWord();
        if (!word.equals("")) {
            requestResultCount(word);
        }
    }

    /**
     * 결과값 개수 요청
     */
    private void requestResultCount(String word) {
        recycleBookList();
        networkManager.requestBookData(word, bookStartPosition, DISPLAY, sort, new OnCallbackListener<RssVO>() {
            @Override
            public void onResponse(Response<RssVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    int total = 0;
                    if (response.body() != null) {
                        total = Integer.parseInt(response.body().getChannelVO().getTotal());
                        if (total != EMPTY_SIZE) {
                            setBookDataInList(response);
                        } else {
                            requestErrAtaData(TYPE_BOOK,word);
                        }
                        String resultTotal = new ValueFormat().getTotalFormat("책", total);
                        tvBookTab.setText(resultTotal);
                    }
                    requestMovieCount(word, total);

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    /**
     * 영화 결과값 개수 요청
     *
     * @param word
     */
    private void requestMovieCount(String word, int bookTotal) {
        recycleMovieList();
        networkManager.requestMovieData(word, movieStartPosition, DISPLAY, curCode, new OnCallbackListener<SearchMovieVO>() {
            @Override
            public void onResponse(Response<SearchMovieVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    int movieTotal = 0;
                    if (response.body() != null) {
                        movieTotal = response.body().getTotal();

                        if(movieTotal!=EMPTY_SIZE){
                            setMovieDataInList(response);
                        }else{
                            requestErrAtaData(TYPE_MOVIE,word);
                        }
                        String resultTotal = new ValueFormat().getTotalFormat("영화", movieTotal);
                        tvMovieTab.setText(resultTotal);
                    }
                    // book or movie 탭 선택 결정 로직
                    // book total > 0 ? booklist set : (movie total > 0) ? movielist set :
                    if (bookTotal == 0 && movieTotal != 0) {
                        //어댑터에 영화 데이터 set
                        type = TYPE_MOVIE;
                        tvBookTab.setTextColor(getResources().getColor(R.color.black));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.naver_color));
                        setSearchAdapter(TYPE_MOVIE, word);
                    } else {
                        //어댑터에 책 데이터 set
                        type = TYPE_BOOK;
                        tvBookTab.setTextColor(getResources().getColor(R.color.naver_color));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.black));
                        setSearchAdapter(TYPE_BOOK, word);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
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
    private String getWord() {
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
     * 책 검색 요청
     *
     * @param word
     */
    private void requestSearchBookData(String word) {
        recycleBookList();
        networkManager.requestBookData(word, bookStartPosition, DISPLAY, sort, new OnCallbackListener<RssVO>() {
            @Override
            public void onResponse(Response<RssVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        if (Integer.parseInt(response.body().getChannelVO().getTotal()) != EMPTY_SIZE) {
                            setBookDataInList(response);
                        } else { //total ==0
                            requestErrAtaData(TYPE_BOOK,word);
                        }
                        setSearchAdapter(TYPE_BOOK, word);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    /**
     * 영화 검색 요청
     *
     * @param word
     */
    private void requestSearchMovieData(String word) {
        recycleMovieList();
        networkManager.requestMovieData(word, movieStartPosition, DISPLAY, curCode, new OnCallbackListener<SearchMovieVO>() {
            @Override
            public void onResponse(Response<SearchMovieVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        if (response.body().getTotal() != EMPTY_SIZE) {
                            setMovieDataInList(response);
                        } else {
                            requestErrAtaData( TYPE_MOVIE,word);
                        }
                        setSearchAdapter(TYPE_MOVIE, word);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
//                    requestErrAtaData(word);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    /**
     * 책 상세검색 요청
     *
     * @param word
     */
    private void requestSearchDetailData(String word) {
        recycleBookList();
        networkManager.requestDetailBookData(d_range, word, bookStartPosition, DISPLAY, sort, new OnCallbackListener<RssVO>() {
            @Override
            public void onResponse(Response<RssVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        if(Integer.parseInt(response.body().getChannelVO().getTotal()) != EMPTY_SIZE) {
                            setBookDataInList(response);
                            setSearchAdapter(TYPE_BOOK,word);
                        } else { //total ==0
                            requestErrAtaData(TYPE_DETAIL,word);
                            setSearchAdapter(TYPE_BOOK,word);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });
    }


    /**
     * 오타변환 단어 요청
     *
     * @param word
     */
    private void requestErrAtaData(String type,String word) {
        networkManager.requestErrAtaData(word, new OnCallbackListener<ErrAtaVO>() {
            @Override
            public void onResponse(Response<ErrAtaVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    ErrAtaVO errAtaVo = response.body();
                    if (type.equals(TYPE_BOOK) || type.equals(TYPE_DETAIL)) {
                        ItemVO header = new ItemVO();
                        header.setViewType(SearchAdapter.BOOK_HEADER_TYPE);
                        detailMainItemVOArrayList.add(header); //헤더

                        ItemVO noResultItems = new ItemVO();
                        noResultItems.setViewType(SearchAdapter.NORESULT_TYPE);
                        detailMainItemVOArrayList.add(noResultItems); //결과없음
//                        bookAdapter.setWord(word);
//                        bookAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
                        tvBookTab.setText("책(0)");
                        if (errAtaVo != null && errAtaVo.getErrata() != null) {
                            searchAdapter.setErrata(errAtaVo.getErrata());
                        }
//                        bookAdapter.notifyDataSetChanged();
                        searchAdapter.notifyDataSetChanged();

                    } else { // TYPE_MOVIE
                        MovieItemsVO headerMovieItems = new MovieItemsVO();
                        headerMovieItems.setViewType(SearchAdapter.MOVIE_HEADER_TYPE);
                        movieMainItemsArrayList.add(0, headerMovieItems); //헤더

                        MovieItemsVO noResultItems = new MovieItemsVO();
                        noResultItems.setViewType(SearchAdapter.NORESULT_TYPE);
                        movieMainItemsArrayList.add(1, noResultItems); //결과없음

//                        movieAdapter.setWord(word);
//                        movieAdapter.setMovieMainItemsArrayList(movieMainItemsArrayList);
                        tvMovieTab.setText("영화(0)");
                        if (errAtaVo != null && errAtaVo.getErrata() != null) {
                            searchAdapter.setErrata(errAtaVo.getErrata());
                        }
//                        movieAdapter.notifyDataSetChanged();
//                        searchAdapter.notifyDataSetChanged();
//                        setSearchAdapter(TYPE_MOVIE,word);
                        searchAdapter.notifyDataSetChanged();


                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error Errata", t.getMessage());
            }
        });
    }

    /**
     * 책 검색 결과 리스트 초기화
     */
    private void recycleBookList() {
        if (bookStartPosition == START_POSITION) {
            if (detailMainItemVOArrayList == null) {
                detailMainItemVOArrayList = new ArrayList<>();
            } else {
                detailMainItemVOArrayList.clear();
            }
        }
    }

    /**
     * 영화 데이터 리스트 초기화
     */
    private void recycleMovieList() {
        if (movieStartPosition == START_POSITION) {
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
     */
    private void setBookDataInList(Response<RssVO> response) {
        maxBookSize = Integer.parseInt(response.body().getChannelVO().getTotal());
        if (bookStartPosition == START_POSITION) {
            ItemVO header = new ItemVO();
            header.setViewType(SearchAdapter.BOOK_HEADER_TYPE);
            detailMainItemVOArrayList.add(header); //헤더처리
        }
        detailMainItemVOArrayList.addAll(response.body().getChannelVO().getItemVO());

        if (bookStartPosition != 1) {
//            bookAdapter.notifyDataSetChanged();
            searchAdapter.notifyDataSetChanged();
        }

        String resultTotal = new ValueFormat().getTotalFormat("책", maxBookSize);
        tvBookTab.setText(resultTotal);

        if (maxBookSize > detailMainItemVOArrayList.size()) {
            ItemVO loadMoreItems = new ItemVO();
            loadMoreItems.setViewType(SearchAdapter.LOADMORE_TYPE);
            detailMainItemVOArrayList.add(loadMoreItems);
            bookStartPosition += DISPLAY;
        } else {
            ItemVO finishItemVO = new ItemVO();
            finishItemVO.setViewType(SearchAdapter.FINISH_VIEW_TYPE);
            detailMainItemVOArrayList.add(finishItemVO);
        }
//
//        bookAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
//        bookAdapter.setWord(word);
//        bookAdapter.notifyDataSetChanged();
    }

    /**
     * 영화 데이터 세팅
     *
     * @param response
     * @param
     */
    private void setMovieDataInList(Response<SearchMovieVO> response) {
        maxMovieSize = response.body().getTotal();
        if (movieStartPosition == START_POSITION) {
            MovieItemsVO movieHeader = new MovieItemsVO();
            movieHeader.setViewType(SearchAdapter.MOVIE_HEADER_TYPE);
            movieMainItemsArrayList.add(0, movieHeader); //헤더처리
        }
        movieMainItemsArrayList.addAll(response.body().getItems());
        if (movieStartPosition != 1) {
//            movieAdapter.notifyDataSetChanged();
            searchAdapter.notifyDataSetChanged();
        }

        if (maxMovieSize > movieMainItemsArrayList.size()) {
            MovieItemsVO loadMoreMovieItems = new MovieItemsVO();
            loadMoreMovieItems.setViewType(SearchAdapter.LOADMORE_TYPE);
            movieMainItemsArrayList.add(loadMoreMovieItems);//더보기 버튼 처리
            movieStartPosition += DISPLAY;
        } else {
            MovieItemsVO finishItem = new MovieItemsVO();
            finishItem.setViewType(SearchAdapter.FINISH_VIEW_TYPE);
            movieMainItemsArrayList.add(finishItem);//더보기 버튼 처리
        }
        String resultTotal = new ValueFormat().getTotalFormat("영화", maxMovieSize);
        tvMovieTab.setText(resultTotal);

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
                        curCode = position;
                        movieStartPosition = START_POSITION;
                        for (int i = 0; i < genreList.size(); i++) {
                            if (i == curCode) {
                                genreList.get(i).setSelected(true);
                            } else {
                                genreList.get(i).setSelected(false);
                            }
                        }
                        requestSearchMovieData(getWord());
                    }
                }, 500);
            }
        }
        @Override
        public void onDismissed(SelectOptionDialog dialog, int position,String sorte,String range) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    sort=sorte;
                    d_range=range;
                    bookStartPosition = START_POSITION;
                    String word = getWord();
                    if (!word.equals("")) {
                        if (d_range.equals("전체")) {
                            requestSearchBookData(word);
                        } else {
                            requestSearchDetailData(word);
                        }
                    }
                }
            }, 500);
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
                        intent.putExtra(URL, detailMainItemVOArrayList.get(position).getLink());
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
                        detailMainItemVOArrayList.remove(position);
                        requestSearchBookData(word);
                    } else {
                        movieMainItemsArrayList.remove(position);
                        requestSearchMovieData(word);
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
