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
     * @param holder
     * @param errata
     * @param onItemClick
     * @param word
     * @return
     */
    public SpannableStringBuilder getSpannableData(RecyclerView.ViewHolder holder, String errata, OnItemClick onItemClick, String word){
        String guideText = holder.itemView.getResources().getString(R.string.text_no_result_guide);
        Log.e("errata", errata);
        if (errata.equals("")) {
            int index = guideText.lastIndexOf("\n");
            Log.e("no data index", index + "");
            guideText = guideText.substring(0, index);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(guideText);

        //기존검색단어 처리
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int firstIndex = guideText.indexOf(".") + 1;
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(25, true), 0, firstIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //오타변환단어 처리
        if (!errata.equals("")) {
            int lastIndex = guideText.lastIndexOf("'");
            spannableStringBuilder.setSpan(new ForegroundColorSpan(
                            holder.itemView.getResources().getColor(R.color.changeWordColor)),
                    lastIndex - 1,
                    lastIndex + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //클릭이벤트처리
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    onItemClick.onItemClick(widget,0,errata);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(holder.itemView.getResources().getColor(R.color.changeWordColor
                    ));
                }
            },lastIndex-1,lastIndex+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spannableStringBuilder.insert(1, word);

        if (!errata.equals("")) {
            String midResult = spannableStringBuilder.toString();
            int index = midResult.lastIndexOf("'");
            spannableStringBuilder.insert(index, errata);
        }
        return spannableStringBuilder;
    }
}
