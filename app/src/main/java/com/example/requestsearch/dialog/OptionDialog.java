package com.example.requestsearch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.OnItemClick;
import com.example.requestsearch.R;
import com.example.requestsearch.data.ClientDataVO;
import com.example.requestsearch.data.detail.Item;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OptionDialog {
    private Context context;
    private Dialog dialog;
    private TextView tvOptionSortRelevance, tvOptionSortPublicationDate, tvOptionSortSales,
            tvOptionRangeAll, tvOptionRangeTitle, tvOptionRangeAuthor, tvOptionRangePublisher;
    private ImageView ivOptionSortRelevance, ivOptionSortPublicationDate, ivOptionSortSales, ivOptionRangeAll,
            ivOptionRangeTitle, ivOptionRangeAuthor, ivOptionRangePublisher;
//    private SortData sortData;
//    private CurData curData;
    private NoWordGuideDialog noWordGuideDialog;
    private int maxBookSize;
 //   private BookAdapter bookAdapter;
    private ClientDataVO clientData;
    private String sort;
    //private Retrofit retrofit;

    private ArrayList<Item> detailMainItemArrayList;
    private ArrayList<Item> detailSubItemArrayList;
    private RecyclerView recyclerView;

    OnItemClick onItemClick=null;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public OptionDialog(Context context,String sort) {
        this.context = context;
        this.sort=sort;
    }

    public void showDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_option_select);
        noWordGuideDialog = new NoWordGuideDialog(context);
        setFindViewById();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);
            window.setGravity(Gravity.BOTTOM);
        }
          uiSetOptionSort();
//        uiSetRange();
        dialog.show();
        ImageView ivCloseDialog = dialog.findViewById(R.id.imageview_dialog_close); // X(닫기) 이미지 버튼형식
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("수행","!");
                dialog.dismiss();
            }
        });

//        //정렬-관련도순
//        tvOptionSortRelevance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (!sortData.getSort().equals("sim")) {
//                    sortData.setSort("sim");
//                    searchWithDRange();
//                }
//            }
//        });
//
        //정렬-출간일순
        tvOptionSortPublicationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sort.equals("date")){
                    if(onItemClick!=null){
                        dialog.dismiss();
                        onItemClick.onItemClick(v,1);
                    }
                }
            }
        });
        //정렬 - 판매일순
        tvOptionSortSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sort.equals("count")){
                    if(onItemClick!=null){
                        dialog.dismiss();
                        onItemClick.onItemClick(v,2);
                    }
                }
            }
        });
//
//        //정렬-판매일순
//        tvOptionSortSales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (!sortData.getSort().equals("count")) {
//                    sortData.setSort("count");
//                    searchWithDRange();
//                }
//            }
//        });
//
//        //범위 - 전체
//        tvOptionRangeAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (curData.getCurWord().length() == 0) {
//                    noWordGuideDialog.show();
//                } else {
//                    sortData.setD_range("전체");
//                    searchWithDRange();
//                }
//
//            }
//        });
//
//        //범위 - 책제목
//        tvOptionRangeTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!sortData.getD_range().equals("책제목")) {
//                    dialog.dismiss();
//                    if (curData.getCurWord().length() == 0) {
//                        noWordGuideDialog.show();
//                    } else {
//                        sortData.setD_range("책제목");
//                        searchWithDRange();
//                    }
//                }
//            }
//        });
//
//        //범위 - 저자
//        tvOptionRangeAuthor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!sortData.getD_range().equals("저자")) {
//                    dialog.dismiss();
//                    if (curData.getCurWord().length() == 0) {
//                        noWordGuideDialog.show();
//                    } else {
//                        sortData.setD_range("저자");
//                        searchWithDRange();
//                    }
//                }
//            }
//        });
//
//        //범위 - 출판사
//        tvOptionRangePublisher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!sortData.getD_range().equals("출판사")) {
//                    dialog.dismiss();
//                    if (curData.getCurWord().length() == 0) {
//                        noWordGuideDialog.show();
//                    } else {
//                        sortData.setD_range("출판사");
//                        searchWithDRange();
//                    }
//                }
//
//            }
//        });

    }

