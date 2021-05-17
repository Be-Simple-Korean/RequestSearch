package com.example.requestsearch.util;

import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 스크롤 방지 리사이클러뷰 아이템 높이값 세팅 클래스
 */
public class HeightFormat {
    /**
     * 스크롤 방지 리사이클러뷰 아이템 높이값 세팅 메소드
     * @param recyclerView
     * @param holder
     * @param headerView
     * @param noResultView
     * @return
     */
    public RecyclerView.LayoutParams setNoResultViewHeight(RecyclerView recyclerView, RecyclerView.ViewHolder holder, View headerView, View noResultView){
        int recyclerViewHeight = recyclerView.getHeight(); //리사이클러뷰 높이

        RecyclerView.LayoutParams header = (RecyclerView.LayoutParams) headerView.getLayoutParams();
        int headerHeight = header.height; //헤더의 높이
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) noResultView.getLayoutParams();
        //dp->px
        int marginTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 70,
                holder.itemView.getResources().getDisplayMetrics());
        params.height = recyclerViewHeight - (headerHeight + marginTop);
       return params;
    }
}
