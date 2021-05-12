package com.example.requestsearch.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.requestsearch.ItemDecoration;
import com.example.requestsearch.listenerInterface.OnBookDataCallback;
import com.example.requestsearch.listenerInterface.OnDetailBookDataCallback;
import com.example.requestsearch.listenerInterface.OnMovieDataCallback;
import com.example.requestsearch.adapter.BookAdapter;
import com.example.requestsearch.adapter.MovieAdapter;
import com.example.requestsearch.listenerInterface.OnDimissListener;
import com.example.requestsearch.listenerInterface.OnItemClick;
import com.example.requestsearch.R;
import com.example.requestsearch.data.CurDataVO;
import com.example.requestsearch.data.book.BookItems;
import com.example.requestsearch.data.book.SearchBook;
import com.example.requestsearch.data.detail.Channel;
import com.example.requestsearch.data.detail.Item;
import com.example.requestsearch.data.detail.Rss;
import com.example.requestsearch.data.movie.MovieGenreData;
import com.example.requestsearch.data.movie.MovieItems;
import com.example.requestsearch.data.movie.SearchMovie;
import com.example.requestsearch.dialog.GenreDialog;
import com.example.requestsearch.dialog.NoWordGuideDialog;
import com.example.requestsearch.dialog.OptionDialog;
import com.example.requestsearch.network.NetworkManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int HEADER_TYPE = 0;
    private static final int NORESULT_TYPE = 1;
    private static final int LOADMORE_TYPE = 2;
    private static final int MAIN_TYPE = 3;
    private EditText etMainWord;
    private ImageButton ibDeleteWord,
            ibFindWordButton;
    private TextView tvBookTab,
            tvMovieTab;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Item> detailMainItemArrayList,
            detailSubItemArrayList;
    private ArrayList<MovieItems> movieMainItemsArrayList,
            movieSubItemsArrayList;
    private InputMethodManager inputMethodManager;
    private CurDataVO curDataVO;
    private int maxMovieSize = 0;
    private MovieAdapter movieAdapter;
    private BookAdapter bookAdapter;
    private GenreDialog genreDialog;
    private ArrayList<MovieGenreData> genreList;
    private NetworkManager networkManager;
    private int curPosition = 0;
    private String type = "book";
    private String sort = "sim";
    private String d_range = "전체";
    private boolean isOpen=true;
    private ArrayList<BookItems> bookMainItemsArrayList;
    private ArrayList<BookItems> bookSubItemsArrayList;
    private int maxBookSize;

    //TODO 책 검색 -
    //아이템클릭
    //100개이상