//    /**
//     * 정렬로 상세검색 수행시 범위에 맞게 검색
//     */
//    private void searchWithDRange() {
//        switch (sortData.getD_range()) {
//            case "전체":
//                if (curData.getCurWord().length() == 0) {
//                    noWordGuideDialog.show();
//                } else {
//                    requestSerachBookData();
//                }
//                break;
//            case "책제목":
//                if (curData.getCurWord().length() == 0) {
//                    noWordGuideDialog.show();
//                } else {
//                    uiSetRange();
//                    requestDetailSearchData("title");
//                }
//                break;
//            case "저자":
//                Log.e("수행","저자1");
//                Log.e("word",curData.getCurWord());
//                if (curData.getCurWord().length() == 0) {
//                    noWordGuideDialog.show();
//                } else {
//                    uiSetRange();
//                    requestDetailSearchData("author");
//                }
//                break;
//            case "출판사":
//                if (curData.getCurWord().length() == 0) {
//                    noWordGuideDialog.show();
//                } else {
//                    uiSetRange();
//                    requestDetailSearchData("publ");
//                }
//                break;
//        }
//    }
//
//
//    /**
//     * 상세검색
//     * @param type 상세검색 유형
//     */
//    private void requestDetailSearchData(String type) {
//        retrofitData = new RetrofitData("xml");
//        retrofit = retrofitData.getRetrofit();
//        naverAPI = retrofit.create(NaverAPI.class);
//        String word = curData.getCurWord();
//        Call<Rss> call;
//        detailMainItemArrayList = new ArrayList<>();
//        detailSubItemArrayList = new ArrayList<>();
//        if (type.equals("title")) {
//            call = naverAPI.getRangeDataByTitle(clientData.getClientId(),clientData.getClientSecret(), 100, sortData.getSort(), word);
//        } else if (type.equals("author")) {
//            call = naverAPI.getRangeDataByAuthor(clientData.getClientId(),clientData.getClientSecret(), 100, sortData.getSort(), word);
//        } else {
//            call = naverAPI.getRangeDataByPubl(clientData.getClientId(),clientData.getClientSecret(), 100, sortData.getSort(), word);
//        }
//
//        call.enqueue(new Callback<Rss>() {
//            @Override
//            public void onResponse(Call<Rss> call, Response<Rss> response) {
//                if (response.body() == null || response.body().getChannel().getTotal().equals("0")) {
//                    showNoSearchDetailData(word);
//                } else {
//                    Channel channel = response.body().getChannel();
//                    maxBookSize = Integer.parseInt(channel.getTotal());
//                    if (maxBookSize > 100) maxBookSize = 100;
//                    detailMainItemArrayList.add(null);
//                    if (maxBookSize > 15) {
//                        for (int i = 0; i < 15; i++) {
//                            detailMainItemArrayList.add(channel.getItem().get(i));
//                        }
//                        for (int i = 15; i < maxBookSize; i++) {
//                            detailSubItemArrayList.add(channel.getItem().get(i));
//                        }
//                        detailMainItemArrayList.add(null);
//                        bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,detailSubItemArrayList,maxBookSize,1,word);
//                    } else {
//                        detailMainItemArrayList = channel.getItem();
//                        bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,null,maxBookSize,1,word);
//                    }
//                    recyclerView.setAdapter(bookAdapter);
//                    bookAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Rss> call, Throwable t) {
//                        showNoSearchDetailData(word);
//            }
//        });
//    }
//
//    /**
//     * 상세검색 결과없음
//     * @param word
//     */
//    private void showNoSearchDetailData(String word){
//        detailMainItemArrayList.clear();
//        detailSubItemArrayList.clear();
//        maxBookSize=0;
//        detailMainItemArrayList.add(null); //헤더처리
//        detailMainItemArrayList.add(null); //결과없음처리
//        bookAdapter = new BookAdapter(null,null,detailMainItemArrayList,null,maxBookSize,-1,word); // -1= 검색 결과 없음
//        recyclerView.setAdapter(bookAdapter);
//    }
//
//    /**
//     * 기본 책 데이터 가져오기
//      */
//    private void requestSerachBookData() {
//        clientData = new ClientData();
//        retrofitData = new RetrofitData("json");
//        retrofit = retrofitData.getRetrofit();
//        naverAPI = retrofit.create(NaverAPI.class);
//        String word = curData.getCurWord();
//        Call<SearchBook> searchBookCall = naverAPI.getBookData(clientData.getClientId(), clientData.getClientSecret(), word, 100, sortData.getSort());
//        ArrayList<BookItems> bookMainItemsArrayList = new ArrayList<>();
//        ArrayList<BookItems> bookSubItemsArrayList = new ArrayList<>();
//        maxBookSize = 0;
//        searchBookCall.enqueue(new Callback<SearchBook>() {
//            @Override
//            public void onResponse(Call<SearchBook> call, Response<SearchBook> response) {
//                if (response.body() != null) {
//                    SearchBook searchBook = response.body();
//                    if (searchBook.getTotal() >= 100) {
//                        maxBookSize = 100;
//                    } else {
//                        maxBookSize = searchBook.getTotal();
//                    }
//                    if (maxBookSize != 0) {
//                        bookMainItemsArrayList.add(null); //헤더처리
//                        if (maxBookSize > 15) {
//                            for (int i = 0; i < 15; i++) {
//                                if (searchBook.getItems().get(i) == null) {
//                                    continue;
//                                } else {
//                                    bookMainItemsArrayList.add(searchBook.getItems().get(i));
//                                }
//                            }
//                            for (int i = 15; i < maxBookSize; i++) {
//                                if (searchBook.getItems().get(i) == null) {
//                                    continue;
//                                } else {
//                                    bookSubItemsArrayList.add(searchBook.getItems().get(i));
//                                }
//                            }
//                            bookMainItemsArrayList.add(null); //더보기 버튼 처리
//                            bookAdapter = new BookAdapter(bookMainItemsArrayList, bookSubItemsArrayList, null, null, maxBookSize, 0, word);
//                        } else {
//                            for (int i = 0; i < maxBookSize; i++) {
//                                if (searchBook.getItems().get(i) == null) {
//                                    continue;
//                                } else {
//                                    bookMainItemsArrayList.add(searchBook.getItems().get(i));
//                                }
//                            }
//                            bookAdapter = new BookAdapter(bookMainItemsArrayList, null, null, null, maxBookSize, 0, word);
//                        }
//                        MainActivity mainActivity = new MainActivity();
//                        mainActivity.setBookText(maxBookSize);
//                        recyclerView.setAdapter(bookAdapter);
//                    } else { //maxBookSize == 0
//                        maxBookSize = -1; //결과없음 상태
//                        bookMainItemsArrayList.add(null); //헤더
//                        bookMainItemsArrayList.add(null); //결과없음
//                        bookAdapter = new BookAdapter(bookMainItemsArrayList, null, null, null, maxBookSize, 0, word);
//                        recyclerView.setAdapter(bookAdapter);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SearchBook> call, Throwable t) {
//                maxBookSize = -1;
//                bookMainItemsArrayList.add(null); //헤더
//                bookMainItemsArrayList.add(null); //결과없음
//                bookAdapter = new BookAdapter(bookMainItemsArrayList, null, null, null, maxBookSize, 0, word);
//                recyclerView.setAdapter(bookAdapter);
//                Log.e("t", t.getMessage());
//            }
//        });
//    }
//
//
//    /**
//     * 상세검색 범위 ui set
//     */
//    private void uiSetRange() {
//        tvOptionRangeAll.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
//        ivOptionRangeAll.setImageResource(R.drawable.sort_baseicon);
//        tvOptionRangeTitle.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
//        ivOptionRangeTitle.setImageResource(R.drawable.sort_baseicon);
//        tvOptionRangeAuthor.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
//        ivOptionRangeAuthor.setImageResource(R.drawable.sort_baseicon);
//        tvOptionRangePublisher.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
//        ivOptionRangePublisher.setImageResource(R.drawable.sort_baseicon);
//        switch (sortData.getD_range()) {
//            case "전체":
//                tvOptionRangeAll.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
//                ivOptionRangeAll.setImageResource(R.drawable.sort_selecticon);
//                break;
//            case "책제목":
//                tvOptionRangeTitle.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
//                ivOptionRangeTitle.setImageResource(R.drawable.sort_selecticon);
//                break;
//            case "저자":
//                tvOptionRangeAuthor.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
//                ivOptionRangeAuthor.setImageResource(R.drawable.sort_selecticon);
//                break;
//            case "출판사":
//                tvOptionRangePublisher.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
//                ivOptionRangePublisher.setImageResource(R.drawable.sort_selecticon);
//                break;
//
//        }
//    }
//
    /**
     * 상세검색 정렬 ui set
     */
    private void uiSetOptionSort() {
        tvOptionSortRelevance.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionSortRelevance.setImageResource(R.drawable.sort_baseicon);
        tvOptionSortPublicationDate.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionSortPublicationDate.setImageResource(R.drawable.sort_baseicon);
        tvOptionSortSales.setTextColor(dialog.getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionSortSales.setImageResource(R.drawable.sort_baseicon);
        switch (sort) {
            case "sim":
                tvOptionSortRelevance.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
                ivOptionSortRelevance.setImageResource(R.drawable.sort_selecticon);
                break;
            case "date":
                tvOptionSortPublicationDate.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
                ivOptionSortPublicationDate.setImageResource(R.drawable.sort_selecticon);
                break;
            case "count":
                tvOptionSortSales.setTextColor(dialog.getContext().getResources().getColor(R.color.black));
                ivOptionSortSales.setImageResource(R.drawable.sort_selecticon);
                break;
        }
    }

    /**
     * findviewbyid set
     */
    private void setFindViewById() {
        ivOptionSortRelevance = dialog.findViewById(R.id.imageview_option_sort_relevance); // 옵션 - 정렬 - 관련도
        tvOptionSortRelevance = dialog.findViewById(R.id.textview_option_sort_relevance); // 옵션 - 정렬 - 관련도
        tvOptionSortPublicationDate = dialog.findViewById(R.id.textview_option_sort_publicationDate); // 옵션 - 정렬 - 출간일
        ivOptionSortPublicationDate = dialog.findViewById(R.id.imageview_option_sort_publicationDate); // 옵션 - 정렬 - 출간일
        tvOptionSortSales = dialog.findViewById(R.id.textview_option_sort_sales); // 옵션 - 정렬 - 판매량
        ivOptionSortSales = dialog.findViewById(R.id.imageview_option_sort_sales); // 옵션 - 정렬 - 판매량
        tvOptionRangeAll = dialog.findViewById(R.id.textview_option_range_all); // 옵션 - 범위 - 전체
        ivOptionRangeAll = dialog.findViewById(R.id.imageview_option_range_all); // 옵션 - 범위 - 전체
        tvOptionRangeTitle = dialog.findViewById(R.id.textview_option_range_title); // 옵션 - 범위 - 제목
        ivOptionRangeTitle = dialog.findViewById(R.id.imageview_option_range_title); // 옵션 - 범위 - 제목
        tvOptionRangeAuthor = dialog.findViewById(R.id.textview_option_range_author); // 옵션 - 범위 - 저자
        ivOptionRangeAuthor = dialog.findViewById(R.id.imageview_option_range_author); // 옵션 - 범위 - 저자
        tvOptionRangePublisher = dialog.findViewById(R.id.textview_option_range_publisher); // 옵션 - 범위 - 출판사
        ivOptionRangePublisher = dialog.findViewById(R.id.imageview_option_range_publisher); // 옵션 - 범위 - 출판사
    }
}
