package com.example.requestsearch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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


import com.example.requestsearch.ItemDecoration;
import com.example.requestsearch.data.book.Item;
import com.example.requestsearch.data.book.Rss;
import com.example.requestsearch.dialog.SelectOptionDialog;
import com.example.requestsearch.listenerInterface.OnBookDataCallback;
import com.example.requestsearch.listenerInterface.OnDetailBookDataCallback;
import com.example.requestsearch.listenerInterface.OnMovieDataCallback;
import com.example.requestsearch.adapter.BookAdapter;
import com.example.requestsearch.adapter.MovieAdapter;
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
import com.example.requestsearch.util.TotalFormat;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 메인 액티비티
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> detailMainItemArrayList;

    private static final String URL = "url";
    private static final int EMPTY_SIZE = 0;
    private static final int START_POSITION = 1;
    private static final String TAG = "MainActivity";
    private static final int SUCCESS_CODE = 200;
    private static final String TYPE_BOOK = "book";
    private static final String TYPE_MOVIE = "movie";
    private static final String TYPE_DETAIL = "detail";
    private static final String TYPE_MOVIE_GENRE = "movie-genre";
    private static final int DISPLAY = 15;
    private static final int HEADER_TYPE = 0;
    private static final int NORESULT_TYPE = 1;
    private static final int LOADMORE_TYPE = 2;

    private EditText etMainWord;
    private ImageButton ibDeleteWord;
    private TextView tvBookTab,
            tvMovieTab;
    private LinearLayout layoutMainTab;
    private RecyclerView recyclerView;
    private GenreDialog genreDialog;

    private LinearLayoutManager linearLayoutManager;
    private CurDataVO curDataVO;
    private int maxMovieSize = 0;
    private MovieAdapter movieAdapter;
    private BookAdapter bookAdapter;
    private NetworkManager networkManager;
    private ArrayList<MovieGenreDataVO> genreList;
    private ArrayList<MovieItemsVO> movieMainItemsArrayList;
    private int maxBookSize;
    private int start = 1;
    private int curPosition = 0;
    private String type = "book";
    private String sort = "sim";
    private String d_range = "전체";
    private boolean isOpen = true;

    private NoWordGuideDialog noWordGuideDialog;
    private InputMethodManager inputMethodManager;

//TODO
    //         * Q.StringBuilder
    //         * Q.textview- drawble start - drawble.setbounds


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

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        settingEditText();
        curDataVO = new CurDataVO();
        linearLayoutManager = new LinearLayoutManager(this);
        networkManager = new NetworkManager();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(this));
        bookAdapter = new BookAdapter(recyclerView);
        movieAdapter = new MovieAdapter(recyclerView);
        recyclerView.setAdapter(bookAdapter);
        movieAdapter.setOnItemClick(onItemClick);
        bookAdapter.setOnItemClick(onItemClick);
        tvBookTab.setOnClickListener(onClickListener);
        tvMovieTab.setOnClickListener(onClickListener);
        ibDeleteWord.setOnClickListener(onClickListener);
        //검색 버튼
        findViewById(R.id.imagebutton_main_findword).setOnClickListener(view -> {
            if (inputMethodManager.isAcceptingText()) { //키보드가 열려있다면
                inputMethodManager.hideSoftInputFromWindow(etMainWord.getWindowToken(), 0);
            }
            if (etMainWord.getText().length() == 0) {
                noWordGuideDialog = new NoWordGuideDialog(this);
                noWordGuideDialog.show();
            }else{
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
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //아래로 스크롤
                    if (isOpen) { //탭을 닫는 코드
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
                if (etMainWord.getText().length()>0) {
                    ibDeleteWord.setVisibility(View.VISIBLE); //삭제버튼 보이기
                }else if(etMainWord.getText().length()==0){
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
                        resetAndSearch();
                    }
                    break;
                case R.id.textview_main_movietab: //탭 - 영화
                    if (!type.equals(TYPE_MOVIE)) {
                        recyclerView.setAdapter(movieAdapter);
                        start = 1;
                        type = TYPE_MOVIE;
                        tvBookTab.setTextColor(getResources().getColor(R.color.black));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.naver_color));
                        String word = checkWord();
                        requestSearchData(TYPE_MOVIE, word);
                    }
                    break;
            }
        }
    };


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
        tvBookTab.setTextColor(getResources().getColor(R.color.naver_color));