//                 * 옵션다이얼로그 코드 수정
    //         * 스크롤 딜레이 수정 -애니메이터
    //         * 애니메이션 레이아웃 setPadding값 dp로 설정
    //         * StringBuilder
    //         * textview- drawble start - bounds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkManager = new NetworkManager();

        // 영화 장르 리스트 초기화
        genreList = new ArrayList<>();
        Resources r = getResources();
        String[] word = r.getStringArray(R.array.genre);
        for (int i = 0; i < word.length; i++) {
            genreList.add(new MovieGenreData(word[i], false));

        }

        ibDeleteWord = findViewById(R.id.imagebutton_main_deleteword); //검색어 삭제 버튼
        ibFindWordButton = findViewById(R.id.imagebutton_main_findword); //검색 버튼
        tvBookTab = findViewById(R.id.textview_main_booktab); //탭 - 책
        tvMovieTab = findViewById(R.id.textview_main_movietab); //탭 - 영화
        etMainWord = findViewById(R.id.edittext_main_word); //검색어 텍스트뷰
        recyclerView = findViewById(R.id.recyclerview_main);
        LinearLayout layoutMainTab=findViewById(R.id.layout_main_tab);
        curDataVO = new CurDataVO();
        ibFindWordButton.setOnClickListener(onClickListener);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(this));
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        tvBookTab.setOnClickListener(onClickListener);
        tvMovieTab.setOnClickListener(onClickListener);

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
                if (!etMainWord.getText().equals("")) {
                    ibDeleteWord.setVisibility(View.VISIBLE); //삭제버튼 보이기
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
                            NoWordGuideDialog dialog = new NoWordGuideDialog(MainActivity.this);
                            dialog.show();
                        } else {
                            resetAndSearch();
                        }
                        break;
                }
                return false;
            }
        });

        //애니메이션 처리
        Animation animationUp = new TranslateAnimation(0,0,0,-150);
        animationUp.setDuration(300);
        animationUp.setFillAfter(true);
        Animation animationDown = new TranslateAnimation(0,0,-150,0);
        animationDown.setFillAfter(true);
        animationDown.setDuration(300);

        //애니메이터
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    if(isOpen){
                        Log.e("수행","1");
                        recyclerView.setPadding(0,0,0,0);
                        layoutMainTab.startAnimation(animationUp);
                        isOpen=false;
                    }
                }else{
                    if(!isOpen){
                        Log.e("수행","2");
                        recyclerView.setPadding(0,130,0,0);
                        layoutMainTab.startAnimation(animationDown);
                        isOpen=true;
                    }
                }
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
                case R.id.imagebutton_main_findword:  // 검색 버튼 클릭시
                    resetAndSearch();
                    break;
                case R.id.textview_main_booktab: //탭 - 책
                    if (!type.equals("book")) {
                        type = "book";
                        resetAndSearch();
                    }
                    break;
                case R.id.textview_main_movietab: //탭 - 영화
                    if (!type.equals("movie")) {
                        type = "movie";
                        tvBookTab.setText("책");
                        tvBookTab.setTextColor(getResources().getColor(R.color.black));
                        tvMovieTab.setTextColor(getResources().getColor(R.color.naver_color));
                        String word=checkWord();
                        requestSearchData("movie",word);
                    }
                    break;
            }
        }
    };


    /**
     * 검색옵션 초기화
     */
    private void setFilterData() {
        type = "book";
        sort = "sim";
        d_range = "전체";
    }

    /**
     * 검색옵션 초기화 후 검색 수행
     */
    private void resetAndSearch() {
        setFilterData();
        setGenreList();
        tvMovieTab.setText("영화");
        tvBookTab.setTextColor(getResources().getColor(R.color.naver_color));
        tvMovieTab.setTextColor(getResources().getColor(R.color.black));
        String word = checkWord();
        if (!word.equals("")) {
            requestSearchData(type, word); //책검색 수행
        }
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
        String word = "";
        if (etMainWord.getText().length() == 0 || etMainWord.getText().equals("")) { //검색단어가 없다면
            if (curDataVO.getCurWord().length() == 0 || curDataVO.getCurWord().equals("")) { //검색단어도 없고 검색했던 단어도 없는경우
                NoWordGuideDialog noWordGuideDialog = new NoWordGuideDialog(this);
                noWordGuideDialog.show();
                word = "";
            } else { //검색단어는 없지만 검색했던 단어가 존재하는 경우
                word = curDataVO.getCurWord();
            }
        } else { //검색 단어가 있다면
            word = etMainWord.getText().toString();
            curDataVO.setCurWord(etMainWord.getText().toString());
        }
        return word;
    }

    /**
     * 검색 데이터 요청
     */
    private void requestSearchData(String type, String word) {
        if (type.equals("movie")) {
            networkManager.requestMovieData(word, 1, 100, new OnMovieDataCallback() {
                @Override
                public void onResponse(Call<SearchMovie> call, Response<SearchMovie> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            setMovieDataInList(response, word);
                        } else {
                            setNoResultMovieDataInList(word);
                        }
                        movieAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<SearchMovie> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        } else if (type.equals("book")) {
            bookMainItemsArrayList = new ArrayList<>();
            bookSubItemsArrayList = new ArrayList<>();
            networkManager.requestBookData(word, 1, 100, sort, new OnBookDataCallback() {
                @Override
                public void onResponse(Call<SearchBook> call, Response<SearchBook> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            maxBookSize = 0;
                            SearchBook searchBook = response.body();
                            if (searchBook.getTotal() >= 100) {
                                maxBookSize = 100;
                            } else {
                                maxBookSize = searchBook.getTotal();
                            }
                            if (maxBookSize != 0) {
                                BookItems header = new BookItems("", "", "", "", "", "", "", "", "", "");
                                header.setViewType(HEADER_TYPE);
                                bookMainItemsArrayList.add(header); //헤더처리
                                if (maxBookSize > 15) {
                                    for (int i = 0; i < 15; i++) {
                                        if (searchBook.getItems().get(i) == null) {
                                            continue;
                                        } else {
                                            searchBook.getItems().get(i).setViewType(MAIN_TYPE);
                                            bookMainItemsArrayList.add(searchBook.getItems().get(i));
                                        }
                                    }
                                    for (int i = 15; i < maxBookSize; i++) {
                                        if (searchBook.getItems().get(i) == null) {
                                            continue;
                                        } else {
                                            searchBook.getItems().get(i).setViewType(MAIN_TYPE);
                                            bookSubItemsArrayList.add(searchBook.getItems().get(i));
                                        }
                                    }
                                    BookItems btnBookItems = new BookItems("", "", "", "", "", "", "", "", "", "");
                                    btnBookItems.setViewType(LOADMORE_TYPE);
                                    bookMainItemsArrayList.add(btnBookItems); //더보기 버튼 처리
                                    bookAdapter = new BookAdapter(bookMainItemsArrayList, bookSubItemsArrayList, null, null, maxBookSize, 0, word);
                                } else {
                                    for (int i = 0; i < maxBookSize; i++) {
                                        if (searchBook.getItems().get(i) == null) {
                                            continue;
                                        } else {
                                            searchBook.getItems().get(i).setViewType(MAIN_TYPE);
                                            bookMainItemsArrayList.add(searchBook.getItems().get(i));
                                        }
                                    }
                                    bookAdapter = new BookAdapter(bookMainItemsArrayList, null, null, null, maxBookSize, 0, word);
                                }
                                tvBookTab.setText("책(" + maxBookSize + ")");
                                bookAdapter.setOnItemClick(onItemClick);
                                recyclerView.setAdapter(bookAdapter);
                            } else { //maxBookSize == 0
                                tvBookTab.setText("책");
                                maxBookSize = -1; //결과없음 상태
                                BookItems header = new BookItems("", "", "", "", "", "", "", "", "", "");
                                header.setViewType(HEADER_TYPE);
                                bookMainItemsArrayList.add(header); //헤더
                                BookItems btnBookItems = new BookItems("", "", "", "", "", "", "", "", "", "");
                                btnBookItems.setViewType(NORESULT_TYPE);
                                bookMainItemsArrayList.add(btnBookItems); //결과없음
                                bookAdapter = new BookAdapter(bookMainItemsArrayList, null, null, null, maxBookSize, 0, word);
                                bookAdapter.setOnItemClick(onItemClick);
                                recyclerView.setAdapter(bookAdapter);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<SearchBook> call, Throwable t) {
                    tvBookTab.setText("책");
                    maxBookSize = -1; //결과없음 상태
                    BookItems header = new BookItems("", "", "", "", "", "", "", "", "", "");
                    header.setViewType(HEADER_TYPE);
                    bookMainItemsArrayList.add(header); //헤더
                    BookItems btnBookItems = new BookItems("", "", "", "", "", "", "", "", "", "");
                    btnBookItems.setViewType(NORESULT_TYPE);
                    bookMainItemsArrayList.add(btnBookItems); //결과없음
                    bookAdapter = new BookAdapter(bookMainItemsArrayList, null, null, null, maxBookSize, 0, word);
                    bookAdapter.setOnItemClick(onItemClick);
                    recyclerView.setAdapter(bookAdapter);
                }
            });
        } else if (type.equals("detail")) {
            // 상세 검색 - 미사용
//            detailMainItemArrayList=new ArrayList<>();
//            detailSubItemArrayList=new ArrayList<>();
//            Log.e("수행",6+"");
//            Log.e("send",d_range+"/"+word+"/1/100/"+sort);
//            networkManager.requestDetailBookData(d_range, word, 1, 100, sort, new OnDetailBookDataCallback() {
//                @Override
//                public void onResponse(Call<Rss> call, Response<Rss> response) {
//                    Log.e("code",response.code()+"");
//                    Log.e("raw",response.raw()+"");
//                    if(response.code()==200){
//                        if(response.body()!=null){
//                            Channel channel = response.body().getChannel();
//                            maxBookSize = Integer.parseInt(channel.getTotal());
//                            if (maxBookSize > 100) maxBookSize = 100;
//                            if(maxBookSize!=0){
//                                Item header = new Item("","","","","","","","","","");
//                                header.setViewType(HEADER_TYPE);
//                                detailMainItemArrayList.add(header);
//                                if (maxBookSize > 15) {
//                                    for (int i = 0; i < 15; i++) {
//                                        channel.getItem().get(i).setViewType(MAIN_TYPE);
//                                        detailMainItemArrayList.add(channel.getItem().get(i));
//                                    }
//                                    for (int i = 15; i < maxBookSize; i++) {
//                                        channel.getItem().get(i).setViewType(MAIN_TYPE);
//                                        detailSubItemArrayList.add(channel.getItem().get(i));
//                                    }
//                                    Item btnDetailItem = new Item("","","","","","","","","","");
//                                    btnDetailItem.setViewType(LOADMORE_TYPE);
//                                    detailMainItemArrayList.add(btnDetailItem);
//                                } else {
//                                    for (int i = 0; i < maxBookSize; i++) {
//                                        if (channel.getItem().get(i) == null) {
//                                            continue;
//                                        } else {
//                                            channel.getItem().get(i).setViewType(MAIN_TYPE);
//                                            detailMainItemArrayList.add(channel.getItem().get(i));
//                                        }
//                                    }
//                                }
//                                bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,detailSubItemArrayList,maxBookSize,1,word);
//                                bookAdapter.setOnItemClick(onItemClick);
//                                recyclerView.setAdapter(bookAdapter);
//                                bookAdapter.notifyDataSetChanged();
//                            }else{
//                                tvBookTab.setText("책");
//                                maxBookSize = -1; //결과없음 상태
//                                Item header = new Item("","","","","","","","","","");
//                                header.setViewType(HEADER_TYPE);
//                                detailMainItemArrayList.add(header);
//                                Item noResultItem = new Item("","","","","","","","","","");
//                                noResultItem.setViewType(NORESULT_TYPE);
//                                detailMainItemArrayList.add(noResultItem);
//                                bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,detailSubItemArrayList,maxBookSize,1,word);
//                                bookAdapter.setOnItemClick(onItemClick);
//                                recyclerView.setAdapter(bookAdapter);
//                            }
//                        }else{
//                            tvBookTab.setText("책");
//                            maxBookSize = -1; //결과없음 상태
//                            Item header = new Item("","","","","","","","","","");
//                            header.setViewType(HEADER_TYPE);
//                            detailMainItemArrayList.add(header);
//                            Item noResultItem = new Item("","","","","","","","","","");
//                            noResultItem.setViewType(NORESULT_TYPE);
//                            detailMainItemArrayList.add(noResultItem);
//                            bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,detailSubItemArrayList,maxBookSize,1,word);
//                            bookAdapter.setOnItemClick(onItemClick);
//                            recyclerView.setAdapter(bookAdapter);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Rss> call, Throwable t) {
//                    tvBookTab.setText("책");
//                    maxBookSize = -1; //결과없음 상태
//                    Item header = new Item("","","","","","","","","","");
//                    header.setViewType(HEADER_TYPE);
//                    detailMainItemArrayList.add(header);
//                    Item noResultItem = new Item("","","","","","","","","","");
//                    noResultItem.setViewType(NORESULT_TYPE);
//                    detailMainItemArrayList.add(noResultItem);
//                    bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,detailSubItemArrayList,maxBookSize,1,word);
//                    bookAdapter.setOnItemClick(onItemClick);
//                    recyclerView.setAdapter(bookAdapter);
//                    Log.e("error",t.getMessage());
//
//                }
//            });
        } else { //영화 장르검색
            networkManager.requestMovieGenreData(word, 1, 100, curPosition, new OnMovieDataCallback() {
                @Override
                public void onResponse(Call<SearchMovie> call, Response<SearchMovie> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            setMovieDataInList(response, word);
                        } else {
                            setNoResultMovieDataInList(word);
                        }
                        movieAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<SearchMovie> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    /**
     * 영화 데이터 세팅
     *
     * @param response
     * @param word
     */
    private void setMovieDataInList(Response<SearchMovie> response, String word) {
        movieMainItemsArrayList = new ArrayList<>();
        movieSubItemsArrayList = new ArrayList<>();
        SearchMovie searchMovie = response.body();
        if (searchMovie.getTotal() >= 100) {
            maxMovieSize = 100;
        } else {
            maxMovieSize = searchMovie.getTotal();
        }
        if (maxMovieSize != 0) {
            MovieItems movieItems = new MovieItems("", "", "", "", "", "", "", "");
            movieItems.setViewType(HEADER_TYPE);
            movieMainItemsArrayList.add(0, movieItems); //헤더처리
            if (maxMovieSize > 15) {
                for (int i = 0; i < 15; i++) {
                    if (searchMovie.getItems().get(i) == null) {
                        continue;
                    } else {
                        searchMovie.getItems().get(i).setViewType(MAIN_TYPE);
                        movieMainItemsArrayList.add(searchMovie.getItems().get(i));
                    }
                }
                for (int i = 15; i < maxMovieSize; i++) {
                    searchMovie.getItems().get(i).setViewType(MAIN_TYPE);
                    movieSubItemsArrayList.add(searchMovie.getItems().get(i));
                }
                MovieItems loadMoreMovieItems = new MovieItems("", "", "", "", "", "", "", "");
                loadMoreMovieItems.setViewType(LOADMORE_TYPE);
                movieMainItemsArrayList.add(loadMoreMovieItems); //더보기 버튼
                movieAdapter = new MovieAdapter(word, maxMovieSize, movieMainItemsArrayList, movieSubItemsArrayList);
            } else {
                for (int i = 0; i < maxMovieSize; i++) {
                    if (searchMovie.getItems().get(i) == null) {
                        continue;
                    } else {
                        movieMainItemsArrayList.add(searchMovie.getItems().get(i));
                        movieMainItemsArrayList.get(i + 1).setViewType(MAIN_TYPE);
                    }
                }
                movieAdapter = new MovieAdapter(word, maxMovieSize, movieMainItemsArrayList, null);

            }
            tvMovieTab.setText("영화(" + maxMovieSize + ")");
            recyclerView.setAdapter(movieAdapter);
            movieAdapter.setOnItemClick(onItemClick);
        } else {
            tvMovieTab.setText("영화");
            setNoResultMovieDataInList(word);
        }
    }

    /**
     * Dialog 닫기 리스너
     */
    OnDimissListener onDimissListener = new OnDimissListener() {
        @Override
        public void onDismissed(GenreDialog v, boolean status, int position) {
            if (!status) { //닫기 버튼 클릭시
                v.dismiss();
            } else { //아이템 클릭시
                v.dismiss();
                curPosition = position;
                requestSearchData("movie-genre", curDataVO.getCurWord());
            }
        }
    };

    /**
     * Recyclerview 아이템 클릭 리스너
     */
    OnItemClick onItemClick = new OnItemClick() {
        @Override
        public void onItemClick(View v, int position) {
            String word = "";
            switch (v.getId()) {
                case R.id.layout_movie_item:
                case R.id.layout_book_item:  // 아이템 제목 클릭
                    Intent intent = new Intent(MainActivity.this, WebViewActivty.class);
                    if (type.equals("book")) {
                        intent.putExtra("url", bookMainItemsArrayList.get(position).getLink());
                    } else {
                        intent.putExtra("url", movieMainItemsArrayList.get(position).getLink());
                    }
                    startActivity(intent);
                    break;
                case R.id.layout_movie_genre: // 탭 - 영화 - 헤더 - 장르
                    genreDialog = new GenreDialog(MainActivity.this, genreList);
                    genreDialog.show();
                    genreDialog.setOnDimissListener(onDimissListener);
                    break;
//                case R.id.layout_main_option: // 탭 - 책 - 옵션
//                    OptionDialog optionDialog = new OptionDialog(MainActivity.this, sort, d_range);
//                    optionDialog.showDialog();
//                    optionDialog.setOnItemClick(onItemClick);
//                    break;
//                case R.id.textview_option_sort_relevance: //옵션 - 정렬 - 관련도순
//                    sort = "sim";
//                    word = checkWord();
//                    if(!word.equals("")){
//                        if (d_range.equals("전체")) {
//                            requestSearchData("book", word);
//                        } else {
//                            requestSearchData("detail", word);
//                        }
//                    }
//                    break;
//                case R.id.textview_option_sort_publicationDate: //옵션 - 정렬 - 출간일
//                    sort = "date";
//                    word = checkWord();
//                    if(!word.equals("")){
//                        if (d_range.equals("전체")) {
//                            requestSearchData("book", word);
//                        } else {
//                            requestSearchData("detail", word);
//                        }
//                    }
//                    break;
//                case R.id.textview_option_sort_sales: //옵션 - 정렬 - 판매일
//                    sort = "count";
//                    word = checkWord();
//                    if(!word.equals("")){
//                        if (d_range.equals("전체")) {
//                            requestSearchData("book", word);
//                        } else {
//                            requestSearchData("detail", word);
//                        }
//                    }
//                    break;
//                case R.id.textview_option_range_all: // 옵션 - 범위 - 전체
//                    d_range="전체";
//                    word=checkWord();
//                    if(!word.equals("")){
//                        requestSearchData("book",word);
//                    }
//                    break;
//                case R.id.textview_option_range_title: //옵션 - 범위 - 책제목
//                    Log.e("수행","4");
//                    d_range = "책제목";
//                    word = checkWord();
//                    Log.e("word","="+word);
//                    if(!word.equals("")){
//                        Log.e("수행","5");
//                        requestSearchData("detail",word);
//                    }
//                    break;
//                case R.id.textview_option_range_author: //옵션 - 범위 - 저자
//                    Log.e("저자","4");
//                    d_range = "저자";
//                    word = checkWord();
//                    Log.e("저자 검색 단어","="+word);
//                    if(!word.equals("")){
//                        Log.e("저자","5");
//                        requestSearchData("detail",word);
//                    }
//                    break;
//                case R.id.textview_option_range_publisher: //옵션 - 범위 - 출판사
//                    d_range = "출판사";
//                    word = checkWord();
//                    if(!word.equals("")){
//                        requestSearchData("detail",word);
//                    }
//                    break;

            }
        }
    };

    /**
     * 결과없음 데이터 세팅
     */
    private void setNoResultMovieDataInList(String word) {
        maxMovieSize = -1; //결과없음 상태
        MovieItems movieItems = new MovieItems("", "", "", "", "", "", "", "");
        movieItems.setViewType(HEADER_TYPE);
        movieMainItemsArrayList.add(0, movieItems); //헤더
        MovieItems movieItems2 = new MovieItems("", "", "", "", "", "", "", "");
        movieItems2.setViewType(NORESULT_TYPE);
        movieMainItemsArrayList.add(1, movieItems2); //결과없음
        movieAdapter = new MovieAdapter(word, maxMovieSize, movieMainItemsArrayList, null);
        movieAdapter.setOnItemClick(onItemClick);
        recyclerView.setAdapter(movieAdapter);
    }


}
