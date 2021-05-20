package com.example.requestsearch.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;
import com.example.requestsearch.listenerInterface.OnItemClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValueFormat {
    /**
     * 날짜 형식 필터링메소드
     * @param date 네이버에서 가져오는 날짜데이터
     * @return 필터링된 날짜데이터
     */
    public String getDateFormat(String date){
        String newPubDate="";
        SimpleDateFormat oldDataFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            java.util.Date formatDate = oldDataFormat.parse(date);
            newPubDate = newDateFormat.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newPubDate;
    }

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

    /**
     * 가격 1000단위 포맷
     * @param price
     * @return
     */
    public String getPriceFormat(String price){
        if(price.contains(".")) {
            int index=price.indexOf(".");
            price=price.substring(0,index);
        }
        return String.format("%,d", Integer.parseInt(price));
    }

    /**
     * 결과 개수 포맷
     * @param type
     * @param total
     * @return
     */
    public String getTotalFormat(String type,int total){
        String result="";
        if(total==0){
            return type+"(0)";
        }else{
            if(total>9999){
                return type+"(9999)";
            }else{
                return type+"("+total+")";
            }
        }
    }

    /**
     * 결과없음 단어 Spannble 처리 메소드
     *
     * @param holder
     * @param errata
     * @param onItemClick
     * @param word
     * @return
     */
    public SpannableStringBuilder getSpannableData(RecyclerView.ViewHolder holder, String errata, OnItemClick onItemClick, String word) {

        //검색단어처리
        SpannableStringBuilder curWordSpannable = new SpannableStringBuilder("'" + word + "'");
        curWordSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, curWordSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        curWordSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, curWordSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        curWordSpannable.append("에 대한 검색결과를 찾을 수 없습니다.\n");
        curWordSpannable.setSpan(new AbsoluteSizeSpan(25, true), 0, curWordSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //오타변환단어처리
        if (!errata.equals("")) {
            SpannableStringBuilder errataSpannable = new SpannableStringBuilder("'" + errata + "'");
            errataSpannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    onItemClick.onItemClick(widget, 0, errata);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(holder.itemView.getResources().getColor(R.color.changeWordColor));
                }
            }, 0, errataSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            errataSpannable.append(" 검색 결과 보기");
            curWordSpannable.append(errataSpannable);
        }

        return curWordSpannable;
    }
}