//        tvBookTab.setTextColor(ContextCompat.getColor(this, R.color.naver_color));
        tvMovieTab.setTextColor(getResources().getColor(R.color.black));
        recyclerView.setAdapter(bookAdapter);
        String word = checkWord();
        if (!word.equals("")) {
            requestResultCount(word);
            requestSearchData(TYPE_BOOK, word);
        }
    }

    /**
     * 결과값 개수 요청
     */
    private void requestResultCount(String word) {
        networkManager.requestBookData(word, start, DISPLAY, sort, new OnBookDataCallback() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        int total = Integer.parseInt(response.body().getChannel().getTotal());
                        String resultTotal=new TotalFormat().getTotalFormat("책",total);
                        tvBookTab.setText(resultTotal);
                    }
                }
            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                showNoResultBookData(word);
                Log.e(TAG, t.getMessage());
            }
        });
        networkManager.requestMovieData(word, 1, 100, new OnMovieDataCallback() {
            @Override
            public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                if (response.code() == SUCCESS_CODE) {
                    if (response.body() != null) {
                        int total = response.body().getTotal();
                        String resultTotal=new TotalFormat().getTotalFormat("영화",total);
                        tvMovieTab.setText(resultTotal);
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
            networkManager.requestMovieData(word, start, DISPLAY, new OnMovieDataCallback() {
                @Override
                public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                    if (response.code() == SUCCESS_CODE) {
                        if (response.body() != null) {
                            Log.e("검색 수행","1");
                            setMovieDataInList(response, word);
                        } else {
                            setNoResultMovieDataInList(word);
                        }
                        movieAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<SearchMovieVO> call, Throwable t) {
                    setNoResultMovieDataInList(word);
                    Log.e(TAG, t.getMessage());
                }
            });
        } else if (type.equals(TYPE_BOOK)) {
            Log.e("수행","2");
            recycleBookList();
            networkManager.requestBookData(word, start, DISPLAY, sort, new OnBookDataCallback() {
                @Override
                public void onResponse(Call<Rss> call, Response<Rss> response) {
                    Log.e("code", response.code() + "");
                    if (response.code() == SUCCESS_CODE) {
                        Log.e("수행","3");
                        if (response.body() != null) {
                            Log.e("수행","4");
                            setBookDataInList(response, word);
                        } else { //size==0
                            //오타변환 api 요청
                            /*
                            TODO 오타변환 데이터 요청
                                  받은 데이터를 어댑터에 보내서 띄우고
                                  어댑터에서는 리스너를 통해 데이터를 다시 리턴하거나
                                  여기에 저장된 데이터를 이용해서 검색창에 데이터를 세팅하고
                                  결과수행해서 어댑터 NOTIFY
                             */
                            showNoResultBookData(word);
                        }
                    } else { //body==null
                        showNoResultBookData(word);
                    }
                }

                @Override
                public void onFailure(Call<Rss> call, Throwable t) {
                    showNoResultBookData(word);
                    Log.e(TAG, t.getMessage());
                }
            });
        }
        else if (type.equals(TYPE_DETAIL)) {
            recycleBookList();
            networkManager.requestDetailBookData(d_range, word, start, DISPLAY, sort, new OnDetailBookDataCallback() {
                @Override
                public void onResponse(Call<Rss> call, Response<Rss> response) {
                    Log.e("code", response.code() + "");
                    Log.e("raw", response.raw() + "");
                    if (response.code() == SUCCESS_CODE) {
                        if (response.body() != null) {
                            setBookDataInList(response, word);
                        } else { //size==0
                            showNoResultBookData(word);
                        }
                    } else { //body==null
                        showNoResultBookData(word);
                    }
                }

                @Override
                public void onFailure(Call<Rss> call, Throwable t) {
                    showNoResultBookData(word);
                    Log.e(TAG,t.getMessage());

                }
            });
        }
        else { //영화 장르검색
            recycleMovieList();
            networkManager.requestMovieGenreData(word, start, DISPLAY, curPosition, new OnMovieDataCallback() {
                @Override
                public void onResponse(Call<SearchMovieVO> call, Response<SearchMovieVO> response) {
                    if (response.code() == SUCCESS_CODE) {
                        if (response.body() != null) {
                            setMovieDataInList(response, word);
                        } else {
                            setNoResultMovieDataInList(word);
                        }
                        movieAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<SearchMovieVO> call, Throwable t) {
                    setNoResultMovieDataInList(word);
                    Log.e(TAG, t.getMessage());
                }
            });
        }
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
        if (maxBookSize != EMPTY_SIZE) {
            Log.e("수행","5");
            if (start == START_POSITION) {
                Item header = new Item();
                header.setViewType(HEADER_TYPE);
                detailMainItemArrayList.add(header); //헤더처리
            }
            detailMainItemArrayList.addAll(response.body().getChannel().getItem());

            if (start != 1) {
                bookAdapter.notifyDataSetChanged();
            }
            String resultTotal=new TotalFormat().getTotalFormat("책",maxBookSize);
            tvBookTab.setText(resultTotal);
            if (maxBookSize > DISPLAY) { //더보기
                if (maxBookSize > start + DISPLAY) {
                    Item btnBookItems = new Item();
                    btnBookItems.setViewType(LOADMORE_TYPE);
                    detailMainItemArrayList.add(btnBookItems);
                    start += DISPLAY;
                }
            }
        } else {
            showNoResultBookData(word);
        }
        bookAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
        bookAdapter.setWord(word);
        bookAdapter.notifyDataSetChanged();
    }

    /**
     * 책 검색 결과 없음 세팅
     */
    private void showNoResultBookData(String word) {
        Item header = new Item();
        header.setViewType(HEADER_TYPE);
        detailMainItemArrayList.add(header); //헤더
        Item loadMoreBtn = new Item();
        loadMoreBtn.setViewType(NORESULT_TYPE);
        detailMainItemArrayList.add(loadMoreBtn); //결과없음
        bookAdapter.setWord(word);
        bookAdapter.setDetailMainItemArrayList(detailMainItemArrayList);
        tvBookTab.setText("책(0)");
        bookAdapter.notifyDataSetChanged();
    }

    /**
     * 영화 데이터 세팅
     *
     * @param response
     * @param word
     */
    private void setMovieDataInList(Response<SearchMovieVO> response, String word) {
        maxMovieSize = response.body().getTotal();
        if (maxMovieSize != EMPTY_SIZE) {
            if (start == START_POSITION) {
                MovieItemsVO movieHeader = new MovieItemsVO();
                movieHeader.setViewType(HEADER_TYPE);
                movieMainItemsArrayList.add(0, movieHeader); //헤더처리
            }
            movieMainItemsArrayList.addAll(response.body().getItems());

            if (start != 1) {
                movieAdapter.notifyDataSetChanged();
            }

            if (maxMovieSize > DISPLAY) {
                if (maxMovieSize > +start + DISPLAY) {
                    MovieItemsVO loadMoreMovieItems = new MovieItemsVO();
                    loadMoreMovieItems.setViewType(LOADMORE_TYPE);
                    movieMainItemsArrayList.add(loadMoreMovieItems);//더보기 버튼 처리
                    start += DISPLAY;
                }
            }
            String resultTotal=new TotalFormat().getTotalFormat("영화",maxMovieSize);
            tvMovieTab.setText(resultTotal);
            movieAdapter.setWord(word);
            movieAdapter.setMovieMainItemsArrayList(movieMainItemsArrayList);
            movieAdapter.notifyDataSetChanged();
        } else {
            setNoResultMovieDataInList(word);
        }
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
                dialog.dismiss();
                curPosition = position;
                start = START_POSITION;
                requestSearchData(TYPE_MOVIE_GENRE, curDataVO.getCurWord());
            }
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
                    genreDialog = new GenreDialog(MainActivity.this, genreList);
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
                    SelectOptionDialog selectOptionDialog = new SelectOptionDialog(MainActivity.this,sort,d_range);
                    selectOptionDialog.show();
                    selectOptionDialog.setOnItemClick(onItemClick);
                    break;
                case R.id.textview_option_sort_relevance: //옵션 - 정렬 - 관련도순
                    Log.e("관련도","1");
                    start=1;
                    sort = "sim";
                    word = checkWord();
                    if(!word.equals("")){
                        if (d_range.equals("전체")) {
                            requestSearchData("book", word);
                        } else {
                            requestSearchData("detail", word);
                        }
                    }
                    break;
                case R.id.textview_option_sort_publicationDate: //옵션 - 정렬 - 출간일
                    Log.e("출간일","1");
                    start=1;
                    sort = "date";
                    word = checkWord();
                    if(!word.equals("")){
                        if (d_range.equals("전체")) {
                            Log.e("수행","1");
                            requestSearchData("book", word);
                        } else {
                            requestSearchData("detail", word);
                        }
                    }
                    break;
                case R.id.textview_option_sort_sales: //옵션 - 정렬 - 판매일
                    Log.e("판매량","1");
                    start=1;
                    sort = "count";
                    word = checkWord();
                    if(!word.equals("")){
                        if (d_range.equals("전체")) {
                            requestSearchData("book", word);
                        } else {
                            requestSearchData("detail", word);
                        }
                    }
                    break;
                case R.id.textview_option_range_all: // 옵션 - 범위 - 전체
                    Log.e("수행","전체1");
                    start=1;
                    d_range="전체";
                    word=checkWord();
                    if(!word.equals("")){
                        Log.e("수행","전체1");
                        requestSearchData("book",word);
                    }
                    break;
                case R.id.textview_option_range_title: //옵션 - 범위 - 책제목
                    start=1;
                    Log.e("수행","4");
                    d_range = "책제목";
                    word = checkWord();
                    Log.e("word","="+word);
                    if(!word.equals("")){
                        Log.e("수행","5");
                        requestSearchData("detail",word);
                    }
                    break;
                case R.id.textview_option_range_author: //옵션 - 범위 - 저자
                    start=1;
                    Log.e("저자","4");
                    d_range = "저자";
                    word = checkWord();
                    Log.e("저자 검색 단어","="+word);
                    if(!word.equals("")){
                        Log.e("저자","5");
                        requestSearchData("detail",word);
                    }
                    break;
                case R.id.textview_option_range_publisher: //옵션 - 범위 - 출판사
                    start=1;
                    d_range = "출판사";
                    word = checkWord();
                    if(!word.equals("")){
                        requestSearchData("detail",word);
                    }
                    break;
            }
        }
    };

    /**
     * 영화 검색 결과없음 데이터 세팅
     */
    private void setNoResultMovieDataInList(String word) {
        MovieItemsVO headerMovieItems = new MovieItemsVO();
        headerMovieItems.setViewType(HEADER_TYPE);
        movieMainItemsArrayList.add(0, headerMovieItems); //헤더
        MovieItemsVO noResultItems = new MovieItemsVO();
        noResultItems.setViewType(NORESULT_TYPE);
        movieMainItemsArrayList.add(1, noResultItems); //결과없음
        movieAdapter.setWord(word);
        movieAdapter.setMovieMainItemsArrayList(movieMainItemsArrayList);
        tvMovieTab.setText("영화(0)");
        movieAdapter.notifyDataSetChanged();
    }
}
