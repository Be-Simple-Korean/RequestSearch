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
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requestsearch.R;
import com.example.requestsearch.listenerInterface.OnItemClick;

/**
 * 결과없음 단어 Spannble 처리 클래스
 */
public class SpannableFormat {
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
