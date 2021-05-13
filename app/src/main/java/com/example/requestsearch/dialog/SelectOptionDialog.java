package com.example.requestsearch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.requestsearch.R;
import com.example.requestsearch.data.movie.MovieGenreDataVO;
import com.example.requestsearch.listenerInterface.OnDimissListener;
import com.example.requestsearch.listenerInterface.OnItemClick;

import java.util.ArrayList;

/**
 * 장르 항목 표시 다이얼로그
 */
public class SelectOptionDialog extends Dialog {

    private TextView tvOptionSortRelevance, tvOptionSortPublicationDate, tvOptionSortSales,
            tvOptionRangeAll, tvOptionRangeTitle, tvOptionRangeAuthor, tvOptionRangePublisher;
    private ImageView ivOptionSortRelevance, ivOptionSortPublicationDate, ivOptionSortSales, ivOptionRangeAll,
            ivOptionRangeTitle, ivOptionRangeAuthor, ivOptionRangePublisher;

    private String sort;
    private String d_range;

    private OnItemClick onItemClick = null;


    public SelectOptionDialog(@NonNull Context context,String sort, String d_range) {
        super(context);
        this.sort = sort;
        this.d_range = d_range;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option_select);
        setFindViewById();
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);
            window.setGravity(Gravity.BOTTOM);
        }
        uiSetOptionSort();
        uiSetRange();
        ImageView ivCloseDialog = findViewById(R.id.imageview_dialog_close); // X(닫기) 이미지 버튼형식
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //정렬-관련도순
        tvOptionSortRelevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sort.equals("sim")) {
                    dismiss();
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 0,"");
                    }
                }
            }
        });

        //정렬-출간일순
        tvOptionSortPublicationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sort.equals("date")) {
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 1,"");
                    }
                }
            }
        });
        //정렬 - 판매일순
        tvOptionSortSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sort.equals("count")) {
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 2,"");
                    }
                }
            }
        });

        //범위 - 전체
        tvOptionRangeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!d_range.equals("전체")) {
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 3,"");
                    }
                }
            }
        });

        //범위 - 책제목
        tvOptionRangeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!d_range.equals("책제목")) {
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 3,"");
                    }
                }
            }
        });

        //범위 - 저자
        tvOptionRangeAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!d_range.equals("저자")) {
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 3,"");
                    }
                }
            }
        });

        //범위 - 출판사
        tvOptionRangePublisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!d_range.equals("출판사")) {
                    if (onItemClick != null) {
                        dismiss();
                        onItemClick.onItemClick(v, 3,"");
                    }
                }
            }
        });

    }

    /**
     * 아이템 클릭 리스너 설정
     * @param onItemClick
     */
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    /**
     * findviewbyid set
     */
    private void setFindViewById() {
        ivOptionSortRelevance = findViewById(R.id.imageview_option_sort_relevance); // 옵션 - 정렬 - 관련도
        tvOptionSortRelevance = findViewById(R.id.textview_option_sort_relevance); // 옵션 - 정렬 - 관련도
        tvOptionSortPublicationDate = findViewById(R.id.textview_option_sort_publicationDate); // 옵션 - 정렬 - 출간일
        ivOptionSortPublicationDate = findViewById(R.id.imageview_option_sort_publicationDate); // 옵션 - 정렬 - 출간일
        tvOptionSortSales = findViewById(R.id.textview_option_sort_sales); // 옵션 - 정렬 - 판매량
        ivOptionSortSales = findViewById(R.id.imageview_option_sort_sales); // 옵션 - 정렬 - 판매량
        tvOptionRangeAll = findViewById(R.id.textview_option_range_all); // 옵션 - 범위 - 전체
        ivOptionRangeAll = findViewById(R.id.imageview_option_range_all); // 옵션 - 범위 - 전체
        tvOptionRangeTitle = findViewById(R.id.textview_option_range_title); // 옵션 - 범위 - 제목
        ivOptionRangeTitle = findViewById(R.id.imageview_option_range_title); // 옵션 - 범위 - 제목
        tvOptionRangeAuthor = findViewById(R.id.textview_option_range_author); // 옵션 - 범위 - 저자
        ivOptionRangeAuthor = findViewById(R.id.imageview_option_range_author); // 옵션 - 범위 - 저자
        tvOptionRangePublisher = findViewById(R.id.textview_option_range_publisher); // 옵션 - 범위 - 출판사
        ivOptionRangePublisher = findViewById(R.id.imageview_option_range_publisher); // 옵션 - 범위 - 출판사
    }

    /**
     * 상세검색 정렬 ui set
     */
    private void uiSetOptionSort() {
        tvOptionSortRelevance.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionSortRelevance.setImageResource(R.drawable.sort_baseicon);
        tvOptionSortPublicationDate.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionSortPublicationDate.setImageResource(R.drawable.sort_baseicon);
        tvOptionSortSales.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionSortSales.setImageResource(R.drawable.sort_baseicon);
        switch (sort) {
            case "sim":
                tvOptionSortRelevance.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionSortRelevance.setImageResource(R.drawable.sort_selecticon);
                break;
            case "date":
                tvOptionSortPublicationDate.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionSortPublicationDate.setImageResource(R.drawable.sort_selecticon);
                break;
            case "count":
                tvOptionSortSales.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionSortSales.setImageResource(R.drawable.sort_selecticon);
                break;
        }
    }

    /**
     * 상세검색 범위 ui set
     */
    private void uiSetRange() {
        tvOptionRangeAll.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionRangeAll.setImageResource(R.drawable.sort_baseicon);
        tvOptionRangeTitle.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionRangeTitle.setImageResource(R.drawable.sort_baseicon);
        tvOptionRangeAuthor.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionRangeAuthor.setImageResource(R.drawable.sort_baseicon);
        tvOptionRangePublisher.setTextColor(getContext().getResources().getColor(R.color.baseTextColor));
        ivOptionRangePublisher.setImageResource(R.drawable.sort_baseicon);
        switch (d_range) {
            case "전체":
                tvOptionRangeAll.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionRangeAll.setImageResource(R.drawable.sort_selecticon);
                break;
            case "책제목":
                tvOptionRangeTitle.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionRangeTitle.setImageResource(R.drawable.sort_selecticon);
                break;
            case "저자":
                tvOptionRangeAuthor.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionRangeAuthor.setImageResource(R.drawable.sort_selecticon);
                break;
            case "출판사":
                tvOptionRangePublisher.setTextColor(getContext().getResources().getColor(R.color.black));
                ivOptionRangePublisher.setImageResource(R.drawable.sort_selecticon);
                break;

        }
    }
}
